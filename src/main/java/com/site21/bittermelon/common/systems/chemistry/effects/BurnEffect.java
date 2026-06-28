package com.site21.bittermelon.common.systems.chemistry.effects;

import com.site21.bittermelon.common.systems.chemistry.ReactionEffect;
import com.site21.bittermelon.common.systems.chemistry.ReactionEffectType;
import com.site21.bittermelon.common.systems.substance.SubstanceContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

import static com.site21.bittermelon.init.custom.ReactionEffects.BURN;
import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class BurnEffect implements ReactionEffect {

    @Override
    public ReactionEffectType<?> getType() {
        return BURN.get();
    }

    @Override
    public void apply(SubstanceContainer substanceContainer, Level level, BlockPos pos, int amount) {
        BlockState state = level.getBlockState(pos);
        Optional<Boolean> lit = state.getOptionalValue(BlockStateProperties.LIT);

        if (lit.isPresent()) {
            if (!lit.get()) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0f,
                        0.8f + level.getRandom().nextFloat() * 0.4f);
            }
        } else if (state.canBeReplaced()) {
            level.setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0f,
                    0.8f + level.getRandom().nextFloat() * 0.4f);
        }
    }
}
