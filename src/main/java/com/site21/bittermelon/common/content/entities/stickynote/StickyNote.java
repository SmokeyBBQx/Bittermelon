package com.site21.bittermelon.common.content.entities.stickynote;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class StickyNote extends HangingEntity {


    protected StickyNote(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected AABB calculateBoundingBox(BlockPos pos, Direction direction) {
        return null;
    }

    @Override
    public void playPlacementSound() {

    }

    @Override
    public void dropItem(ServerLevel level, @Nullable Entity causedBy) {

    }
}
