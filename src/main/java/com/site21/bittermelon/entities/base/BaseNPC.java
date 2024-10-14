package com.site21.bittermelon.entities.base;

import com.site21.bittermelon.character.Character;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public abstract class BaseNPC extends PathfinderMob {
    Character character;

    public BaseNPC(EntityType<?> entityType, Level level) {
        super((EntityType<? extends PathfinderMob>) entityType, level);
    }


}
