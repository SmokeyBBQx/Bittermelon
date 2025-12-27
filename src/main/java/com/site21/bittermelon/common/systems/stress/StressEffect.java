package com.site21.bittermelon.common.systems.stress;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface StressEffect {
    void apply(Entity entity, Level level);
}
