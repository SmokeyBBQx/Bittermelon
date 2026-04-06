package com.site21.bittermelon.common.systems.chemistry.effects;

import com.site21.bittermelon.common.systems.chemistry.ReactionEffect;
import com.site21.bittermelon.common.systems.chemistry.ReactionEffectType;
import com.site21.bittermelon.common.systems.substance.SubstanceContainer;
import com.site21.bittermelon.init.custom.ReactionEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public record ChanceSetBlockEffect(Block block, float chance) implements ReactionEffect {
    @Override
    public ReactionEffectType<?> getType() {
        return ReactionEffects.CHANCE_SET_BLOCK.get();
    }

    @Override
    public void apply(SubstanceContainer substanceContainer, Level level, BlockPos pos, int amount) {
        if (level.getRandom().nextFloat() < chance) {
            level.setBlock(pos, block.defaultBlockState(), UPDATE_ALL);
        }
    }
}
