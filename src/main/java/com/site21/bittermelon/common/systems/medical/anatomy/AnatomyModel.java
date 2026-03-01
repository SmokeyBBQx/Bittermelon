package com.site21.bittermelon.common.systems.medical.anatomy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents the physical parts of the anatomy, used for rendering and other interactions with the game.
 */
public class AnatomyModel {
    public static final Codec<AnatomyModel> CODEC;
    public static final StreamCodec<ByteBuf, AnatomyModel> STREAM_CODEC;
    public static final AnatomyModel EMPTY = new AnatomyModel(new HashMap<>());

    private final Map<String, UUID> limbs;

    public AnatomyModel(Map<String, UUID> limbs) {
        this.limbs = limbs;
    }

    public AnatomyModel() {
        this(new HashMap<>());
    }

    public Map<String, UUID> getLimbs() {
        return limbs;
    }

    public void addLimb(String name, UUID id) {
        limbs.put(name, id);
    }

    public Map<String, Boolean> getLimbVisibility() {
        Map<String, Boolean> visibility = new HashMap<>();
        for (Map.Entry<String, UUID> entry : limbs.entrySet()) {
            visibility.put(entry.getKey(), entry.getValue() != null);
        }
        return visibility;
    }

    private Map<String, Optional<UUID>> getLimbsAsOptional() {
        Map<String, Optional<UUID>> result = new HashMap<>();
        for (Map.Entry<String, UUID> entry : limbs.entrySet()) {
            result.put(entry.getKey(), Optional.ofNullable(entry.getValue()));
        }
        return result;
    }

    @Contract("_ -> new")
    private static @NotNull AnatomyModel fromOptionalMap(Map<String, Optional<UUID>> map) {
        Map<String, UUID> limbs = new HashMap<>();
        for (Map.Entry<String, Optional<UUID>> entry : map.entrySet()) {
            limbs.put(entry.getKey(), entry.getValue().orElse(null));
        }
        return new AnatomyModel(limbs);
    }

    static {
        Codec<Optional<UUID>> OPTIONAL_UUID = UUIDUtil.CODEC.optionalFieldOf("uuid")
                .codec()
                .orElse(Optional.empty());

        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(Codec.STRING, OPTIONAL_UUID)
                        .fieldOf("limbs")
                        .forGetter(AnatomyModel::getLimbsAsOptional)
        ).apply(instance, AnatomyModel::fromOptionalMap));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.map(
                        HashMap::new,
                        ByteBufCodecs.STRING_UTF8,
                        UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs::optional)
                ),
                AnatomyModel::getLimbsAsOptional,
                AnatomyModel::fromOptionalMap
        );
    }
}
