package com.site21.bittermelon.common.content.items.scps.scp377;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FortuneInstance(Fortune fortune, long readTime) {
    public final static Codec<FortuneInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Fortune.CODEC.fieldOf("fortune").forGetter(FortuneInstance::fortune),
                    Codec.LONG.fieldOf("readTime").forGetter(FortuneInstance::readTime)
            ).apply(instance, FortuneInstance::new)
    );
}
