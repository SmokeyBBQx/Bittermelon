package com.site21.bittermelon.medical.medicalstats;

import com.site21.bittermelon.blocks.FluidBlock;
import com.site21.bittermelon.blocks.blockentities.FluidBlockEntity;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.entities.behavior.misc.FeelsPain;
import com.site21.bittermelon.medical.blood.BloodType;
import com.site21.bittermelon.medical.compartments.*;
import com.site21.bittermelon.medical.compartments.bodyparts.BodyPart;
import com.site21.bittermelon.medical.compartments.conditions.ForeignSubstance;
import com.site21.bittermelon.medical.compartments.conditions.Infection;
import com.site21.bittermelon.medical.compartments.organs.HeartRhythm;
import com.site21.bittermelon.miscellaneous.stumble.StumbleHandler;
import com.site21.bittermelon.networking.client.S2CSetForcedPose;
import com.site21.bittermelon.substance.SubstanceStack;
import com.site21.bittermelon.util.LocalMessageHelper;
import com.site21.bittermelon.util.ServerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
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
    private static final int GASP_INTERVAL = 200;
    private int stumbleTickCounter = 0;
    private static final int STUMBLE_INTERVAL = 100;
    private int painTickCounter = 0;
    private static final int BASE_PAIN_INTERVAL = 300;
    private final Map<Holder<Attribute>, Double> defaultAttributeValues = new HashMap<>();

    public MedicalStats(BloodType bloodType, List<Compartment> compartments, @NotNull Character character) {
        this.bloodType = bloodType;
        this.compartments = new CopyOnWriteArrayList<>(compartments);
        this.character = character;
        this.stats = new EnumMap<>(FunctionType.class);
        this.immunity = new ConcurrentHashMap<>();
        this.vitalSigns = new VitalSigns();

        for (FunctionType type : FunctionType.values()) {
            stats.put(type, 0f);
        }

        initializeEntity();
    }

    private void initializeEntity() {
        this.entity = ServerUtil.getLivingEntity(character.getEntityUUID());

        if (entity != null) {
            EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) entity.getType();
            AttributeMap attributeMap = new AttributeMap(DefaultAttributes.getSupplier(entityType));

            for (AttributeInstance instance : attributeMap.attributes.values()) {
                defaultAttributeValues.put(instance.getAttribute(), instance.getBaseValue());
            }
        }
    }

    public void update() {
        updateCompartments();
        updateEntityAttributes();
        updateCardiopulmonary();
        updateStats();
        handlePain();
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
        } else {
            initializeEntity();
        }
    }

    private void updateMovementAttributes() {
        float capability = getMovement();

        handleStumbling(capability);
//        System.out.println(capability);

        updateEntityAttribute(Attributes.MOVEMENT_SPEED, capability);
        updateEntityAttribute(Attributes.JUMP_STRENGTH, capability);
    }

    private void updateManipulationAttributes() {
        float capability = getManipulation();

        updateEntityAttribute(Attributes.ATTACK_SPEED, capability);
        updateEntityAttribute(Attributes.ATTACK_DAMAGE, capability);
        updateEntityAttribute(Attributes.BLOCK_BREAK_SPEED, capability);
        updateEntityAttribute(Attributes.BLOCK_INTERACTION_RANGE, capability);
        updateEntityAttribute(Attributes.ENTITY_INTERACTION_RANGE, capability);
    }

    private void updateEntityAttribute(Holder<Attribute> attributeHolder, double value) {
        AttributeInstance attribute = entity.getAttribute(attributeHolder);
        if (attribute == null) return;

        if (defaultAttributeValues.get(attributeHolder) == null) return;
        double baseValue = defaultAttributeValues.get(attributeHolder);
        attribute.setBaseValue(value * baseValue);
    }

    private void updateStats() {
        EnumMap<FunctionType, Float> statsCopy = new EnumMap<>(FunctionType.class);
        EnumMap<FunctionType, Integer> countMap = new EnumMap<>(FunctionType.class);

        for (FunctionType type : FunctionType.values()) {
            statsCopy.put(type, 0f);
            countMap.put(type, 0);
        }

        for (Compartment compartment : compartments) {
            for (FunctionType stat : FunctionType.values()) {
                float attribute = compartment.getAttribute(stat);
                if (attribute > 0) {
                    statsCopy.compute(stat, (k, currentValue) -> currentValue + attribute);
                    countMap.compute(stat, (k, count) -> count + 1);
                }
            }
        }

        for (FunctionType type : FunctionType.values()) {
            int count = countMap.get(type);
            if (count > 0) {
                float average = statsCopy.get(type) / count;
                statsCopy.put(type, average);
            }
        }

        stats.putAll(statsCopy);
    }

    private void updateCardiopulmonary() {
        vitalSigns.modifyBloodVolume(stats.get(FunctionType.CIRCULATION) / 100 - stats.get(FunctionType.BLEED) / 20);
        vitalSigns.modifyOxygenSaturation((stats.get(FunctionType.RESPIRATORY) / 100) * stats.get(FunctionType.BRAIN_VITALS) - 0.01f);

        handleGasping();

        if (vitalSigns.bloodVolume < 60 || vitalSigns.oxygenSaturation < 80) {
            for (Compartment compartment : compartments) {
                if (compartment instanceof BodyPart && !compartment.hasType(CompartmentType.MAJOR_BODY_PART)) {
                    compartment.modifyHealth(-0.001f);
                    if (compartment.getHealth() <= 0) {
                        compartment.modifyMaxHealth(-0.001f);
                    }
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
                    Component message = Component.literal(character.getName() + " gasps for air.")
                            .withColor(character.getEmoteColor());
                    LocalMessageHelper.sendLocalMessage(entity, 10, message);
                }
                gaspTickCounter = 0;
            }
        } else {
            gaspTickCounter = 0;
        }
    }

    private void handleStumbling(float capability) {
        if (capability < 0.5f) {
            stumbleTickCounter += entity.isSprinting() ? 2 : 1;

            if (stumbleTickCounter >= STUMBLE_INTERVAL) {
                float stumbleChance = entity.getRandom().nextFloat();

                if (entity.isSprinting()) stumbleChance -= 0.1f;

                if (stumbleChance > 0.5 + capability) {
                    StumbleHandler.stumble(entity);
                }
                stumbleTickCounter = 0;
            }
        }
    }

    private void updateConsciousness() {
        if (vitalSigns.consciousness < 0.1f) {
            if (entity.getPose() != Pose.SLEEPING) {
                entity.setPose(Pose.SLEEPING);
                PacketDistributor.sendToAllPlayers(new S2CSetForcedPose(entity.getUUID(), Pose.SLEEPING));
                LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(character.getName() + " passes out.").withColor(character.getEmoteColor()));
            }
        }

        vitalSigns.consciousness = stats.get(FunctionType.BRAIN_VITALS);
    }

    private void handlePain() {
        if (entity instanceof FeelsPain || entity instanceof Player) {
            if (getPain() <= 0) return;
            int painInterval = Math.max(60, (int)(BASE_PAIN_INTERVAL * Math.exp(-getPain() / 10)));
            painTickCounter++;

            if (painTickCounter >= painInterval) {
                painTickCounter = 0;

                if (getPain() > 8) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40 + (int) getPain(),
                            (int) getPain(), false, false));
                    entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40 + (int) getPain(),
                            (int) getPain(), false, false));
                    entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40 + (int) getPain(),
                            (int) getPain(), false, false));
                    entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40 + (int) getPain(),
                            (int) getPain(), false, false));
                }

                if (entity instanceof FeelsPain) {
                    Component message = Component.literal(character.getName() + " " + ((FeelsPain) entity)
                            .getPainMessage(getPain())).withColor(character.getEmoteColor());
                    LocalMessageHelper.sendLocalMessage(entity, 10, message);

                    entity.level().playSound(null, entity.getOnPos(), ((FeelsPain) entity)
                            .getPainSound(getPain()), SoundSource.AMBIENT);
                }
            }
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
            } else {
                child.initializeWithOwner(compartment.getOwner());
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

    public float getStat(FunctionType functionType) {
        return stats.getOrDefault(functionType, 0.0f);
    }

    public float getManipulation() {
        return stats.get(FunctionType.BRAIN_MOTOR_ABILITY) * stats.get(FunctionType.MANIPULATION) * getConsciousness();
    }

    public float getMovement() {
        return stats.get(FunctionType.BRAIN_MOTOR_ABILITY) * stats.get(FunctionType.MOVEMENT) * getConsciousness();
    }

    public float getSight() {
        return stats.get(FunctionType.SIGHT) * stats.get(FunctionType.BRAIN_SIGHT) * getConsciousness();
    }

    public float getBite() {
        return stats.get(FunctionType.BRAIN_MOTOR_ABILITY) * stats.get(FunctionType.BITE) * getConsciousness();
    }

    public float getPain() {
        return stats.get(FunctionType.NERVOUS) * stats.get(FunctionType.PAIN) * getConsciousness();

        // TODO: Better way to get pain?
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
