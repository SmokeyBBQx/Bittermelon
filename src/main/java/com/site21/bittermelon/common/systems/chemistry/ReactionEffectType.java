package com.site21.bittermelon.common.systems.chemistry;

import com.mojang.serialization.MapCodec;

public record ReactionEffectType<T extends ReactionEffect>(MapCodec<T> codec) {
}

