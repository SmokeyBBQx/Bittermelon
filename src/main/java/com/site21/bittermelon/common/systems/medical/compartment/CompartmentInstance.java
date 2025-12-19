package com.site21.bittermelon.common.systems.medical.compartment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY_KEY;

public class CompartmentInstance {
    public static final Codec<CompartmentInstance> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, CompartmentInstance> STREAM_CODEC;
    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Compartment>> COMPARTMENT_STREAM_CODEC;

    private final Compartment compartment;
    private final UUID id;
    private final List<LayerData> layers;
    private final float maxHealth;
    private final EnumMap<MedicalAttribute, Float> attributes;
    private final EnumSet<CompartmentTag> tags;
    private final String name;
    private final VisualData visualData;

    private float health;
    private float function;
    private boolean dirty;
    private Set<CompartmentInstance> cachedCompartments;
    private boolean compartmentsCacheDirty = true;

    public CompartmentInstance(@NotNull Compartment compartment, UUID id, List<LayerData> layers, float health,
                               float maxHealth, EnumMap<MedicalAttribute, Float> attributes, EnumSet<CompartmentTag> tags,
                               String name, VisualData visualData) {
        this.compartment = compartment;
        this.id = id;
        this.layers = layers;
        this.health = health;
        this.maxHealth = maxHealth;
        this.attributes = attributes;
        this.tags = tags;
        this.name = name;
        this.visualData = visualData;
        dirty = true;
    }

    public CompartmentInstance(@NotNull Holder<Compartment> compartment, UUID id, List<LayerData> layers, float health,
                               float maxHealth, EnumMap<MedicalAttribute, Float> attributes, EnumSet<CompartmentTag> tags,
                               String name, VisualData visualData) {
        this(compartment.value(), id, layers, health, maxHealth, attributes, tags, name, visualData);
    }

    /**
     * Convenience constructor for stream codec
     */
    public CompartmentInstance(@NotNull Holder<Compartment> compartmentHolder, UUID id, List<LayerData> layers,
                               Float health, Float maxHealth, @NotNull Map<MedicalAttribute, Float> attributes, Collection<CompartmentTag> tags,
                               String name, VisualData visualData) {
        this(compartmentHolder.value(), id, layers, health, maxHealth, new EnumMap<>(MedicalAttribute.class),
                EnumSet.noneOf(CompartmentTag.class), name, visualData);
        this.attributes.putAll(attributes);
        this.tags.addAll(tags);
    }

    /**
     * Convenience constructor for codec
     */
    public CompartmentInstance(@NotNull Compartment compartment, UUID id, List<LayerData> layers,
                               float health, float maxHealth, @NotNull Map<MedicalAttribute, Float> attributes,
                               List<CompartmentTag> tags, String name, VisualData visualData) {
        this(compartment, id, layers, health, maxHealth, new EnumMap<>(MedicalAttribute.class),
                EnumSet.noneOf(CompartmentTag.class), name, visualData);
        this.attributes.putAll(attributes);
        this.tags.addAll(tags);
    }

    public void tick(MedicalStats medicalStats) {
        compartment.tick(medicalStats, this);

        float newFunction = health / maxHealth;
        if (function != newFunction) {
            function = newFunction;
            dirty = true;
        }
    }

    /** <h4>Getters and Setters</h4> **/
    public float getAttribute(MedicalAttribute attribute) {
        return attributes.getOrDefault(attribute, 0f) * function;
    }

    public Compartment getCompartment() {
        return compartment;
    }

    public Holder<Compartment> getCompartmentHolder() {
        return compartment.builtInRegistryHolder();
    }

    public UUID getId() {
        return id;
    }

    public List<LayerData> getLayers() {
        return layers;
    }

