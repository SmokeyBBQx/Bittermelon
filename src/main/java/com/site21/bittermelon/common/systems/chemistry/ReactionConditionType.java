package com.site21.bittermelon.common.systems.chemistry;

import com.mojang.serialization.MapCodec;

public record ReactionConditionType<T extends ReactionCondition>(MapCodec<T> codec) {
}

