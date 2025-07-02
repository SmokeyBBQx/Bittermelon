package com.site21.bittermelon.content.medical.compartments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY_KEY;

public class CompartmentInstance {
    public static final Codec<CompartmentInstance> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, CompartmentInstance> STREAM_CODEC;
    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Compartment>> COMPARTMENT_STREAM_CODEC;

    private final Compartment compartment;
    private final UUID uuid;
    private final List<LayerData> layers;
    private final float maxHealth;
    private final EnumMap<MedicalAttribute, Float> attributes;
    private final EnumSet<CompartmentTag> tags;
    private final String name;
    private final VisualData visualData;

    private float health;
    private float function;
    private Item item;
    private boolean dirty = false;
    private Set<CompartmentInstance> cachedCompartments;
    private boolean compartmentsCacheDirty = true;

    public CompartmentInstance(@NotNull Compartment compartment, UUID uuid, List<LayerData> layers, float health,
                               float maxHealth, EnumMap<MedicalAttribute, Float> attributes, EnumSet<CompartmentTag> tags,
                               @NotNull Item item, String name, VisualData visualData) {
        this.compartment = compartment;
        this.uuid = uuid;
        this.layers = layers;
        this.health = health;
        this.maxHealth = maxHealth;
        this.attributes = attributes;
        this.tags = tags;
        this.item = item;
        this.name = name;
        this.visualData = visualData;
        dirty = true;
    }

    public CompartmentInstance(@NotNull Holder<Compartment> compartment, UUID uuid, List<LayerData> layers, float health, float maxHealth,
                               EnumMap<MedicalAttribute, Float> attributes, EnumSet<CompartmentTag> tags,
                               @NotNull Holder<Item> item, String name, VisualData visualData) {
        this(compartment.value(), uuid, layers, health, maxHealth, attributes, tags, item.value(), name, visualData);
    }

    public CompartmentInstance(Compartment compartment, float maxHealth, String name, VisualData visualData) {
        this(compartment, UUID.randomUUID(), new ArrayList<>(), maxHealth, maxHealth, new EnumMap<>(MedicalAttribute.class),
                EnumSet.noneOf(CompartmentTag.class), Items.AIR, name, visualData);
    }

    public void tick(MedicalStats medicalStats) {
        compartment.tick(medicalStats, this);

        function = 1;

//        if (dirty) {
//            updateFunction(medicalStats);
//            dirty = false;
//        }
    }

    public float getAttribute(MedicalAttribute attribute) {
        return attributes.getOrDefault(attribute, 0f) * function;
    }

    public void updateFunction(MedicalStats medicalStats) {
        float functionMultiplier = 1;
        int count = 0;

        for (CompartmentInstance instance : getAllCompartments(medicalStats)) {
            functionMultiplier += instance.getAttribute(MedicalAttribute.FUNCTION);
            count++;
        }

        function = health / maxHealth * functionMultiplier / count;
    }

    public Set<CompartmentInstance> getAllCompartments(MedicalStats medicalStats) {
        if (compartmentsCacheDirty || cachedCompartments == null) {
            cachedCompartments = layers.stream()
                    .flatMap(layer -> layer.getCompartments().stream())
                    .map(medicalStats::getCompartment)
                    .collect(Collectors.toSet());
            compartmentsCacheDirty = false;
        }
        return cachedCompartments;
    }

    public Compartment getCompartment() {
        return compartment;
    }

    public Holder<Compartment> getCompartmentHolder() {
        return compartment.builtInRegistryHolder();
    }

