package com.site21.bittermelon.common.systems.medical.anatomy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnatomyModel {
    public static final Codec<AnatomyModel> CODEC;
    public static final StreamCodec<ByteBuf, AnatomyModel> STREAM_CODEC;
    public static final AnatomyModel EMPTY = new AnatomyModel(new HashMap<>());

    private final BiMap<String, UUID> limbs;

    public AnatomyModel() {
        this.limbs = HashBiMap.create();
    }

    public AnatomyModel(BiMap<String, UUID> limbs) {
        this.limbs = limbs;
    }

    public AnatomyModel(Map<String, UUID> limbs) {
        this.limbs = HashBiMap.create(limbs);
    }

    public Map<String, UUID> getLimbs() {
        return limbs;
    }

    public void addLimb(String name, UUID id) {
        // TODO: dumb solution for serializing nulls, change this later
        if (id == null) id = UUID.fromString("00000000-0000-0000-0000-000000000000");

        limbs.put(name, id);
    }

    public Map<String, Boolean> getLimbVisibility() {
        Map<String, Boolean> visibility = new HashMap<>();
        for (Map.Entry<String, UUID> entry : limbs.entrySet()) {
            visibility.put(entry.getKey(),
                    entry.getValue().equals(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        }
        return visibility;
    }

    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.unboundedMap(Codec.STRING, UUIDUtil.CODEC).fieldOf("limbs").forGetter(AnatomyModel::getLimbs)
        ).apply(instance, AnatomyModel::new));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.map(
                        HashMap::new,
                        ByteBufCodecs.STRING_UTF8,
                        UUIDUtil.STREAM_CODEC
                ),
                AnatomyModel::getLimbs,
                AnatomyModel::new
        );
    }
}
