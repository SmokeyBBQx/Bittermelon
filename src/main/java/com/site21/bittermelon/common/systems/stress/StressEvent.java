package com.site21.bittermelon.common.systems.stress;

import net.minecraft.world.entity.Entity;

public interface StressEvent {
    StressEvent getRandomEvent(Entity entity);

}
