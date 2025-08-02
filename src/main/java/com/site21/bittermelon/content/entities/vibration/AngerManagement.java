package com.site21.bittermelon.content.entities.vibration;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.ExtraCodecs;

import java.util.UUID;

public class AngerManagement {
    protected static final int MAX_ANGER = 100;
    private static final int ANGER_DECAY = 1;
    private static final Codec<Pair<UUID, Integer>> SUSPECT_ANGER_PAIR = RecordCodecBuilder.create(
            pair -> pair.group(
                            UUIDUtil.CODEC.fieldOf("uuid").forGetter(Pair::getFirst), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("anger").forGetter(Pair::getSecond)
                    )
                    .apply(pair, Pair::of)
    );


    public AngerManagement() {}


}
