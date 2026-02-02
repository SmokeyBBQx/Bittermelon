package com.site21.bittermelon.common.content.entities.cage.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public record BlockInfo(BlockState state, Vec3i offset) {
    public static final Codec<BlockInfo> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockState.CODEC.fieldOf("state").forGetter(BlockInfo::state),
                    Vec3i.CODEC.fieldOf("offset").forGetter(BlockInfo::offset)
            ).apply(instance, BlockInfo::new)
    );

    public static final StreamCodec<FriendlyByteBuf, BlockState> BLOCK_STATE_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(FriendlyByteBuf buffer, BlockState value) {
            buffer.writeNbt(NbtUtils.writeBlockState(value));
        }

        @Override
        public BlockState decode(FriendlyByteBuf buffer) {
            HolderGetter<Block> holderGetter = VanillaRegistries.createLookup().lookupOrThrow(Registries.BLOCK);
            return NbtUtils.readBlockState(holderGetter, Objects.requireNonNull(buffer.readNbt()));
        }
    };

    public static final StreamCodec<FriendlyByteBuf, BlockInfo> STREAM_CODEC = StreamCodec.composite(
            BLOCK_STATE_STREAM_CODEC,
            BlockInfo::state,
            Vec3i.STREAM_CODEC,
            BlockInfo::offset,
            BlockInfo::new
    );
}
