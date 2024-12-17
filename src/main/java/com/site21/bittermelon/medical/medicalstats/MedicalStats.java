package com.site21.bittermelon.medical.medicalstats;

import com.site21.bittermelon.blocks.FluidBlock;
import com.site21.bittermelon.blocks.blockentities.FluidBlockEntity;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.blood.BloodType;
import com.site21.bittermelon.medical.compartments.*;
import com.site21.bittermelon.medical.compartments.bodyparts.BodyPart;
import com.site21.bittermelon.medical.compartments.conditions.ForeignSubstance;
import com.site21.bittermelon.medical.compartments.conditions.infections.Infection;
import com.site21.bittermelon.medical.compartments.organs.HeartRhythm;
import com.site21.bittermelon.miscellaneous.stumble.StumbleHandler;
import com.site21.bittermelon.substance.SubstanceStack;
import com.site21.bittermelon.util.LocalMessageHelper;
import com.site21.bittermelon.util.ServerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.site21.bittermelon.init.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.Substances.LIQUID_BLOOD;

public class MedicalStats {
    private final CopyOnWriteArrayList<Compartment> compartments;
    private final BloodType bloodType;
    private float consciousness = 1;
    private float oxygenSaturation = 100;
    private float bloodVolume = 100;
    private float BPM;
    private float bloodPressureSystolic;
    private float bloodPressureDiastolic;
    private float temperature;
    private HeartRhythm heartRhythm;
    private final EnumMap<FunctionType, Float> stats = new EnumMap<>(FunctionType.class);
    private final Map<UUID, Float> immunity = new HashMap<>();
    private final Character character;
    private final transient LivingEntity entity;
    private int puddleTimer = 0;
    private static final int PUDDLE_INTERVAL = 40;


    public MedicalStats(BloodType bloodType, List<Compartment> compartments, @NotNull Character character) {
        this.bloodType = bloodType;
        this.compartments = new CopyOnWriteArrayList<>(compartments);
        this.character = character;

        this.entity = ServerUtil.getLivingEntity(character.getEntityUUID());

        for (FunctionType type : FunctionType.values()) {
            stats.put(type, 0f);
        }
    }

    public void update() {
        List<Compartment> compartmentsCopy = new ArrayList<>(compartments);
        for (Compartment compartment : compartmentsCopy) {
            if (compartment == null) {
                continue;
            }
            if (!compartments.contains(compartment)) {
                continue;
            }
            compartment.update(this);
            if (compartment instanceof Condition) {
                immuneSystem(compartment);
                elimination(compartment);
                heal(compartment);
            }
        }
        if (entity != null) {
            movement();
            manipulation();
            brain();
//
//            puddleTimer++;
//
//            if (puddleTimer >= PUDDLE_INTERVAL) {
//                bloodPuddle();
//                puddleTimer = 0;
//            }
        }
        updateCardiopulmonary();
        updateStats();
    }

    public void removeCompartment(@NotNull Compartment compartment) {
        compartment.getOwner().getChildren().remove(compartment);
        compartments.remove(compartment);

        List<Compartment> childrenToRemove = new ArrayList<>(compartment.getChildren());
        for (Compartment child : childrenToRemove) {
            if (compartment.hasType(CompartmentType.MAJOR_BODY_PART)) {
                removeCompartment(child);
            } else if (child instanceof Condition) {
                removeCompartment(child);
            }
        }


        // TODO: Severed vessels and such for connecting compartments
    }

    public void extractCompartment(@NotNull Compartment compartment) {
        compartment.onExtract(this);
        removeCompartment(compartment);
    }

    public void addCompartment(Compartment compartment) {
        compartments.add(compartment);
    }

    public List<Compartment> getCompartments() {
        return compartments;
    }

    public void modifyBloodVolume(float amount) {
        bloodVolume = Math.max(0, Math.min(amount + bloodVolume, 100));
    }

    public void modifyOxygenSaturation(float amount) {
        oxygenSaturation = Math.max(0, Math.min(amount + oxygenSaturation, 100));
    }

    private void updateStats() {
        EnumMap<FunctionType, Float> statsCopy = new EnumMap<>(FunctionType.class);
        for (FunctionType type : FunctionType.values()) {
            statsCopy.put(type, 0f);
        }

        for (Compartment compartment : compartments) {
            for (FunctionType stat : FunctionType.values()) {
                float attribute = compartment.getAttribute(stat);
                if (attribute > 0) {
                    statsCopy.compute(stat, (k, currentValue) -> currentValue + attribute);
                }
            }
        }

        stats.putAll(statsCopy);
    }

