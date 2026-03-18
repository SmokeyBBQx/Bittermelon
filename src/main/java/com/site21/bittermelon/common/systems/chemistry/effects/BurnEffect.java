package com.site21.bittermelon.common.systems.chemistry.effects;

import com.site21.bittermelon.common.systems.chemistry.ReactionEffect;
import com.site21.bittermelon.common.systems.chemistry.ReactionEffectType;
import com.site21.bittermelon.common.systems.substance.SubstanceContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import static com.site21.bittermelon.init.custom.ReactionEffects.BURN;

public class BurnEffect implements ReactionEffect {

    @Override
    public ReactionEffectType<?> getType() {
        return BURN.get();
    }

    @Override
    public void apply(SubstanceContainer substanceContainer, Level level, BlockPos pos, int amount) {
        level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
    }
}
