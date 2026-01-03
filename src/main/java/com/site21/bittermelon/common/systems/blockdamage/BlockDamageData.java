package com.site21.bittermelon.common.systems.blockdamage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BlockDamageData {
    private static final int MAX_DAMAGE = 100;

    public static final Codec<BlockPos> BLOCK_POS_STRING_CODEC;
    public static final Codec<BlockDamageData> CODEC;
    public static final StreamCodec<ByteBuf, BlockDamageData> STREAM_CODEC;

    private final Map<BlockPos, Integer> blockDamage = new HashMap<>();

    public Map<BlockPos, Integer> getBlockDamages() {
        return blockDamage;
    }

    public void setBlockDamages(Map<BlockPos, Integer> damages) {
        blockDamage.clear();
        blockDamage.putAll(damages);
    }

    public int getBlockDamage(BlockPos pos) {
        return blockDamage.getOrDefault(pos, 0);
    }

    public int getVisualBlockDamage(BlockPos pos) {
        return getBlockDamage(pos) / 10;
    }

    public void addBlockDamage(LevelAccessor level, BlockPos pos, int damage) {
        float resistance = BlockDamageUtil.getBlockResistance(level, pos);
        int currentDamage = blockDamage.getOrDefault(pos, 0);
        int newDamage = Mth.clamp(currentDamage + Math.round(damage / resistance), 0, MAX_DAMAGE);
        if (newDamage >= MAX_DAMAGE) {
            blockDamage.remove(pos);
            level.destroyBlock(pos, true);
        } else {
            blockDamage.put(pos, newDamage);
        }
    }

    public void repairBlockDamage(BlockPos pos, int repairAmount) {
        int currentDamage = blockDamage.getOrDefault(pos, 0);
        int newDamage = Mth.clamp(currentDamage - repairAmount, 0, MAX_DAMAGE);
        if (newDamage <= 0) {
            blockDamage.remove(pos);
        } else {
            blockDamage.put(pos, newDamage);
        }
    }

    public static @NotNull BlockDamageData deserialize(Map<BlockPos, Integer> damages) {
        BlockDamageData data = new BlockDamageData();
        data.setBlockDamages(damages);
        return data;
    }

    @Contract("_ -> new")
    public static @NotNull BlockPos fromShortString(@NotNull String str) {
        String[] parts = str.split(", ");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        int z = Integer.parseInt(parts[2]);
        return new BlockPos(x, y, z);
    }

    static {
        BLOCK_POS_STRING_CODEC = Codec.stringResolver(BlockPos::toShortString, BlockDamageData::fromShortString);

        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.unboundedMap(BLOCK_POS_STRING_CODEC, Codec.INT).fieldOf("block_damage").forGetter(BlockDamageData::getBlockDamages)
        ).apply(instance, BlockDamageData::deserialize));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.map(
                        HashMap::new,
                        BlockPos.STREAM_CODEC,
                        ByteBufCodecs.INT
                ),
                BlockDamageData::getBlockDamages,
                BlockDamageData::deserialize
        );
    }
}
