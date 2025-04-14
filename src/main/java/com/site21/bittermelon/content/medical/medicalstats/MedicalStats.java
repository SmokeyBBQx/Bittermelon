package com.site21.bittermelon.content.medical.medicalstats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.client.visualeffects.ScreenshakeHandler;
import com.site21.bittermelon.content.atmosphere.AtmosHandler;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlock;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlockEntity;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.entities.ai.behavior.misc.FeelsPain;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.compartments.*;
import com.site21.bittermelon.content.medical.compartments.organs.HeartRhythm;
import com.site21.bittermelon.content.medical.simulations.Simulation;
import com.site21.bittermelon.content.miscellaneous.stumble.StumbleHandler;
import com.site21.bittermelon.content.substance.SubstanceStack;
import com.site21.bittermelon.networking.client.SetForcedPose;
import com.site21.bittermelon.util.LocalMessageHelper;
import com.site21.bittermelon.util.ServerUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.site21.bittermelon.init.custom.Substances.LIQUID_BLOOD;
import static com.site21.bittermelon.init.custom.Substances.LIQUID_WATER;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.FLUID;
import static net.minecraft.world.level.block.Block.UPDATE_ALL_IMMEDIATE;

public class MedicalStats {
    public static final Codec<MedicalStats> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.list(CompartmentInstance.CODEC).fieldOf("compartments").forGetter(
                            stats -> new ArrayList<>(stats.compartments.values())
                    ),
                    BloodType.CODEC.fieldOf("bloodType").forGetter(MedicalStats::getBloodType),
                    UUIDUtil.CODEC.fieldOf("characterID").forGetter(MedicalStats::getCharacterID),
                    VitalSigns.CODEC.fieldOf("vitalSigns").forGetter(MedicalStats::getVitalSigns)
            ).apply(instance, MedicalStats::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MedicalStats> STREAM_CODEC = StreamCodec.composite(
            CompartmentInstance.STREAM_CODEC.apply(
                    ByteBufCodecs.collection(ArrayList::new)
            ),
            MedicalStats::getCompartmentsCollection,
            BloodType.STREAM_CODEC,
            MedicalStats::getBloodType,
            UUIDUtil.STREAM_CODEC,
            MedicalStats::getCharacterID,
            VitalSigns.STREAM_CODEC,
            MedicalStats::getVitalSigns,
            MedicalStats::new
    );

    private static final int GASP_INTERVAL = 200;
    private static final int STUMBLE_INTERVAL = 100;
    private static final int BASE_PAIN_INTERVAL = 300;
    private static final int BASE_BLEED_INTERVAL = 100;
    private static final float MIN_CONSCIOUSNESS_THRESHOLD = 0.1f;
    private static final float LOW_OXYGEN_THRESHOLD = 80f;
    private static final float LOW_BLOOD_VOLUME_THRESHOLD = 60f;

    private final HashMap<UUID, CompartmentInstance> compartments;
    private final Map<FunctionType, Float> stats;
    private final Map<UUID, Float> immunity;
    private final List<SubstanceStack> substances;
    private final List<Simulation> simulations;
    private final BloodType bloodType;
    private final UUID characterID;
    private Character character;
    private final MedicalStats.VitalSigns vitalSigns;
    private final Map<Holder<Attribute>, Double> defaultAttributeValues;

    private LivingEntity entity;
    private float heartLifeSupport;
    private int gaspTickCounter;
    private int stumbleTickCounter;
    private int painTickCounter;
    private int bleedTickCounter;

    public MedicalStats(@NotNull Collection<CompartmentInstance> compartments, BloodType bloodType, UUID characterID, VitalSigns vitalSigns) {
        this.bloodType = bloodType;
        this.compartments = new HashMap<>();
        for (CompartmentInstance instance : compartments) {
            this.compartments.put(instance.getUUID(), instance);
        }
        this.characterID = characterID;
        this.vitalSigns = vitalSigns;
        this.character = CharacterManager.get(ServerUtil.getServer()).getCharacter(characterID);
        this.stats = new EnumMap<>(FunctionType.class);
        this.immunity = new ConcurrentHashMap<>();
        this.substances = new ArrayList<>();
        this.simulations = new ArrayList<>();
        this.defaultAttributeValues = new HashMap<>();

        initializeStats();
        initializeEntity();
    }

    public MedicalStats(@NotNull List<CompartmentInstance> compartments, BloodType bloodType, UUID characterID) {
        this(compartments, bloodType, characterID, new VitalSigns());
    }

    private void initializeStats() {
        for (FunctionType type : FunctionType.values()) {
            stats.put(type, 0f);
        }
    }

    @SuppressWarnings("unchecked")
    private void initializeEntity() {
        if (character == null) {
            character = CharacterManager.get(ServerUtil.getServer()).getCharacter(characterID);
            return;
        }

        this.entity = ServerUtil.getLivingEntity(character.getEntityUUID());
        if (entity == null) return;

        EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) entity.getType();
        AttributeMap attributeMap = new AttributeMap(DefaultAttributes.getSupplier(entityType));

        for (AttributeInstance instance : attributeMap.attributes.values()) {
            defaultAttributeValues.put(instance.getAttribute(), instance.getBaseValue());
        }
    }

    public void update() {
        if (entity == null) {
            initializeEntity();
            return;
        }

        updateCompartments();
        updateEntityAttributes();
        updateCardiopulmonary();
//        updateSubstances();
//        updateSimulations();
        handlePain();
        handleTremor();
        bloodPuddle();
    }

    private void updateCompartments() {
        EnumMap<FunctionType, Float> statsCopy = new EnumMap<>(FunctionType.class);
        EnumMap<FunctionType, Integer> countMap = new EnumMap<>(FunctionType.class);

        for (FunctionType type : FunctionType.values()) {
            statsCopy.put(type, 0f);
            countMap.put(type, 0);
        }

        for (CompartmentInstance compartment : compartments.values()) {
            compartment.tick(this);

//            if (compartment instanceof Condition) {
//                processCondition(compartment);
//            }

            for (FunctionType stat : FunctionType.values()) {
                float attribute = compartment.getAttribute(stat);
                if (attribute != 0) {
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

    private void updateEntityAttributes() {
        updateMovementAttributes();
        updateManipulationAttributes();
        updateConsciousness();
    }

    private void updateMovementAttributes() {
        float capability = getMovement();

        handleStumbling(capability);

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

    private void updateCardiopulmonary() {
        vitalSigns.modifyBloodVolume(getCirculation() / 100 - stats.get(FunctionType.BLEED) / 20);
        vitalSigns.modifyOxygenSaturation((stats.get(FunctionType.RESPIRATORY) / 100) * stats.get(FunctionType.BRAIN_VITALS) * getAirQuality() - 0.01f);

//        if (!entity.level().isClientSide) {
//            AtmosHandler.releaseGas(entity.level(), entity.getOnPos(), new SubstanceStack(LIQUID_WATER.get(), 0.001f));
//        }
        handleGasping();

        if (vitalSigns.bloodVolume < 60 || vitalSigns.oxygenSaturation < 80) {
            for (CompartmentInstance compartment : compartments.values()) {
                    compartment.modifyHealth(-0.001f);
                    if (compartment.getHealth() <= 0) {
                        compartment.modifyMaxHealth(-0.001f);
                    }
            }
        }

        // TODO: Random heart state depending on heart health
    }

    private float getAirQuality() {
        return 1;

//        if (!entity.level().isClientSide) {
//            AtmosInstance atmos = AtmosHandler.getAtmosInstanceAt(entity.level(), entity.getOnPos());
//            if (atmos != null) {
//                List<SubstanceStack> gasses = atmos.getGases();
//                for (SubstanceStack stack : gasses) {
//                    if (Objects.equals(stack.getSubstance().getName(), "gaseous_oxygen")) {
//                        float amount = stack.getAmount();
//                        return amount / 20;
//                    }
//                }
//            }
//        }
//        return 0;
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
                PacketDistributor.sendToAllPlayers(new SetForcedPose(entity.getUUID(), Pose.SLEEPING));
//                LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(character.getName() + " passes out.").withColor(character.getEmoteColor()));
            }
        }

        vitalSigns.consciousness = stats.get(FunctionType.BRAIN_VITALS) * stats.get(FunctionType.CIRCULATION);
    }

    private void handlePain() {
        if (entity instanceof FeelsPain || entity instanceof Player) {
            if (getPain() <= 0) return;
            int painInterval = Math.max(60, (int) (BASE_PAIN_INTERVAL * Math.exp(-getPain() / 10)));
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

    private void handleTremor() {
        if (entity instanceof Player player) {
            if (!entity.level().isClientSide()) return;
            ScreenshakeHandler.startScreenshake(player, 80, Math.min(0.8f, getTremor() / 10));
        }
    }

    private void bloodPuddle() {
        float bleedValue = stats.get(FunctionType.BLEED);
        if (Float.isNaN(bleedValue) || bleedValue <= 0) {
            stats.put(FunctionType.BLEED, 0f);
            return;
        }

        int bleedInterval = Math.max(60, (int) (BASE_BLEED_INTERVAL * Math.exp(-bleedValue / 5)));
        bleedTickCounter++;

        if (bleedTickCounter >= bleedInterval) {
            bleedTickCounter = 0;

            BlockPos pos = entity.getOnPos().above();
            Level level = entity.level();
            if (level.isClientSide) return;
            BlockState existingState = level.getBlockState(pos);
            if (level.getBlockState(pos.below()).getBlock() instanceof CarpetBlock) return;

            float bloodAmount = bleedValue;
            if (Float.isNaN(bloodAmount)) {
                return;
            }
            SubstanceStack stack = new SubstanceStack(LIQUID_BLOOD.get(), 0);
            stack.setVolume(bloodAmount);

            if (!(existingState.getBlock() instanceof FluidBlock) && existingState.canBeReplaced()) {
                level.setBlock(pos, FLUID.get().defaultBlockState(), UPDATE_ALL_IMMEDIATE);
            }

            if (level.getBlockEntity(pos) == null) System.out.println("BlockEntity is null");

            // TODO: WHY DOESN'T IT FIND THE FLUID BLOCK ENTITY HALF THE TIME

            if (level.getBlockEntity(pos) instanceof FluidBlockEntity fluid) {
                fluid.updateSubstance(stack);
            } else {
                System.out.println("Fluid not found at " + pos);
            }
        }
    }

    public CompartmentInstance getCompartment(UUID uuid) {
        return compartments.get(uuid);
    }

    public HashMap<UUID, CompartmentInstance> getCompartments() {
        return compartments;
    }

    public Collection<CompartmentInstance> getCompartmentsCollection() {
        return compartments.values();
    }

    public void extractCompartment(@NotNull CompartmentInstance compartment) {
        compartment.getCompartment().onExtract(this, compartment);
        removeCompartment(compartment);
    }

    public void removeCompartment(@NotNull CompartmentInstance compartment) {
        getCompartment(compartment.getParentID()).getChildren().remove(compartment.getUUID());
        compartments.remove(compartment.getUUID());

        List<UUID> childrenToRemove = new ArrayList<>(compartment.getChildren());
        for (UUID child : childrenToRemove) {
            if (getCompartment(child).hasTag(CompartmentTag.MAJOR_BODY_PART)) {
                removeCompartment(getCompartment(child));
            } else if (getCompartment(child).hasTag(CompartmentTag.CONDITION)) {
                removeCompartment(getCompartment(child));
            } else {
                getCompartment(child).initializeWithParent(getCompartment(compartment.getParentID()));
            }
        }


        // TODO: Severed vessels and such for connecting compartments
    }

    public void addCompartment(CompartmentInstance compartment) {
        compartments.put(compartment.getUUID(), compartment);
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

    public int getBPM() {
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

    public float getCirculation() {
        return stats.get(FunctionType.CIRCULATION) + heartLifeSupport;
    }

    public void setHeartLifeSupport(float value) {
        heartLifeSupport = value;
    }

    public UUID getCharacterID() {
        return characterID;
    }

    public VitalSigns getVitalSigns() {
        return vitalSigns;
    }

    public static class VitalSigns {
        public static final Codec<VitalSigns> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.FLOAT.fieldOf("consciousness").forGetter(vs -> vs.consciousness),
                        Codec.FLOAT.fieldOf("oxygenSaturation").forGetter(vs -> vs.oxygenSaturation),
                        Codec.FLOAT.fieldOf("bloodVolume").forGetter(vs -> vs.bloodVolume),
                        Codec.FLOAT.fieldOf("plateletNumber").forGetter(vs -> vs.plateletNumber),
                        Codec.INT.fieldOf("bpm").forGetter(vs -> vs.bpm),
                        Codec.FLOAT.fieldOf("bloodPressureSystolic").forGetter(vs -> vs.bloodPressureSystolic),
                        Codec.FLOAT.fieldOf("bloodPressureDiastolic").forGetter(vs -> vs.bloodPressureDiastolic),
                        Codec.FLOAT.fieldOf("temperature").forGetter(vs -> vs.temperature),
                        HeartRhythm.CODEC.fieldOf("heartRhythm").forGetter(vs -> vs.heartRhythm)
                ).apply(instance, VitalSigns::new)
        );

        public static final StreamCodec<ByteBuf, VitalSigns> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public void encode(@NotNull ByteBuf buf, @NotNull VitalSigns value) {
                buf.writeFloat(value.getConsciousness());
                buf.writeFloat(value.getOxygenSaturation());
                buf.writeFloat(value.getBloodVolume());
                buf.writeFloat(value.getPlateletNumber());
                buf.writeInt(value.getBpm());
                buf.writeFloat(value.getBloodPressureSystolic());
                buf.writeFloat(value.getBloodPressureDiastolic());
                buf.writeFloat(value.getTemperature());
                HeartRhythm.STREAM_CODEC.encode(buf, value.getHeartRhythm());
            }

            @Override
            public @NotNull VitalSigns decode(@NotNull ByteBuf buf) {
                float consciousness = buf.readFloat();
                float oxygenSaturation = buf.readFloat();
                float bloodVolume = buf.readFloat();
                float plateletNumber = buf.readFloat();
                int bpm = buf.readInt();
                float bloodPressureSystolic = buf.readFloat();
                float bloodPressureDiastolic = buf.readFloat();
                float temperature = buf.readFloat();
                HeartRhythm heartRhythm = HeartRhythm.STREAM_CODEC.decode(buf);

                return new VitalSigns(
                        consciousness,
                        oxygenSaturation,
                        bloodVolume,
                        plateletNumber,
                        bpm,
                        bloodPressureSystolic,
                        bloodPressureDiastolic,
                        temperature,
                        heartRhythm
                );
            }
        };

        private float consciousness;
        private float oxygenSaturation;
        private float bloodVolume;
        private float plateletNumber;
        private int bpm;
        private float bloodPressureSystolic;
        private float bloodPressureDiastolic;
        private float temperature;
        private HeartRhythm heartRhythm;

        public VitalSigns(float consciousness, float oxygenSaturation, float bloodVolume,
                          float plateletNumber, int bpm, float bloodPressureSystolic,
                          float bloodPressureDiastolic, float temperature, HeartRhythm heartRhythm) {
            this.consciousness = consciousness;
            this.oxygenSaturation = oxygenSaturation;
            this.bloodVolume = bloodVolume;
            this.plateletNumber = plateletNumber;
            this.bpm = bpm;
            this.bloodPressureSystolic = bloodPressureSystolic;
            this.bloodPressureDiastolic = bloodPressureDiastolic;
            this.temperature = temperature;
            this.heartRhythm = heartRhythm;
        }

        public VitalSigns() {
            this.consciousness = 1;
            this.oxygenSaturation = 100;
            this.bloodVolume = 100;
            this.plateletNumber = 0.0005f;
            this.bpm = 0;
            this.bloodPressureSystolic = 0;
            this.bloodPressureDiastolic = 0;
            this.temperature = 37.0f;
            this.heartRhythm = HeartRhythm.SINUS_RHYTHM;
        }

        public void modifyBloodVolume(float amount) {
            bloodVolume = Math.max(0, Math.min(amount + bloodVolume, 100));
        }

        public void modifyOxygenSaturation(float amount) {
            oxygenSaturation = Math.max(0, Math.min(amount + oxygenSaturation, 100));
        }

        public float getConsciousness() {
            return consciousness;
        }

        public float getOxygenSaturation() {
            return oxygenSaturation;
        }

        public float getBloodVolume() {
            return bloodVolume;
        }

        public float getPlateletNumber() {
            return plateletNumber;
        }

        public int getBpm() {
            return bpm;
        }

        public float getBloodPressureSystolic() {
            return bloodPressureSystolic;
        }

        public float getBloodPressureDiastolic() {
            return bloodPressureDiastolic;
        }

        public float getTemperature() {
            return temperature;
        }

        public HeartRhythm getHeartRhythm() {
            return heartRhythm;
        }
    }
}
