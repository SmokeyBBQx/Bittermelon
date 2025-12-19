package com.site21.bittermelon.common.systems.medical.medicalstats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.character.skills.Skill;
import com.site21.bittermelon.common.systems.medical.Anatomy;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.MedicalAttribute;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.level.Level;
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

    private final Holder<Anatomy> anatomy;
    private final int version;
    private final Map<UUID, CompartmentInstance> compartments;
    private final UUID mainCompartmentId;
    private final UUID characterId;
    private final EnumMap<MedicalAttribute, MedicalAttributeInstance> medicalAttributes;
    private final Map<Holder<Attribute>, Double> defaultEntityAttributes;
    private final PatchedDataComponentMap components;

    protected LivingEntity entity;
    protected Character character;

    public MedicalStats(Holder<Anatomy> anatomy, Integer version, @NotNull List<CompartmentInstance> compartments, UUID mainCompartmentId, UUID characterId, Map<MedicalAttribute, MedicalAttributeInstance> attributes, PatchedDataComponentMap components) {
        this.anatomy = anatomy;
        this.version = version;
        this.mainCompartmentId = mainCompartmentId;
        this.compartments = new ConcurrentHashMap<>();
        for (CompartmentInstance instance : compartments) {
            this.compartments.put(instance.getId(), instance);
        }
        this.characterId = characterId;
        this.medicalAttributes = new EnumMap<>(MedicalAttribute.class);
        medicalAttributes.putAll(attributes);
        this.defaultEntityAttributes = new HashMap<>();
        this.components = components;
    }

    public MedicalStats(Holder<Anatomy> anatomy, Integer version, List<CompartmentInstance> compartments, UUID mainCompartmentId, UUID characterId, Map<MedicalAttribute, MedicalAttributeInstance> attributes, DataComponentPatch components) {
        this(anatomy, version, compartments, mainCompartmentId, characterId, attributes, PatchedDataComponentMap.fromPatch(anatomy.value().components(), components));
    }

    @SuppressWarnings("unchecked")
    private void initializeEntity(Level level) {
        if (character == null) {
            character = CharacterManager.get(level).getCharacter(characterId);
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            this.entity = (LivingEntity) serverLevel.getEntities().get(character.getEntityUUID());
            if (entity == null) return;

            EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) entity.getType();

            for (AttributeInstance instance : DefaultAttributes.getSupplier(entityType).instances.values()) {
                defaultEntityAttributes.put(instance.getAttribute(), instance.getBaseValue());
            }
        }
    }

    public void tick(@NotNull Level level) {
        if (entity == null || defaultEntityAttributes.isEmpty()) {
            initializeEntity(level);
            return;
        }

        updateCompartments();
        updateEntityAttributes();
        handleMobEffects();
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

    private void updateEntityAttributes() {
        updateMovementAttributes();
        updateManipulationAttributes();
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

    private void updateMovementAttributes() {
        float capability = getMovement() * character.getSkill(Skill.AGILITY);

        if (capability < 1) {
            entity.addEffect(new MobEffectInstance(BAD_MOBILITY, MobEffectInstance.INFINITE_DURATION,
                    (int) Math.abs(1 - capability) * 10));
        } else if (entity.hasEffect(BAD_MOBILITY)) {
            entity.removeEffect(BAD_MOBILITY);
        }

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

        if (defaultEntityAttributes.get(attributeHolder) == null) return;
        double baseValue = defaultEntityAttributes.get(attributeHolder);

        attribute.setBaseValue(value * baseValue);
    }

    protected void handleMobEffects() {
        if (getConsciousness() <= 0) {
            entity.addEffect(new MobEffectInstance(UNCONSCIOUS, 2, 0, true, false, false));
        } else if (getConsciousness() < 1) {
            entity.addEffect(new MobEffectInstance(FAINTING, 2, (int) ((1 - getConsciousness()) * 100), true, false, false));
        }
    }

    public CompartmentInstance getCompartment(UUID uuid) {
        if (uuid == null) return null;

        CompartmentInstance compartment = compartments.get(uuid);

        // TODO: Desync between client and server causing compartments to be missing, why?
        if (compartment == null)
            System.out.println("Compartment not found: " + uuid + " for compartments " + compartments.keySet());

        if (compartment == null) {
            compartments.remove(uuid);
        }

        return compartment;
    }

    public Map<UUID, CompartmentInstance> getCompartments() {
        return compartments;
    }

    public void removeCompartment(@NotNull CompartmentInstance compartment) {
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

    public UUID getCharacterId() {
        return characterId;
    }

    public UUID getEntityID() {
        if (entity == null) return null;
        return entity.getUUID();
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public int getVersion() {
        return version;
    }

    public Holder<Anatomy> getAnatomy() {
        return anatomy;
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
        this.components.setAll(this.components);
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return components;
    }

    static {
        CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Anatomy.CODEC.fieldOf("anatomy").forGetter(MedicalStats::getAnatomy),
                        Codec.INT.fieldOf("version").orElse(1).forGetter(MedicalStats::getVersion),
                        Codec.list(CompartmentInstance.CODEC).fieldOf("compartments").forGetter(
                                stats -> new ArrayList<>(stats.compartments.values())
                        ),
                        UUIDUtil.CODEC.fieldOf("mainCompartmentID").forGetter(MedicalStats::getMainCompartmentId),
                        UUIDUtil.CODEC.fieldOf("characterID").forGetter(MedicalStats::getCharacterId),
                        Codec.unboundedMap(MedicalAttribute.CODEC, MedicalAttributeInstance.CODEC).fieldOf("attributes").forGetter(MedicalStats::getAttributes),
                        DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(stats -> stats.components.asPatch())
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
                UUIDUtil.STREAM_CODEC,
                MedicalStats::getCharacterId,
                ByteBufCodecs.map(
                        HashMap::new,
                        MedicalAttribute.STREAM_CODEC,
                        MedicalAttributeInstance.STREAM_CODEC
                ),
                MedicalStats::getAttributes,
                DataComponentPatch.STREAM_CODEC,
                stats -> stats.components.asPatch(),
                MedicalStats::new
        );
    }
}
