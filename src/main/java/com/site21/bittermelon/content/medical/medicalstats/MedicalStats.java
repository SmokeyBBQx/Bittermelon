package com.site21.bittermelon.content.medical.medicalstats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.character.skills.Skill;
import com.site21.bittermelon.content.medical.client.screen.networking.UpdateHealthScreen;
import com.site21.bittermelon.content.medical.compartments.*;
import com.site21.bittermelon.content.medical.drugs.DrugInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.*;

public class MedicalStats {
    public static final Codec<MedicalStats> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.list(CompartmentInstance.CODEC).fieldOf("compartments").forGetter(
                            stats -> new ArrayList<>(stats.compartments.values())
                    ),
                    UUIDUtil.CODEC.fieldOf("mainCompartmentID").forGetter(MedicalStats::getMainCompartmentID),
                    UUIDUtil.CODEC.fieldOf("characterID").forGetter(MedicalStats::getCharacterID),
                    Codec.unboundedMap(MedicalAttribute.CODEC, MedicalAttributeInstance.CODEC).fieldOf("attributes").forGetter(MedicalStats::getAttributes),
                    Codec.list(DrugInstance.CODEC).fieldOf("activeDrugs").forGetter(MedicalStats::getActiveDrugs)
            ).apply(instance, MedicalStats::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MedicalStats> STREAM_CODEC = StreamCodec.composite(
            CompartmentInstance.STREAM_CODEC.apply(
                    ByteBufCodecs.collection(ArrayList::new)
            ),
            MedicalStats::getCompartmentsCollection,
            UUIDUtil.STREAM_CODEC,
            MedicalStats::getMainCompartmentID,
            UUIDUtil.STREAM_CODEC,
            MedicalStats::getCharacterID,
            ByteBufCodecs.map(
                    HashMap::new,
                    MedicalAttribute.STREAM_CODEC,
                    MedicalAttributeInstance.STREAM_CODEC
            ),
            MedicalStats::getAttributes,
            DrugInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
            MedicalStats::getActiveDrugs,
            MedicalStats::new
    );

    protected final Map<UUID, CompartmentInstance> compartments;
    protected final UUID mainCompartmentID;
    protected final UUID characterID;
    private final EnumMap<MedicalAttribute, MedicalAttributeInstance> medicalAttributes;
    protected final Map<Holder<Attribute>, Double> defaultEntityAttributes;
    protected final List<DrugInstance> activeDrugs;
    protected final Map<CompartmentInstance, CompartmentInstance> compartmentRelations;

    protected LivingEntity entity;
    protected Character character;

    public MedicalStats(@NotNull Collection<CompartmentInstance> compartments, UUID mainCompartmentID, UUID characterID, Map<MedicalAttribute, MedicalAttributeInstance> attributes, List<DrugInstance> activeDrugs) {
        this.mainCompartmentID = mainCompartmentID;
        this.compartments = new ConcurrentHashMap<>();
        for (CompartmentInstance instance : compartments) {
            this.compartments.put(instance.getUUID(), instance);
        }
        this.characterID = characterID;
        this.activeDrugs = new ArrayList<>(activeDrugs);
        medicalAttributes = new EnumMap<>(MedicalAttribute.class);
        medicalAttributes.putAll(attributes);
        this.defaultEntityAttributes = new HashMap<>();
        this.compartmentRelations = new HashMap<>();
    }

    public MedicalStats(@NotNull List<CompartmentInstance> compartments, UUID mainCompartmentID, UUID characterID) {
        this(compartments, mainCompartmentID, characterID, new EnumMap<>(MedicalAttribute.class), new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    private void initializeEntity(Level level) {
        if (character == null) {
            character = CharacterManager.get(level).getCharacter(characterID);
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

    public void update(@NotNull Level level) {
        if (level.isClientSide) return;

        if (entity == null || defaultEntityAttributes.isEmpty()) {
            initializeEntity(level);
            return;
        }

        updateCompartments();
        updateEntityAttributes();
        tickDrugs();
        handleMobEffects();
    }

    private void updateCompartments() {
        for (CompartmentInstance compartment : compartments.values()) {
            compartment.tick(this);

            if (compartment.isDirty()) {
                for (MedicalAttribute attribute : compartment.getAttributes().keySet()) {
                    MedicalAttributeInstance instance = medicalAttributes.computeIfAbsent(attribute,
                            (k) -> new MedicalAttributeInstance());
                    instance.updateModifier(compartment.getUUID(), compartment.getAttribute(attribute));
                }
                compartment.setDirty(false);
            }
        }
    }

    private void updateEntityAttributes() {
        updateMovementAttributes();
        updateManipulationAttributes();
    }

    private void tickDrugs() {
        Iterator<DrugInstance> iterator = activeDrugs.iterator();
        while (iterator.hasNext()) {
            DrugInstance drug = iterator.next();
            drug.tickInstance(this);
            if (drug.getAmount() <= 0) {
                drug.remove(this);
                iterator.remove();
            }
        }
    }

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

        if (compartment == null) {
            compartments.remove(uuid);
        }

        return compartment;
    }

    public Map<UUID, CompartmentInstance> getCompartments() {
        return compartments;
    }

    public Collection<CompartmentInstance> getCompartmentsCollection() {
        return compartments.values();
    }

    public void removeCompartment(@NotNull CompartmentInstance compartment) {
    }

    public void addCompartment(CompartmentInstance compartment) {
        compartments.put(compartment.getUUID(), compartment);

        if (entity != null) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new UpdateHealthScreen(characterID, this));
        }
    }

    public void addRelation(CompartmentInstance child, CompartmentInstance parent) {
        compartmentRelations.put(child, parent);
    }

    public CompartmentInstance getParent(CompartmentInstance child) {
        return compartmentRelations.computeIfAbsent(child, this::findParent);
    }

    private CompartmentInstance findParent(@NotNull CompartmentInstance child) {
        UUID childId = child.getUUID();

        return compartments.values().stream()
                .filter(parent -> parent.getLayers().stream()
                        .anyMatch(layer -> layer.getCompartments().contains(childId)))
                .findFirst()
                .orElse(null);
    }

    public void addDrug(DrugInstance instance) {
        activeDrugs.add(instance);
    }

    public void removeDrug(DrugInstance instance) {
        activeDrugs.remove(instance);
    }

    public List<DrugInstance> getActiveDrugs() {
        return activeDrugs;
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

    public UUID getMainCompartmentID() {
        return mainCompartmentID;
    }

    public CompartmentInstance getMainCompartment() {
        return getCompartment(mainCompartmentID);
    }

    public UUID getCharacterID() {
        return characterID;
    }

    public UUID getEntityID() {
        if (entity == null) return null;
        return entity.getUUID();
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
