package com.site21.bittermelon.common.systems.fluid.substance;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

// This might be such a bad solution, but I have no other way of forcing the rerender
public record UpdateBlockAt(BlockPos pos) implements CustomPacketPayload {
    public static final Type<UpdateBlockAt> TYPE = new Type<>(Bittermelon.resource("update_block_at"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, UpdateBlockAt> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            UpdateBlockAt::pos,
            UpdateBlockAt::new
    );

    public void handle(IPayloadContext ctx) {
        Level level = ctx.player().level();
        BlockState state = level.getBlockState(pos);
        ctx.player().level().sendBlockUpdated(pos, state, state, UPDATE_ALL);
    }
}
