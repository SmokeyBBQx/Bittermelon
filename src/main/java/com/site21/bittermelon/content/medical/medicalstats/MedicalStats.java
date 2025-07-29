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

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.BAD_MOBILITY;

public class MedicalStats {
    public static final Codec<MedicalStats> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.list(CompartmentInstance.CODEC).fieldOf("compartments").forGetter(
                            stats -> new ArrayList<>(stats.compartments.values())
                    ),
                    UUIDUtil.CODEC.fieldOf("mainCompartmentID").forGetter(MedicalStats::getMainCompartmentID),
                    UUIDUtil.CODEC.fieldOf("characterID").forGetter(MedicalStats::getCharacterID),
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
            DrugInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
            MedicalStats::getActiveDrugs,
            MedicalStats::new
    );

    protected final Map<UUID, CompartmentInstance> compartments;
    protected final UUID mainCompartmentID;
    protected final Map<MedicalAttribute, Float> medicalAttributes;
    protected final UUID characterID;
    protected final Map<Holder<Attribute>, Double> defaultEntityAttributes;
    protected final List<DrugInstance> activeDrugs;
    protected final Map<CompartmentInstance, CompartmentInstance> compartmentRelations;

    protected LivingEntity entity;
    protected Character character;

    public MedicalStats(@NotNull Collection<CompartmentInstance> compartments, UUID mainCompartmentID, UUID characterID, List<DrugInstance> activeDrugs) {
        this.mainCompartmentID = mainCompartmentID;
        this.compartments = new ConcurrentHashMap<>();
        for (CompartmentInstance instance : compartments) {
            this.compartments.put(instance.getUUID(), instance);
        }
        this.characterID = characterID;
        this.activeDrugs = activeDrugs;
        this.medicalAttributes = new EnumMap<>(MedicalAttribute.class);
        this.defaultEntityAttributes = new HashMap<>();
        this.compartmentRelations = new HashMap<>();

        initializeStats();
    }

    public MedicalStats(@NotNull List<CompartmentInstance> compartments, UUID mainCompartmentID, UUID characterID) {
        this(compartments, mainCompartmentID, characterID, new ArrayList<>());
    }

    private void initializeStats() {
        for (MedicalAttribute type : MedicalAttribute.values()) {
            medicalAttributes.put(type, 0f);
        }
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
    }

    private void updateCompartments() {
        EnumMap<MedicalAttribute, Float> statsCopy = new EnumMap<>(MedicalAttribute.class);
        EnumMap<MedicalAttribute, Integer> countMap = new EnumMap<>(MedicalAttribute.class);

        for (MedicalAttribute type : MedicalAttribute.values()) {
            statsCopy.put(type, 0f);
            countMap.put(type, 0);
        }

        for (CompartmentInstance compartment : compartments.values()) {
            compartment.tick(this);

            for (MedicalAttribute stat : MedicalAttribute.values()) {
                float attribute = compartment.getAttribute(stat);
                if (attribute != 0) {
                    statsCopy.compute(stat, (k, currentValue) -> currentValue + attribute);
                    countMap.compute(stat, (k, count) -> count + 1);
                }
            }
        }

        for (MedicalAttribute type : MedicalAttribute.values()) {
            int count = countMap.get(type);
            if (count > 0) {
                float average = statsCopy.get(type) / count;
                statsCopy.put(type, average);
            }
        }

        medicalAttributes.putAll(statsCopy);
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

    public float getAttribute(MedicalAttribute attribute) {
        return medicalAttributes.get(attribute);
    }

    public float getTasteAbility() {
        return medicalAttributes.get(MedicalAttribute.BRAIN_TASTE) * medicalAttributes.get(MedicalAttribute.TASTE);
    }

    public float getHearingAbility() {
        return medicalAttributes.get(MedicalAttribute.BRAIN_HEARING) * medicalAttributes.get(MedicalAttribute.HEARING);
    }

    public float getLanguageComprehension() {
        return medicalAttributes.get(MedicalAttribute.BRAIN_LANGUAGE);
    }

    public float getTremor() {
        return medicalAttributes.get(MedicalAttribute.TREMOR) + getPain();
    }


    public float getStat(MedicalAttribute medicalAttribute) {
        return medicalAttributes.getOrDefault(medicalAttribute, 0.0f);
    }

    public float getManipulation() {
        return medicalAttributes.get(MedicalAttribute.BRAIN_MOTOR_ABILITY) * medicalAttributes.get(MedicalAttribute.MANIPULATION) * getConsciousness();
    }

    public float getMovement() {
        return medicalAttributes.get(MedicalAttribute.BRAIN_MOTOR_ABILITY) * medicalAttributes.get(MedicalAttribute.MOVEMENT) * getConsciousness();
    }

    public float getSight() {
        return medicalAttributes.get(MedicalAttribute.SIGHT) * medicalAttributes.get(MedicalAttribute.BRAIN_SIGHT) * getConsciousness();
    }

    public float getBite() {
        return medicalAttributes.get(MedicalAttribute.BRAIN_MOTOR_ABILITY) * medicalAttributes.get(MedicalAttribute.BITE) * getConsciousness();
    }

    public float getPain() {
        return medicalAttributes.get(MedicalAttribute.NERVOUS) * medicalAttributes.get(MedicalAttribute.PAIN) * getConsciousness();
        // TODO: Better way to get pain?
    }

    public float getElimination() {
        return getAttribute(MedicalAttribute.ELIMINATION) * getCirculation();
    }

    public float getAbsorption() {
        return getAttribute(MedicalAttribute.DIGESTION) * getCirculation();
    }

    public float getCirculation() {
        return medicalAttributes.get(MedicalAttribute.CIRCULATION);
    }

    public float getConsciousness() {
        return 1;
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
}
