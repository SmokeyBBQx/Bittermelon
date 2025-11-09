package com.site21.bittermelon.common.systems.medical.compartments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
    private final List<HashSet<UUID>> layers;
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

    public CompartmentInstance(@NotNull Compartment compartment, UUID uuid, List<HashSet<UUID>> layers, float health,
                               float maxHealth, EnumMap<MedicalAttribute, Float> attributes, EnumSet<CompartmentTag> tags,
                               String name, VisualData visualData) {
        this.compartment = compartment;
        this.uuid = uuid;
        this.layers = layers;
        this.health = health;
        this.maxHealth = maxHealth;
        this.attributes = attributes;
        this.tags = tags;
        this.name = name;
        this.visualData = visualData;
        dirty = true;
    }

    public CompartmentInstance(@NotNull Holder<Compartment> compartment, UUID uuid, List<HashSet<UUID>> layers, float health, float maxHealth,
                               EnumMap<MedicalAttribute, Float> attributes, EnumSet<CompartmentTag> tags, String name, VisualData visualData) {
        this(compartment.value(), uuid, layers, health, maxHealth, attributes, tags, name, visualData);
    }

    public void tick(MedicalStats medicalStats) {
        compartment.tick(medicalStats, this);

        float newFunction = health / maxHealth;
        if (function != newFunction) {
            function = newFunction;
            dirty = true;
        }
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
                    .flatMap(Set::stream)
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

    public List<HashSet<UUID>> getLayers() {
        return layers;
    }

    public Set<UUID> getLayer(int index) {
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

    public boolean tryToInsert(int layer, CompartmentInstance instance) {
        return getCompartment().tryToInsert(this, instance, layer);
    }

    protected void addCompartment(int layer, @NotNull CompartmentInstance instance) {
        layers.get(layer).add(instance.uuid);
    }

    public void removeCompartment(int layer, @NotNull CompartmentInstance instance) {
        layers.get(layer).remove(instance.uuid);
    }

    public void removeCompartment(CompartmentInstance instance) {
        layers.forEach(set -> set.remove(instance.uuid));
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

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                COMPARTMENT_REGISTRY.byNameCodec().fieldOf("compartment").forGetter(CompartmentInstance::getCompartment),
                UUIDUtil.CODEC.fieldOf("uuid").forGetter(CompartmentInstance::getUUID),
                Codec.list(Codec.list(UUIDUtil.CODEC).xmap(
                        HashSet::new,
                        ArrayList::new
                )).fieldOf("layers").forGetter(CompartmentInstance::getLayers),
                Codec.FLOAT.fieldOf("health").forGetter(CompartmentInstance::getHealth),
                Codec.FLOAT.fieldOf("max_health").forGetter(CompartmentInstance::getMaxHealth),
                Codec.unboundedMap(MedicalAttribute.CODEC, Codec.FLOAT).xmap(
                        map -> {
                            EnumMap<MedicalAttribute, Float> em = new EnumMap<>(MedicalAttribute.class);
                            em.putAll(map);
                            return em;
                        },
                        em -> em
                ).fieldOf("attributes").forGetter(CompartmentInstance::getAttributes),
                CompartmentTag.CODEC.listOf().xmap(
                        list -> list.isEmpty() ? EnumSet.noneOf(CompartmentTag.class) : EnumSet.copyOf(list),
                        ArrayList::new
                ).fieldOf("tags").forGetter(CompartmentInstance::getTags),
                Codec.STRING.fieldOf("name").forGetter(CompartmentInstance::getName),
                VisualData.CODEC.fieldOf("visual_data").forGetter(CompartmentInstance::getVisualData)
        ).apply(instance, CompartmentInstance::new));

        STREAM_CODEC = new StreamCodec<>() {
            @Override
            public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull CompartmentInstance value) {
                COMPARTMENT_STREAM_CODEC.encode(buf, value.getCompartmentHolder());
                buf.writeUUID(value.getUUID());
                ByteBufCodecs.collection(HashSet::new, UUIDUtil.STREAM_CODEC)
                        .apply(ByteBufCodecs.list()).encode(buf, value.getLayers());
                buf.writeFloat(value.getHealth());
                buf.writeFloat(value.getMaxHealth());
                buf.writeMap(value.getAttributes(),
                        FriendlyByteBuf::writeEnum,
                        FriendlyByteBuf::writeFloat
                );
                buf.writeEnumSet(value.getTags(), CompartmentTag.class);
                buf.writeUtf(value.getName());
                VisualData.STREAM_CODEC.encode(buf, value.getVisualData());
            }

            @Override
            public @NotNull CompartmentInstance decode(@NotNull RegistryFriendlyByteBuf buf) {
                Holder<Compartment> compartmentHolder = COMPARTMENT_STREAM_CODEC.decode(buf);
                UUID uuid = buf.readUUID();
                List<HashSet<UUID>> layers = buf.readList(byteBuf ->
                        byteBuf.readCollection(HashSet::new, byteBuf1 -> byteBuf1.readUUID()));
                float health = buf.readFloat();
                float maxHealth = buf.readFloat();
                EnumMap<MedicalAttribute, Float> attributes = new EnumMap<>(MedicalAttribute.class);
                Map<MedicalAttribute, Float> tempMap = buf.readMap(
                        byteBuf -> byteBuf.readEnum(MedicalAttribute.class),
                        FriendlyByteBuf::readFloat
                );
                attributes.putAll(tempMap);
                EnumSet<CompartmentTag> tags = buf.readEnumSet(CompartmentTag.class);
                String displayName = buf.readUtf();
                VisualData visualData = VisualData.STREAM_CODEC.decode(buf);

                return new CompartmentInstance(
                        compartmentHolder, uuid, layers, health, maxHealth, attributes, tags, displayName, visualData
                );
            }
        };

        COMPARTMENT_STREAM_CODEC = ByteBufCodecs.holderRegistry(COMPARTMENT_REGISTRY_KEY);
    }
}
