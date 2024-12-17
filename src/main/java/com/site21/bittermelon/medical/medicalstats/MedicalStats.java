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
import com.site21.bittermelon.substance.SubstanceStack;
import com.site21.bittermelon.util.LocalMessageHelper;
import com.site21.bittermelon.util.ServerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.site21.bittermelon.init.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.Substances.LIQUID_BLOOD;

public class MedicalStats {
    private final CopyOnWriteArrayList<Compartment> compartments;
    private final Map<FunctionType, Float> stats;
    private final Map<UUID, Float> immunity;
    private final BloodType bloodType;
    private final Character character;
    private LivingEntity entity;
    private final VitalSigns vitalSigns;

    private int gaspTickCounter = 0;
    private static final int GASP_INTERVAL = 80;

    public MedicalStats(BloodType bloodType, List<Compartment> compartments, @NotNull Character character) {
        this.bloodType = bloodType;
        this.compartments = new CopyOnWriteArrayList<>(compartments);
        this.character = character;

        this.entity = ServerUtil.getLivingEntity(character.getEntityUUID());
        this.stats = new EnumMap<>(FunctionType.class);
        this.immunity = new ConcurrentHashMap<>();
        this.vitalSigns = new VitalSigns();

        for (FunctionType type : FunctionType.values()) {
            stats.put(type, 0f);
        }
    }

    public void update() {
        updateCompartments();
        updateEntityAttributes();
        updateCardiopulmonary();
        updateStats();
    }

    private void updateCompartments() {
        for (Compartment compartment : compartments) {
            compartment.update(this);
            if (compartment instanceof Condition) {
                processCondition(compartment);
            }
        }
    }

    private void processCondition(Compartment compartment) {
        if (compartment instanceof Infection infection) {
            handleInfection(infection);
        } else if (compartment instanceof ForeignSubstance substance) {
            handleForeignSubstance(substance);
        } else if (compartment instanceof Injury injury) {
            handleInjury(injury);
        }
    }

    private void handleInfection(@NotNull Infection infection) {
        float immunityRate = stats.get(FunctionType.IMMUNITY);
        float memory = immunity.getOrDefault(infection.getOrganism(), 0f);

        infection.modifyHealth(-immunityRate * memory / 10);
        immunity.merge(infection.getOrganism(),
                immunityRate / 10,
                Float::sum);

        // TODO: Add inflammation
    }

    private void handleForeignSubstance(@NotNull ForeignSubstance substance) {
        float eliminationRate = stats.get(FunctionType.ELIMINATION);
        substance.modifyHealth(-eliminationRate / 10);

        // TODO: Separate metabolism and elimination?
    }

    private void handleInjury(@NotNull Injury injury) {
        float healRate = stats.get(FunctionType.HEALING);
        injury.modifyHealth(-healRate);
    }


    private void updateEntityAttributes() {
        if (entity != null) {
            updateMovementAttributes();
            updateManipulationAttributes();
            updateConsciousness();
        }
    }

    private void updateMovementAttributes() {
        float capability = stats.get(FunctionType.BRAIN_MOTOR_ABILITY) * stats.get(FunctionType.MOVEMENT);
        updateEntityAttribute(Attributes.MOVEMENT_SPEED, capability);
        updateEntityAttribute(Attributes.JUMP_STRENGTH, capability * 4.2);
    }

    private void updateManipulationAttributes() {
        float capability = stats.get(FunctionType.BRAIN_MOTOR_ABILITY) * stats.get(FunctionType.MANIPULATION);
        updateEntityAttribute(Attributes.ATTACK_SPEED, capability);
        updateEntityAttribute(Attributes.ATTACK_DAMAGE, capability);
        updateEntityAttribute(Attributes.BLOCK_BREAK_SPEED, capability);
        updateEntityAttribute(Attributes.BLOCK_INTERACTION_RANGE, capability);
        updateEntityAttribute(Attributes.ENTITY_INTERACTION_RANGE, capability);
    }

    private void updateEntityAttribute(Holder<Attribute> attribute, double value) {
        Objects.requireNonNull(entity.getAttribute(attribute))
                .setBaseValue(value);
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
        vitalSigns.modifyBloodVolume(0.01f - stats.get(FunctionType.BLEED) / 100);
        vitalSigns.modifyOxygenSaturation(stats.get(FunctionType.RESPIRATORY) * stats.get(FunctionType.BRAIN_VITALS) - 0.01f);

        handleGasping();

        if (vitalSigns.bloodVolume < 60 || vitalSigns.oxygenSaturation < 80) {
            for (Compartment compartment : compartments) {
                if (compartment instanceof BodyPart) {
                    compartment.modifyHealth(-0.001f);
                }
            }
        }

        // TODO: Random heart state depending on heart health
    }

    private void handleGasping() {
        if (vitalSigns.oxygenSaturation < 80) {
            gaspTickCounter++;
            if (gaspTickCounter >= GASP_INTERVAL) {
                if (entity != null && stats.get(FunctionType.BRAIN_VITALS) > 0.1f) {
                    Component message = Component.literal(character.getName() + " gasps for air.");
                    LocalMessageHelper.sendLocalMessage(entity, 10, message);
                }
                gaspTickCounter = 0;
            }
        } else {
            gaspTickCounter = 0;
        }
    }

    private void updateConsciousness() {
        vitalSigns.consciousness = stats.get(FunctionType.BRAIN_VITALS);

        if (vitalSigns.consciousness < 0.1f) {
//            LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal("Entity passes out"));
        }
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

    protected void sight() {
        if (stats.get(FunctionType.BRAIN_SIGHT) <= 10) {

        }

        if (stats.get(FunctionType.SIGHT) <= 10) {

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

    public float getConsciousness() {
        return vitalSigns.consciousness;
    }

    public float getOxygenSaturation() {
        return vitalSigns.oxygenSaturation;
    }

    public float getBloodVolume() {
        return vitalSigns.bloodVolume;
    }

    public float getBPM() {
        return vitalSigns.bpm;
    }

    public float getBloodPressureSystolic() {
        return vitalSigns.bloodPressureSystolic;
    }

    public float getBloodPressureDiastolic() {
        return vitalSigns.bloodPressureDiastolic;
    }

    public float getTemperature() {
        return vitalSigns.temperature;
    }

    public HeartRhythm getHeartRhythm() {
        return vitalSigns.heartRhythm;
    }

    private static class VitalSigns {
        private float consciousness = 1;
        private float oxygenSaturation = 100;
        private float bloodVolume = 100;
        private float bpm;
        private float bloodPressureSystolic;
        private float bloodPressureDiastolic;
        private float temperature;
        private HeartRhythm heartRhythm;

        public void modifyBloodVolume(float amount) {
            bloodVolume = Math.max(0, Math.min(amount + bloodVolume, 100));
        }

        public void modifyOxygenSaturation(float amount) {
            oxygenSaturation = Math.max(0, Math.min(amount + oxygenSaturation, 100));
        }
    }
}
