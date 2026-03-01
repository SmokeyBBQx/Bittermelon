package com.site21.bittermelon.common.systems.medical.medicalstats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.anatomy.Anatomy;
import com.site21.bittermelon.common.systems.medical.anatomy.AnatomyModel;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.component.MedicalTicker;
import com.site21.bittermelon.init.custom.Anatomies;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.MEDICAL_ATTRIBUTES;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.*;

public class MedicalStats implements DataComponentHolder, MutableDataComponentHolder {
    public static final Codec<MedicalStats> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, MedicalStats> STREAM_CODEC;
    public static final MedicalStats EMPTY = new MedicalStats(Anatomies.HUMAN, 0,
            Collections.emptyList(), UUID.fromString("00000000-0000-0000-0000-000000000000"),
            new EnumMap<>(MedicalAttribute.class), AnatomyModel.EMPTY,
            new PatchedDataComponentMap(DataComponentMap.EMPTY));

    private final Holder<Anatomy> anatomy;
    private final int version;
    private final Map<UUID, CompartmentInstance> compartments;
    private final UUID mainCompartmentId;
    private final EnumMap<MedicalAttribute, MedicalAttributeInstance> medicalAttributes;
    private final Map<Holder<Attribute>, Double> defaultEntityAttributes;
    private final AnatomyModel anatomyModel;
    private final PatchedDataComponentMap components;

    public MedicalStats(Holder<Anatomy> anatomy, int version, @NotNull List<CompartmentInstance> compartments,
                        UUID mainCompartmentId, Map<MedicalAttribute, MedicalAttributeInstance> attributes,
                        AnatomyModel anatomyModel, PatchedDataComponentMap components) {
        this.anatomy = anatomy;
        this.version = version;
        this.mainCompartmentId = mainCompartmentId;
        this.compartments = new ConcurrentHashMap<>();
        for (CompartmentInstance instance : compartments) {
            this.compartments.put(instance.getId(), instance);
        }
        this.medicalAttributes = new EnumMap<>(MedicalAttribute.class);
        medicalAttributes.putAll(attributes);
        this.defaultEntityAttributes = new HashMap<>();
        this.anatomyModel = anatomyModel;
        this.components = components;
    }

    public MedicalStats(Holder<Anatomy> anatomy, int version, List<CompartmentInstance> compartments,
                        UUID mainCompartmentId, Map<MedicalAttribute, MedicalAttributeInstance> attributes,
                        AnatomyModel anatomyModel, DataComponentPatch components) {
        this(anatomy, version, compartments, mainCompartmentId, attributes, anatomyModel,
                PatchedDataComponentMap.fromPatch(anatomy.value().components(), components));
    }

    @SuppressWarnings("unchecked")
    private void initializeEntity(LivingEntity entity) {
        EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) entity.getType();

        for (AttributeInstance instance : DefaultAttributes.getSupplier(entityType).instances.values()) {
            defaultEntityAttributes.put(instance.getAttribute(), instance.getBaseValue());
        }
    }

    public void tick(@NotNull LivingEntity entity) {
        anatomy.value().tick(entity, this);

        if (defaultEntityAttributes.isEmpty()) {
            initializeEntity(entity);
            return;
        }

        updateCompartments();
        updateEntityAttributes(entity);
        handleMobEffects(entity);

        for (TypedDataComponent<?> componentType : getComponents()) {
            if (componentType.value() instanceof MedicalTicker ticker) {
                ticker.tick(this, entity.level());
            }
        }
    }

    private void updateCompartments() {
        for (CompartmentInstance compartment : compartments.values()) {
            compartment.tick(this);

            EnumMap<MedicalAttribute, Float> attributes = compartment.get(MEDICAL_ATTRIBUTES);

            if (attributes != null) {
                for (MedicalAttribute attribute : attributes.keySet()) {
                    MedicalAttributeInstance instance = medicalAttributes.computeIfAbsent(attribute,
                            (k) -> new MedicalAttributeInstance());
                    instance.updateModifier(compartment.getId(), attributes.get(attribute));
                }
            }
        }
    }

    private void updateEntityAttributes(LivingEntity entity) {
        updateMovementAttributes(entity);
        updateManipulationAttributes(entity);
    }

