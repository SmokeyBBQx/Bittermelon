package com.site21.bittermelon.content.entities.miscellaneous;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ThrownBallProjectile extends ThrownItemProjectile {
    public ThrownBallProjectile(EntityType<? extends ThrownItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {

    }
}
