package com.site21.bittermelon.common.systems.medical.wound;

import net.minecraft.world.entity.Entity;

public interface AnatomyDefinition {
    HealthContainer createHealthContainer(Entity entity);
}
