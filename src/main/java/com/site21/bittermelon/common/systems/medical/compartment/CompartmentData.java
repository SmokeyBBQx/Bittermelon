package com.site21.bittermelon.common.systems.medical.compartment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY_KEY;

/**
 * Immutable data representation of a compartment instance. Used for components.
 */
public record CompartmentData(
        Holder<Compartment> compartment,
        UUID uuid,
        List<LayerData> layers,
        float health,
        float maxHealth,
        EnumMap<MedicalAttribute, Float> attributes,
        EnumSet<CompartmentTag> tags,
        String name,
        VisualData visualData
) {
    public static final Codec<CompartmentData> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, CompartmentData> STREAM_CODEC;
    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Compartment>> COMPARTMENT_STREAM_CODEC;

    @Contract("_ -> new")
    public static @NotNull CompartmentData fromInstance(@NotNull CompartmentInstance instance) {
        return new CompartmentData(
                instance.getCompartmentHolder(),
                instance.getId(),
                instance.getLayers(),
                instance.getHealth(),
                instance.getMaxHealth(),
                instance.getAttributes(),
                instance.getTags(),
                instance.getName(),
                instance.getVisualData()
        );
    }

    public @NotNull CompartmentInstance toInstance() {
        return new CompartmentInstance(
                compartment, uuid, new ArrayList<>(layers), health, maxHealth,
                new EnumMap<>(attributes), EnumSet.copyOf(tags), name, visualData
        );
    }

    @Contract("_ -> new")
    public @NotNull CompartmentData withHealth(float newHealth) {
        return new CompartmentData(compartment, uuid, layers, newHealth, maxHealth,
                attributes, tags, name, visualData);
    }

    public float getAttribute(MedicalAttribute attribute) {
        return attributes.getOrDefault(attribute, 0f);
    }

    public boolean hasTag(CompartmentTag tag) {
        return tags.contains(tag);
    }

    public Holder<Compartment> getCompartmentHolder() {
        return compartment;
    }

    public VisualData getVisualData() {
        return visualData;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CompartmentData(
                Holder<Compartment> compartment1, UUID uuid1, List<LayerData> layers1, float health1, float maxHealth1,
                EnumMap<MedicalAttribute, Float> attributes1, EnumSet<CompartmentTag> tags1, String name1,
                VisualData data
        ))) return false;
        return Objects.equals(compartment, compartment1) &&
                Objects.equals(uuid, uuid1) &&
                Objects.equals(layers, layers1) &&
                Float.compare(health, health1) == 0 &&
                Float.compare(maxHealth, maxHealth1) == 0 &&
                Objects.equals(attributes, attributes1) &&
                Objects.equals(tags, tags1) &&
                Objects.equals(name, name1) &&
                Objects.equals(visualData, data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compartment, uuid, layers, health, maxHealth,
                attributes, tags, name, visualData);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                COMPARTMENT_REGISTRY.holderByNameCodec().fieldOf("compartment").forGetter(CompartmentData::compartment),
                UUIDUtil.CODEC.fieldOf("uuid").forGetter(CompartmentData::uuid),
                Codec.list(LayerData.CODEC).fieldOf("layers").forGetter(CompartmentData::layers),
                Codec.FLOAT.fieldOf("health").forGetter(CompartmentData::health),
                Codec.FLOAT.fieldOf("max_health").forGetter(CompartmentData::maxHealth),
                Codec.unboundedMap(MedicalAttribute.CODEC, Codec.FLOAT).fieldOf("attributes").forGetter(CompartmentData::attributes),
                CompartmentTag.CODEC.listOf().fieldOf("tags").forGetter(cd -> new ArrayList<>(cd.tags())),
                Codec.STRING.fieldOf("name").forGetter(CompartmentData::name),
                VisualData.CODEC.fieldOf("visual_data").forGetter(CompartmentData::visualData)
        ).apply(instance, (compartment, uuid, layers, health, maxHealth,
                           attributes, tagsList, name, visualData) -> {
            EnumMap<MedicalAttribute, Float> attrMap = new EnumMap<>(MedicalAttribute.class);
            attrMap.putAll(attributes);
            EnumSet<CompartmentTag> tagSet = EnumSet.copyOf(tagsList);

            return new CompartmentData(compartment, uuid, layers, health, maxHealth,
                    attrMap, tagSet, name, visualData);
        }));

        STREAM_CODEC = new StreamCodec<>() {
            @Override
            public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull CompartmentData value) {
                COMPARTMENT_STREAM_CODEC.encode(buf, value.getCompartmentHolder());
                buf.writeUUID(value.uuid());
                LayerData.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, value.layers());
                buf.writeFloat(value.health());
                buf.writeFloat(value.maxHealth());
                buf.writeMap(value.attributes(),
                        FriendlyByteBuf::writeEnum,
                        FriendlyByteBuf::writeFloat
                );
                buf.writeEnumSet(value.tags(), CompartmentTag.class);
                buf.writeUtf(value.name());
                VisualData.STREAM_CODEC.encode(buf, value.visualData());
            }

            @Override
            public @NotNull CompartmentData decode(@NotNull RegistryFriendlyByteBuf buf) {
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
                String displayName = buf.readUtf();
                VisualData visualData = VisualData.STREAM_CODEC.decode(buf);

                return new CompartmentData(
                        compartmentHolder, uuid, layers, health, maxHealth, attributes, tags, displayName, visualData
                );
            }
        };

        COMPARTMENT_STREAM_CODEC = ByteBufCodecs.holderRegistry(COMPARTMENT_REGISTRY_KEY);
    }
}