    public LayerData getLayer(int index) {
        return layers.get(index);
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public EnumMap<MedicalAttribute, Float> getAttributes() {
        return attributes;
    }

    public EnumSet<CompartmentTag> getTags() {
        return tags;
    }

    public List<CompartmentTag> getTagsAsList() {
        return new ArrayList<>(tags);
    }

    public boolean hasTag(CompartmentTag tag) {
        return tags.contains(tag);
    }

    public String getName() {
        return name;
    }

    public VisualData getVisualData() {
        return visualData;
    }

    public Item getItem() {
        return compartment.getItem();
    }

    public boolean isDirty() {
        return dirty;
    }

    public void removeCompartment(int layer, @NotNull CompartmentInstance instance) {
        layers.get(layer).removeInstance(instance.id);
    }

    public void removeCompartment(CompartmentInstance instance) {
        layers.forEach(set -> set.removeInstance(instance.id));
    }

    public void setAttribute(MedicalAttribute attribute, float value) {
        attributes.put(attribute, value);
    }

    public void setIcon(ResourceLocation location) {
        getVisualData().icon = location;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void addTag(CompartmentTag tag) {
        tags.add(tag);
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public CompartmentData toData() {
        return CompartmentData.fromInstance(this);
    }

    @Contract("_ -> new")
    public static @NotNull CompartmentInstance fromData(@NotNull CompartmentData data) {
        return data.toInstance();
    }

    /** <h4>Interaction Methods</h4> **/
    public boolean tryToInsert(int layer, int x, int y, @NotNull CompartmentInstance instance) {
        return layers.get(layer).tryToPlace(x, y, instance);
    }

    public void removeCompartment(int layer, UUID instanceID) {
        layers.get(layer).removeInstance(instanceID);
    }

    public boolean canExtract(MedicalStats medicalStats) {
        return compartment.canExtract(this, medicalStats);
    }

    static {
        COMPARTMENT_STREAM_CODEC = ByteBufCodecs.holderRegistry(COMPARTMENT_REGISTRY_KEY);

        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                COMPARTMENT_REGISTRY.byNameCodec().fieldOf("compartment").forGetter(CompartmentInstance::getCompartment),
                UUIDUtil.CODEC.fieldOf("id").forGetter(CompartmentInstance::getId),
                Codec.list(LayerData.CODEC).fieldOf("layers").forGetter(CompartmentInstance::getLayers),
                Codec.FLOAT.fieldOf("health").forGetter(CompartmentInstance::getHealth),
                Codec.FLOAT.fieldOf("max_health").forGetter(CompartmentInstance::getMaxHealth),
                Codec.unboundedMap(MedicalAttribute.CODEC, Codec.FLOAT).fieldOf("attributes").forGetter(CompartmentInstance::getAttributes),
                Codec.list(CompartmentTag.CODEC).fieldOf("tags").forGetter(CompartmentInstance::getTagsAsList),
                Codec.STRING.fieldOf("name").forGetter(CompartmentInstance::getName),
                VisualData.CODEC.fieldOf("visual_data").forGetter(CompartmentInstance::getVisualData)
        ).apply(instance, CompartmentInstance::new));

        STREAM_CODEC = StreamCodec.composite(
                COMPARTMENT_STREAM_CODEC,
                CompartmentInstance::getCompartmentHolder,
                UUIDUtil.STREAM_CODEC,
                CompartmentInstance::getId,
                LayerData.STREAM_CODEC.apply(ByteBufCodecs.list()),
                CompartmentInstance::getLayers,
                ByteBufCodecs.FLOAT,
                CompartmentInstance::getHealth,
                ByteBufCodecs.FLOAT,
                CompartmentInstance::getMaxHealth,
                ByteBufCodecs.map(
                        HashMap::new,
                        MedicalAttribute.STREAM_CODEC,
                        ByteBufCodecs.FLOAT,
                        MedicalAttribute.values().length
                ),
                CompartmentInstance::getAttributes,
                ByteBufCodecs.collection(
                        HashSet::new,
                        CompartmentTag.STREAM_CODEC,
                        CompartmentTag.values().length
                ),
                CompartmentInstance::getTags,
                ByteBufCodecs.STRING_UTF8,
                CompartmentInstance::getName,
                VisualData.STREAM_CODEC,
                CompartmentInstance::getVisualData,
                CompartmentInstance::new
        );
    }
}
