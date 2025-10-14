package com.site21.bittermelon.content.atmosphere.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.atmosphere.AtmosInstance;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AtmosBlockData {
    Map<BlockPos, UUID> atmosBlocks = new HashMap<>();

    public static final Codec<AtmosBlockData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(BlockPos.CODEC, UUIDUtil.CODEC)
                            .fieldOf("atmosBlocks")
                            .forGetter(AtmosBlockData::getAtmosBlocks)
            ).apply(instance, atmosBlocks -> {
                AtmosBlockData data = new AtmosBlockData();
                data.atmosBlocks.putAll(atmosBlocks);
                return data;
            })
    );

    public static final StreamCodec<ByteBuf, Map<BlockPos, UUID>> ATMOS_BLOCKS_STREAM_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    BlockPos.STREAM_CODEC,
                    UUIDUtil.STREAM_CODEC
            );

    public static final StreamCodec<ByteBuf, AtmosBlockData> STREAM_CODEC =
            StreamCodec.composite(
                    ATMOS_BLOCKS_STREAM_CODEC,
                    AtmosBlockData::getAtmosBlocks,
                    atmosBlocks -> {
                        AtmosBlockData data = new AtmosBlockData();
                        data.atmosBlocks.putAll(atmosBlocks);
                        return data;
                    }
            );

    public void setAtmosBlocks(Map<BlockPos, UUID> atmosBlocks) {
        this.atmosBlocks = atmosBlocks;
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
