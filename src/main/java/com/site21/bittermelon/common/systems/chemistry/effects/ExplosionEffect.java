package com.site21.bittermelon.common.systems.chemistry.effects;

import com.site21.bittermelon.common.systems.chemistry.ReactionEffect;
import com.site21.bittermelon.common.systems.chemistry.Reactor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ExplosionEffect implements ReactionEffect {
    @Override
    public void apply(Reactor reactor, Level level, BlockPos pos, int amount) {
        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, amount / 10f,
                Level.ExplosionInteraction.BLOCK);
    }
}
