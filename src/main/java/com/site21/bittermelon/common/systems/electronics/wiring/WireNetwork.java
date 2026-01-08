package com.site21.bittermelon.common.systems.electronics.wiring;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.common.systems.blockdamage.BlockDamageData.BLOCK_POS_STRING_CODEC;

public class WireNetwork {
    public static final Codec<Set<BlockPos>> BLOCK_POS_SET_CODEC;
    public static final Codec<EnumMap<DyeColor, Set<BlockPos>>> ADJACENCY_MAP_CODEC;
    public static final Codec<WireNetwork> CODEC;
    public static final StreamCodec<ByteBuf, WireNetwork> STREAM_CODEC;

    private HashMap<BlockPos, EnumMap<DyeColor, Set<BlockPos>>> NETWORK;

    public Map<BlockPos, EnumMap<DyeColor, Set<BlockPos>>> getNetwork() {
        return NETWORK;
    }

    public void setNetwork(Map<BlockPos, EnumMap<DyeColor, Set<BlockPos>>> network) {
        NETWORK.clear();
        NETWORK.putAll(network);
    }

    /**
     * Check if two BlockPos are aligned straight along one axis
     *
     * @param pos1 the first position
     * @param pos2 the second position
     * @return true if the points are straight, false otherwise. Will return false if the points are the same.
     */
    public static boolean arePointsStraight(@NotNull BlockPos pos1, @NotNull BlockPos pos2) {
        int diffCount = 0;
        if (pos1.getX() != pos2.getX()) diffCount++;
        if (pos1.getY() != pos2.getY()) diffCount++;
        if (pos1.getZ() != pos2.getZ()) diffCount++;

        return diffCount == 1;
    }

    public void connectNodes(@NotNull BlockPos fromPos, @NotNull BlockPos toPos, DyeColor color) {
        if (!arePointsStraight(fromPos, toPos)) {
            Bittermelon.LOGGER.error("Tried to connect non-straight points {} and {}", fromPos, toPos);
            return;
        }

        // Should always be undirected
        addConnection(fromPos, toPos, color);
        addConnection(toPos, fromPos, color);
    }

    private void addConnection(BlockPos fromPos, BlockPos toPos, DyeColor color) {
        NETWORK.computeIfAbsent(fromPos, k -> new EnumMap<>(DyeColor.class))
                .computeIfAbsent(color, k -> new HashSet<>()).add(toPos);
    }

    private void removeConnection(BlockPos fromPos, BlockPos toPos, DyeColor color) {
        if (!NETWORK.containsKey(fromPos) || !NETWORK.get(fromPos).containsKey(color)) {
            Bittermelon.LOGGER.error("Tried to remove non-existent connection from {} to {} of color {}", fromPos, toPos, color);
            return;
        }
        NETWORK.get(fromPos).get(color).remove(toPos);
    }

    public Set<BlockPos> getConnectedNodes(BlockPos pos, DyeColor color) {
        return NETWORK.getOrDefault(pos, new EnumMap<>(DyeColor.class)).getOrDefault(color, Set.of());
    }

    public static @NotNull WireNetwork deserialize(Map<BlockPos, EnumMap<DyeColor, Set<BlockPos>>> damages) {
        WireNetwork data = new WireNetwork();
        data.setNetwork(damages);
        return data;
    }

    static {
        BLOCK_POS_SET_CODEC = Codec.list(BlockPos.CODEC).xmap(
                HashSet::new,
                ArrayList::new
        );

        ADJACENCY_MAP_CODEC = Codec.unboundedMap(DyeColor.CODEC, BLOCK_POS_SET_CODEC).xmap(
                EnumMap::new,
                map -> map
        );

        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.unboundedMap(BLOCK_POS_STRING_CODEC, ADJACENCY_MAP_CODEC).fieldOf("network")
                        .forGetter(WireNetwork::getNetwork)
        ).apply(instance, WireNetwork::deserialize));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.map(
                        HashMap::new,
                        BlockPos.STREAM_CODEC,
                        ByteBufCodecs.map(
                                capacity -> new EnumMap<>(DyeColor.class),
                                DyeColor.STREAM_CODEC,
                                ByteBufCodecs.collection(
                                        HashSet::new,
                                        BlockPos.STREAM_CODEC
                                )
                        )
                ),
                WireNetwork::getNetwork,
                WireNetwork::deserialize
        );
    }
}
