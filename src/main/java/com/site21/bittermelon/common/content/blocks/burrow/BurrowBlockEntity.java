package com.site21.bittermelon.common.content.blocks.burrow;

import com.site21.bittermelon.init.neoforge.BitterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public class BurrowBlockEntity extends BlockEntity {
    private LivingEntity entity;

    public BurrowBlockEntity(BlockPos pos, BlockState blockState) {
        super(BitterBlockEntities.BURROW_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void emergeEntity() {
        assert level != null;
        if (entity == null) return;

        entity.revive();
        entity.setPos(worldPosition.getX() + 0.5, worldPosition.getY(), worldPosition.getZ() + 0.5);
        level.addFreshEntity(entity);
        entity = null;
        level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 2.0f);
        setChanged();
    }

    public void insertEntity(LivingEntity entityToStore) {
        assert level != null;
        if (entity != null) return;

        entity = entityToStore;
        level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 0.0f);
        setChanged();
        entity.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
    }

    public boolean isOccupied() {
        return entity != null;
    }

    @Override
    public void setRemoved() {
        emergeEntity();
        super.setRemoved();
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        if (entity != null) {
            entity.save(output);
        }
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        if (level == null) return;
        entity = (LivingEntity) EntityType.create(input, level, EntitySpawnReason.LOAD).orElse(null);
    }
}