//    private void tickDrugs() {
//        Iterator<DrugInstance> iterator = activeDrugs.iterator();
//        while (iterator.hasNext()) {
//            DrugInstance drug = iterator.next();
//            drug.tickInstance(this);
//            if (drug.getAmount() <= 0) {
//                drug.remove(this);
//                iterator.remove();
//            }
//        }
//    }

    private void updateMovementAttributes(LivingEntity entity) {
//        float capability = getMovement() * character.getSkill(Skill.AGILITY);
        float capability = getMovement();

        if (capability < 1) {
            entity.addEffect(new MobEffectInstance(BAD_MOBILITY, MobEffectInstance.INFINITE_DURATION,
                    (int) Math.abs(1 - capability) * 10));
        } else if (entity.hasEffect(BAD_MOBILITY)) {
            entity.removeEffect(BAD_MOBILITY);
        }

        updateEntityAttribute(entity, Attributes.MOVEMENT_SPEED, capability);
        updateEntityAttribute(entity, Attributes.JUMP_STRENGTH, capability);
    }

    private void updateManipulationAttributes(LivingEntity entity) {
        float capability = getManipulation();

        updateEntityAttribute(entity, Attributes.ATTACK_SPEED, capability);
        updateEntityAttribute(entity, Attributes.ATTACK_DAMAGE, capability);
        updateEntityAttribute(entity, Attributes.BLOCK_BREAK_SPEED, capability);
        updateEntityAttribute(entity, Attributes.BLOCK_INTERACTION_RANGE, capability);
        updateEntityAttribute(entity, Attributes.ENTITY_INTERACTION_RANGE, capability);
    }

    private void updateEntityAttribute(LivingEntity entity, Holder<Attribute> attributeHolder, double value) {
        AttributeInstance attribute = entity.getAttribute(attributeHolder);
        if (attribute == null) return;

        if (defaultEntityAttributes.get(attributeHolder) == null) return;
        double baseValue = defaultEntityAttributes.get(attributeHolder);

        attribute.setBaseValue(value * baseValue);
    }

    protected void handleMobEffects(LivingEntity entity) {
        if (getConsciousness() <= 0) {
            entity.addEffect(new MobEffectInstance(UNCONSCIOUS, 2, 0, true, false, false));
        } else if (getConsciousness() < 1) {
            entity.addEffect(new MobEffectInstance(FAINTING, 2, (int) ((1 - getConsciousness()) * 100), true, false, false));
        }
    }

    public CompartmentInstance getCompartment(UUID uuid) {
        if (uuid == null) return null;
        return compartments.get(uuid);
    }

    public Map<UUID, CompartmentInstance> getCompartments() {
        return compartments;
    }

    public void removeCompartment(@NotNull CompartmentInstance compartment) {
        compartments.remove(compartment.getId());
    }

    public void removeCompartment(UUID compartmentId) {
        compartments.remove(compartmentId);
    }

    public void addCompartment(CompartmentInstance compartment) {
        compartments.put(compartment.getId(), compartment);
    }

    public void removeModifiers(UUID uuid, @NotNull Collection<MedicalAttribute> attributes) {
        for (MedicalAttribute attribute : attributes) {
            medicalAttributes.get(attribute).removeModifier(uuid);
        }
    }

    public void updateModifiers(UUID uuid, @NotNull Map<MedicalAttribute, Float> attributes) {
        for (Map.Entry<MedicalAttribute, Float> entry : attributes.entrySet()) {
            medicalAttributes.get(entry.getKey()).updateModifier(uuid, entry.getValue());
        }
    }

    public void updateModifier(UUID uuid, MedicalAttribute attribute, float value) {
        medicalAttributes.get(attribute).updateModifier(uuid, value);
    }

    public EnumMap<MedicalAttribute, MedicalAttributeInstance> getAttributes() {
        return medicalAttributes;
    }

    public float getAttribute(MedicalAttribute attribute) {
        return medicalAttributes.computeIfAbsent(attribute, (k) -> new MedicalAttributeInstance()).getValue();
    }

    public float getTasteAbility() {
        return getAttribute(MedicalAttribute.BRAIN_TASTE) * getAttribute(MedicalAttribute.TASTE);
    }

    public float getHearingAbility() {
        return getAttribute(MedicalAttribute.BRAIN_HEARING) * getAttribute(MedicalAttribute.HEARING);
    }

    public float getManipulation() {
        return getAttribute(MedicalAttribute.BRAIN_MOTOR_ABILITY) * getAttribute(MedicalAttribute.MANIPULATION) * getConsciousness();
    }

    public float getMovement() {
        return getAttribute(MedicalAttribute.BRAIN_MOTOR_ABILITY) * getAttribute(MedicalAttribute.MOVEMENT) * getConsciousness();
    }

    public float getSight() {
        return getAttribute(MedicalAttribute.BRAIN_SIGHT) * getAttribute(MedicalAttribute.SIGHT) * getConsciousness();
    }

    public float getBite() {
        return getAttribute(MedicalAttribute.BRAIN_MOTOR_ABILITY) * getAttribute(MedicalAttribute.BITE) * getConsciousness();
    }

    public float getElimination() {
        return getAttribute(MedicalAttribute.ELIMINATION) * getCirculation();
    }

    public float getAbsorption() {
        return getAttribute(MedicalAttribute.DIGESTION) * getCirculation();
    }

    public float getCirculation() {
        return getAttribute(MedicalAttribute.CIRCULATION);
    }

    public float getConsciousness() {
        return getAttribute(MedicalAttribute.BRAIN_CONSCIOUSNESS);
    }

    public UUID getMainCompartmentId() {
        return mainCompartmentId;
    }

    public CompartmentInstance getMainCompartment() {
        return getCompartment(mainCompartmentId);
    }

    public int getVersion() {
        return version;
    }

    public Holder<Anatomy> getAnatomy() {
        return anatomy;
    }

    public AnatomyModel getAnatomyModel() {
        return anatomyModel;
    }

    @Override
    public <T> @Nullable T set(@NotNull DataComponentType<T> component, @Nullable T value) {
        return components.set(component, value);
    }

    @Override
    public <T> @Nullable T remove(@NotNull DataComponentType<? extends T> component) {
        return components.remove(component);
    }

    @Override
    public void applyComponents(@NotNull DataComponentPatch patch) {
        components.applyPatch(patch);
    }

    @Override
    public void applyComponents(@NotNull DataComponentMap components) {
        this.components.setAll(components);
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return components;
    }

    static {
        CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Anatomy.CODEC
                                .fieldOf("anatomy")
                                .forGetter(MedicalStats::getAnatomy),
                        Codec.INT
                                .fieldOf("version")
                                .orElse(1)
                                .forGetter(MedicalStats::getVersion),
                        Codec.list(CompartmentInstance.CODEC)
                                .fieldOf("compartments")
                                .forGetter(stats -> new ArrayList<>(stats.compartments.values())),
                        UUIDUtil.CODEC
                                .fieldOf("mainCompartmentID")
                                .forGetter(MedicalStats::getMainCompartmentId),
                        Codec.unboundedMap(MedicalAttribute.CODEC, MedicalAttributeInstance.CODEC)
                                .fieldOf("attributes")
                                .forGetter(MedicalStats::getAttributes),
                        AnatomyModel.CODEC
                                .fieldOf("anatomyModel")
                                .forGetter(MedicalStats::getAnatomyModel),
                        DataComponentPatch.CODEC
                                .optionalFieldOf("components", DataComponentPatch.EMPTY)
                                .forGetter(stats -> stats.components.asPatch())
                ).apply(instance, MedicalStats::new)
        );

        STREAM_CODEC = StreamCodec.composite(
                Anatomy.STREAM_CODEC,
                MedicalStats::getAnatomy,
                ByteBufCodecs.INT,
                MedicalStats::getVersion,
                CompartmentInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
                stats -> new ArrayList<>(stats.compartments.values()),
                UUIDUtil.STREAM_CODEC,
                MedicalStats::getMainCompartmentId,
                ByteBufCodecs.map(
                        HashMap::new,
                        MedicalAttribute.STREAM_CODEC,
                        MedicalAttributeInstance.STREAM_CODEC
                ),
                MedicalStats::getAttributes,
                AnatomyModel.STREAM_CODEC,
                MedicalStats::getAnatomyModel,
                DataComponentPatch.STREAM_CODEC,
                stats -> stats.components.asPatch(),
                MedicalStats::new
        );
    }
}
