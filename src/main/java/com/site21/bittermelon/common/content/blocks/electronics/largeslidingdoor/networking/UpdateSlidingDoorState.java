package com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock.Z_AXIS;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.LARGE_SLIDING_DOOR;

public record UpdateSlidingDoorState(BlockPos pos, LargeSlidingDoorBlock.State state) implements CustomPacketPayload {
    public static final Type<UpdateSlidingDoorState> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_sliding_door_state"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, UpdateSlidingDoorState> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            UpdateSlidingDoorState::pos,
            LargeSlidingDoorBlock.State.STATE_STREAM_CODEC,
            UpdateSlidingDoorState::state,
            UpdateSlidingDoorState::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        BlockState updatedState = level.getBlockState(pos).setValue(LargeSlidingDoorBlock.STATE, state);
        level.setBlock(pos, updatedState, 3);

        if (state == LargeSlidingDoorBlock.State.CLOSED) {
            boolean zPlane = level.getBlockState(pos).getValue(Z_AXIS);
            level.setBlock(pos.below(), LARGE_SLIDING_DOOR.get().defaultBlockState().setValue(Z_AXIS, zPlane), 3);
            level.setBlock(pos.below(2), LARGE_SLIDING_DOOR.get().defaultBlockState().setValue(Z_AXIS, zPlane), 3);
            if (level.getBlockEntity(pos) instanceof LargeSlidingDoorBlockEntity blockEntity) {
                blockEntity.setDoorProgress(0);
            }
        }

        if (state == LargeSlidingDoorBlock.State.OPEN) {
            if (level.getBlockEntity(pos) instanceof LargeSlidingDoorBlockEntity blockEntity) {
                blockEntity.setDoorProgress(1);
            }
        }
    }
}
