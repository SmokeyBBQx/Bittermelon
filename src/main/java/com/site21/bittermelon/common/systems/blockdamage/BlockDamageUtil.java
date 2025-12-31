package com.site21.bittermelon.common.systems.blockdamage;

import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.NotNull;

public class BlockDamageUtil {
    public static int getDamage(@NotNull LevelAccessor level, @NotNull BlockPos pos) {
        return getBlockDamageData(level, pos).getBlockDamage(pos);
    }

    public static int getVisualDamage(@NotNull LevelAccessor level, @NotNull BlockPos pos) {
        return getBlockDamageData(level, pos).getVisualBlockDamage(pos);
    }

    public static void addDamage(@NotNull LevelAccessor level, @NotNull BlockPos pos, int damage) {
        ChunkAccess chunk = level.getChunk(pos);
        BlockDamageData data = chunk.getData(BitterAttachmentTypes.BLOCK_DAMAGE);
        data.addBlockDamage(level, pos, damage);
        chunk.setData(BitterAttachmentTypes.BLOCK_DAMAGE, data);
    }

    public static void repairDamage(@NotNull LevelAccessor level, @NotNull BlockPos pos, int repairAmount) {
        ChunkAccess chunk = level.getChunk(pos);
        BlockDamageData data = chunk.getData(BitterAttachmentTypes.BLOCK_DAMAGE);
        data.repairBlockDamage(pos, repairAmount);
        chunk.setData(BitterAttachmentTypes.BLOCK_DAMAGE, data);
    }

    public static void clearDamage(@NotNull LevelAccessor level, @NotNull BlockPos pos) {
        ChunkAccess chunk = level.getChunk(pos);
        BlockDamageData data = chunk.getData(BitterAttachmentTypes.BLOCK_DAMAGE);
        data.getBlockDamages().remove(pos);
        chunk.setData(BitterAttachmentTypes.BLOCK_DAMAGE, data);
    }

    public static float getBlockResistance(@NotNull LevelAccessor level, @NotNull BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        return block.defaultDestroyTime();
    }

    public static @NotNull BlockDamageData getBlockDamageData(@NotNull LevelAccessor level, @NotNull BlockPos pos) {
        ChunkAccess chunk = level.getChunk(pos);
        return chunk.getData(BitterAttachmentTypes.BLOCK_DAMAGE);
    }
}
