package com.site21.bittermelon.atmosphere.data;

import com.site21.bittermelon.atmosphere.AtmosInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AtmosBlockData implements INBTSerializable<CompoundTag> {
    Map<BlockPos, UUID> atmosBlocks = new HashMap<>();

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        long[] data = new long[atmosBlocks.size() * 3];

        int i = 0;
        for (Map.Entry<BlockPos, UUID> entry : atmosBlocks.entrySet()) {
            data[i] = entry.getKey().asLong();
            data[i + 1] = entry.getValue().getMostSignificantBits();
            data[i + 2] = entry.getValue().getLeastSignificantBits();
            i += 3;
        }

        tag.put("Data", new LongArrayTag(data));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag tag) {
        atmosBlocks.clear();
        long[] data = tag.getLongArray("Data");

        for (int i = 0; i < data.length; i += 3) {
            BlockPos pos = BlockPos.of(data[i]);
            UUID uuid = new UUID(data[i + 1], data[i + 2]);
            atmosBlocks.put(pos, uuid);
        }
    }

    public Map<BlockPos, UUID> getAtmosBlocks() {
        return atmosBlocks;
    }

    public void addAtmosBlock(BlockPos pos, UUID uuid) {
        atmosBlocks.put(pos, uuid);
    }

    public void removeAtmosBlock(BlockPos pos) {
        atmosBlocks.remove(pos);
    }

    public AtmosInstance getAtmosInstance(Level level, BlockPos pos) {
        UUID uuid = atmosBlocks.get(pos);
        if (uuid == null) {
            return null;
        }
        return AtmosLevelData.get(level).getAtmosInstance(uuid);
    }
}
