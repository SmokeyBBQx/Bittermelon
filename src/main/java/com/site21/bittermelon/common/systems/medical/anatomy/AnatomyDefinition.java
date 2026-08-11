package com.site21.bittermelon.common.systems.medical.anatomy;

import com.site21.bittermelon.common.systems.medical.bodypart.HealthContainer;
import net.minecraft.world.entity.Entity;

public interface AnatomyDefinition {
    HealthContainer createHealthContainer(Entity entity);
}
