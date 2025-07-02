package com.site21.bittermelon.content.medical.compartments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class LayerData {
    public static final Codec<LayerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("background_texture").forGetter(LayerData::getBackgroundTexture),
                    Codec.STRING.fieldOf("name").forGetter(LayerData::getName),
                    Codec.list(Codec.STRING.xmap(UUID::fromString, UUID::toString))
                            .fieldOf("compartments")
                            .forGetter(data -> data.compartments.stream().toList())
            ).apply(instance, LayerData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LayerData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            LayerData::getBackgroundTexture,
            StreamCodec.of(
                    FriendlyByteBuf::writeUtf,
                    FriendlyByteBuf::readUtf
            ),
            LayerData::getName,
            StreamCodec.of(
                    (buf, uuids) -> {
                        buf.writeVarInt(uuids.size());
                        for (UUID uuid : uuids) {
                            buf.writeUUID(uuid);
                        }
                    },
                    buf -> {
                        int size = buf.readVarInt();
                        HashSet<UUID> set = new HashSet<>();
                        for (int i = 0; i < size; i++) {
                            set.add(buf.readUUID());
                        }
                        return set;
                    }
            ),
            LayerData::getCompartments,
            LayerData::new
    );

    private final HashSet<UUID> compartments;
    private final String name;
    private final ResourceLocation backgroundTexture;

    private LayerData(ResourceLocation backgroundTexture, String name, HashSet<UUID> compartments) {
        this.backgroundTexture = backgroundTexture;
        this.name = name;
        this.compartments = compartments;
    }

    public LayerData(ResourceLocation backgroundTexture, String name, List<UUID> compartments) {
        this(backgroundTexture, name, new HashSet<>(compartments));
    }

    public LayerData(ResourceLocation backgroundTexture, String name) {
        this(backgroundTexture, name, new HashSet<>());
    }

    public HashSet<UUID> getCompartments() {
        return compartments;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getBackgroundTexture() {
        return backgroundTexture;
    }

    public void addCompartment(@NotNull CompartmentInstance instance) {
        compartments.add(instance.getUUID());
    }

    public void removeCompartment(@NotNull CompartmentInstance instance) {
        compartments.remove(instance.getUUID());
    }
}
