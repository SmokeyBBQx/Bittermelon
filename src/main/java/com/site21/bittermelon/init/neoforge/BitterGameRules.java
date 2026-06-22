package com.site21.bittermelon.init.neoforge;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

public class BitterGameRules {

    public static final GameRule<Boolean> ENTITIES_MAKE_FLOORS_DIRTY_RULE = Registry.register(
            BuiltInRegistries.GAME_RULE,
            Bittermelon.identifier("entities_make_floors_dirty"),
            new GameRule<>(
                    GameRuleCategory.MISC,
                    GameRuleType.BOOL,
                    BoolArgumentType.bool(),
                    GameRuleTypeVisitor::visitBoolean,
                    Codec.BOOL,
                    gameRuleValue -> gameRuleValue ? 1 : 0,
                    false,
                    FeatureFlagSet.of()
            )
    );
}
