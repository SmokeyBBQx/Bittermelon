package com.site21.bittermelon.common.content.entities.scp1507;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record OpinionValue(float trust, float respect) {
    public static final Codec<OpinionValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.fieldOf("trust").forGetter(OpinionValue::trust),
        Codec.FLOAT.fieldOf("respect").forGetter(OpinionValue::respect)
    ).apply(instance, OpinionValue::new));

    public static final OpinionValue NEUTRAL = new OpinionValue(0.5f, 0.5f);

    public OpinionValue combine(OpinionValue other) {
        return new OpinionValue(this.trust + other.trust, this.respect + other.respect);
    }
}
