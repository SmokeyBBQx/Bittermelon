package com.site21.bittermelon.common.systems.chemistry.effects;

import com.site21.bittermelon.common.systems.chemistry.ReactionEffect;
import com.site21.bittermelon.common.systems.chemistry.Reactor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class SpawnEntityEffect implements ReactionEffect {
    private final EntityType<?> entityType;
    private final int maxAmount;

    public SpawnEntityEffect(EntityType<?> entityType, int maxAmount) {
        this.entityType = entityType;
        this.maxAmount = maxAmount;
    }

    @Override
    public void apply(Reactor reactor, Level level, BlockPos pos, int amount) {
        int actualAmount = Math.min(amount / 10, maxAmount);
        RandomSource random = level.getRandom();

        for (int i = 0; i < actualAmount; i++) {
            Entity entity = entityType.create(level, EntitySpawnReason.MOB_SUMMONED);
            if (entity != null) {
                entity.setPos(pos.getX() + 0.5 + (random.nextDouble() - 0.5), pos.getY() + 0.5, pos.getZ() + 0.5 + (random.nextDouble() - 0.5));
                level.addFreshEntity(entity);
            }
        }
    }
}
