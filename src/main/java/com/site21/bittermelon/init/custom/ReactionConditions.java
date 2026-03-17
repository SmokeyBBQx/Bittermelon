package com.site21.bittermelon.init.custom;

import com.mojang.serialization.MapCodec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.chemistry.ReactionConditionType;
import com.site21.bittermelon.common.systems.chemistry.conditions.IgniteCondition;
import com.site21.bittermelon.init.neoforge.BitterRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReactionConditions {
    public static final DeferredRegister<ReactionConditionType<?>> REACTION_CONDITION_TYPES =
            DeferredRegister.create(BitterRegistries.REACTION_CONDITION_TYPE_KEY, Bittermelon.MOD_ID);

    public static final DeferredHolder<ReactionConditionType<?>, ReactionConditionType<IgniteCondition>> IGNITE =
            REACTION_CONDITION_TYPES.register("ignite", () -> new ReactionConditionType<>(MapCodec.unit(new IgniteCondition())));

}