    private void updateCardiopulmonary() {
        modifyBloodVolume(0.01f - stats.get(FunctionType.BLEED) / 100);
        modifyOxygenSaturation(stats.get(FunctionType.RESPIRATORY) * stats.get(FunctionType.BRAIN_VITALS) - 0.01f);

        System.out.println(bloodVolume);

        if (bloodVolume < 60 || oxygenSaturation < 80) {
            for (Compartment compartment : compartments) {
                if (compartment instanceof BodyPart) {
                    compartment.modifyHealth(-0.001f);
                }
            }
        }

        // TODO: Random heart state depending on heart health
    }

    private void brain() {
        consciousness = stats.get(FunctionType.BRAIN_VITALS);

        System.out.println(consciousness);

        if (consciousness < 0.1f) {
//            LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal("Entity passes out"));
        }
    }

    private void immuneSystem(Compartment compartment) {
        if (compartment instanceof Infection infection) {
            float immunityRate = stats.get(FunctionType.IMMUNITY);
            Compartment owner = infection.getOwner();
//                addCompartment(new Inflammation(owner.getName() + "inflammation", owner, infection.getMaxHealth()));
            // TODO: Add inflammation
            float memory = immunity.get(infection.getOrganism());
            infection.modifyHealth(-immunityRate * memory / 10);
            immunity.compute(infection.getOrganism(), (k, currentImmunity) -> currentImmunity + immunityRate / 10);
        }
    }

    private void elimination(Compartment compartment) {
        if (compartment instanceof ForeignSubstance foreignSubstance) {
            float eliminationRate = stats.get(FunctionType.ELIMINATION);
            foreignSubstance.modifyHealth(-eliminationRate / 10);
            // TODO: Separate metabolism and elimination?
        }
    }

    private void heal(Compartment compartment) {
        if (compartment instanceof Injury injury) {
            float healRate = stats.get(FunctionType.HEALING);
            injury.modifyHealth(-healRate);
        }
    }

    protected void sight() {
        if (stats.get(FunctionType.BRAIN_SIGHT) <= 10) {

        }

        if (stats.get(FunctionType.SIGHT) <= 10) {

        }
    }

    protected void movement() {
        float brainMotorAbility = stats.get(FunctionType.BRAIN_MOTOR_ABILITY);
        float movementAbility = stats.get(FunctionType.MOVEMENT);

        float movementCapability = (brainMotorAbility * movementAbility);
        double currentBaseValue = entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);

//        System.out.println(movementCapability);

        if (movementCapability != currentBaseValue) {
            Objects.requireNonNull(entity.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.JUMP_STRENGTH)).setBaseValue(movementCapability * 4.2);
        }
    }

    protected void manipulation() {
        float brainMotorAbility = stats.get(FunctionType.BRAIN_MOTOR_ABILITY);
        float manipulationAbility = stats.get(FunctionType.MANIPULATION);

        float movementCapability = (brainMotorAbility * manipulationAbility);
        double currentBaseValue = entity.getAttributeBaseValue(Attributes.ATTACK_SPEED);
        if (movementCapability != currentBaseValue) {
            Objects.requireNonNull(entity.getAttribute(Attributes.ATTACK_SPEED)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.BLOCK_BREAK_SPEED)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE)).setBaseValue(movementCapability);
        }
    }

    private void bloodPuddle() {
        BlockPos pos = entity.getOnPos().above();
        Level level = entity.level();
        BlockState existingState = level.getBlockState(pos);

        if (existingState.getBlock() instanceof FluidBlock) {
            if (level.getBlockEntity(pos) instanceof FluidBlockEntity fluid) {
                fluid.updateSubstance(new SubstanceStack(LIQUID_BLOOD.get(), stats.get(FunctionType.BLEED) / 2));
            }
        } else if (existingState.canBeReplaced()) {
            level.setBlock(pos, FLUID.get().defaultBlockState(), 3);
            if (level.getBlockEntity(pos) instanceof FluidBlockEntity fluid) {
                fluid.updateSubstance(new SubstanceStack(LIQUID_BLOOD.get(), stats.get(FunctionType.BLEED) / 2));
            }
        }
    }

    public float getTasteAbility() {
        return stats.get(FunctionType.BRAIN_TASTE) * stats.get(FunctionType.TASTE);
    }

    public float getHearingAbility() {
        return stats.get(FunctionType.BRAIN_HEARING) * stats.get(FunctionType.HEARING);
    }

    public float getLanguageComprehension() {
        return stats.get(FunctionType.BRAIN_LANGUAGE);
    }

    public float getTremor() {
        return stats.get(FunctionType.TREMOR);
    }

    public BloodType getBloodType() {
        return bloodType;
    }
}