    public UUID getUUID() {
        return uuid;
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

    public boolean hasTag(CompartmentTag tag) {
        return tags.contains(tag);
    }

    public Item getItem() {
        return item;
    }

    public Holder<Item> getItemHolder() {
        return item.builtInRegistryHolder();
    }

    public String getName() {
        return name;
    }

    public VisualData getVisualData() {
        return visualData;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void addCompartment(int layer, CompartmentInstance instance) {
        layers.get(layer).addCompartment(instance);
    }

    public void removeCompartment(int layer, CompartmentInstance instance) {
        layers.get(layer).removeCompartment(instance);
    }

    public void removeCompartment(CompartmentInstance instance) {
        layers.forEach(layerData -> layerData.removeCompartment(instance));
    }

    public void setLayer(int index, LayerData layerData) {
        layers.add(index, layerData);
    }

    public void addLayer(LayerData layerData) {
        layers.add(layerData);
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

    public CompartmentData toData() {
        return CompartmentData.fromInstance(this);
    }

    @Contract("_ -> new")
    public static @NotNull CompartmentInstance fromData(@NotNull CompartmentData data) {
        return data.toInstance();
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                COMPARTMENT_REGISTRY.byNameCodec().fieldOf("compartment").forGetter(CompartmentInstance::getCompartment),
                UUIDUtil.CODEC.fieldOf("uuid").forGetter(CompartmentInstance::getUUID),
                LayerData.CODEC.listOf().fieldOf("layers").forGetter(CompartmentInstance::getLayers),
                Codec.FLOAT.fieldOf("max_health").forGetter(CompartmentInstance::getMaxHealth),
                Codec.FLOAT.fieldOf("health").forGetter(CompartmentInstance::getHealth),
                Codec.unboundedMap(MedicalAttribute.CODEC, Codec.FLOAT).fieldOf("attributes").forGetter(CompartmentInstance::getAttributes),
                CompartmentTag.CODEC.listOf().fieldOf("tags").forGetter(ci -> new ArrayList<>(ci.getTags())),
                BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("item", Items.AIR).forGetter(CompartmentInstance::getItem),
                Codec.STRING.fieldOf("name").forGetter(CompartmentInstance::getName),
                VisualData.CODEC.fieldOf("visual_data").forGetter(CompartmentInstance::getVisualData)
        ).apply(instance, (compartment, uuid, layers, maxHealth, health,
                           attributes, tagsList, item, name,
                           visualData) -> {
            EnumMap<MedicalAttribute, Float> attrMap = new EnumMap<>(MedicalAttribute.class);
            attrMap.putAll(attributes);
            EnumSet<CompartmentTag> tagSet = EnumSet.copyOf(tagsList);

            return new CompartmentInstance(compartment, uuid, layers, health, maxHealth, attrMap, tagSet, item, name, visualData);
        }));

        STREAM_CODEC = new StreamCodec<>() {
            @Override
            public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull CompartmentInstance value) {
                COMPARTMENT_STREAM_CODEC.encode(buf, value.getCompartmentHolder());
                buf.writeUUID(value.getUUID());
                LayerData.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, value.getLayers());
                buf.writeFloat(value.getHealth());
                buf.writeFloat(value.getMaxHealth());
                buf.writeMap(value.getAttributes(),
                        FriendlyByteBuf::writeEnum,
                        FriendlyByteBuf::writeFloat
                );
                buf.writeEnumSet(value.getTags(), CompartmentTag.class);
                ByteBufCodecs.holderRegistry(BuiltInRegistries.ITEM.key()).encode(buf, value.getItemHolder());
                buf.writeUtf(value.getName());
                VisualData.STREAM_CODEC.encode(buf, value.getVisualData());
            }

            @Override
            public @NotNull CompartmentInstance decode(@NotNull RegistryFriendlyByteBuf buf) {
                Holder<Compartment> compartmentHolder = COMPARTMENT_STREAM_CODEC.decode(buf);
                UUID uuid = buf.readUUID();
                List<LayerData> layers = LayerData.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf);
                float health = buf.readFloat();
                float maxHealth = buf.readFloat();
                EnumMap<MedicalAttribute, Float> attributes = new EnumMap<>(MedicalAttribute.class);
                Map<MedicalAttribute, Float> tempMap = buf.readMap(
                        byteBuf -> byteBuf.readEnum(MedicalAttribute.class),
                        FriendlyByteBuf::readFloat
                );
                attributes.putAll(tempMap);
                EnumSet<CompartmentTag> tags = buf.readEnumSet(CompartmentTag.class);
                Holder<Item> item = ByteBufCodecs.holderRegistry(BuiltInRegistries.ITEM.key()).decode(buf);
                String displayName = buf.readUtf();
                VisualData visualData = VisualData.STREAM_CODEC.decode(buf);

                return new CompartmentInstance(
                        compartmentHolder, uuid, layers, health, maxHealth, attributes, tags, item, displayName, visualData
                );
            }
        };

        COMPARTMENT_STREAM_CODEC = ByteBufCodecs.holderRegistry(COMPARTMENT_REGISTRY_KEY);
    }
}
