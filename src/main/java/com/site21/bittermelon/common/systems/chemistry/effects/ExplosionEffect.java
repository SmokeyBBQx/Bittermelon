package com.site21.bittermelon.common.systems.chemistry.effects;

import com.site21.bittermelon.common.systems.chemistry.ReactionEffect;
import com.site21.bittermelon.common.systems.chemistry.ReactionEffectType;
import com.site21.bittermelon.common.systems.substance.SubstanceContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import static com.site21.bittermelon.init.custom.ReactionEffects.EXPLOSION;

public class ExplosionEffect implements ReactionEffect {

    @Override
    public ReactionEffectType<?> getType() {
        return EXPLOSION.get();
    }

    @Override
    public void apply(SubstanceContainer substanceContainer, Level level, BlockPos pos, int amount) {
        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, amount / 100f,
                Level.ExplosionInteraction.BLOCK);
    }
}
