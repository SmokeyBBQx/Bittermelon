package com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdateSlidingDoorProgress(float doorProgress, BlockPos pos) implements CustomPacketPayload {
    public static final Type<UpdateSlidingDoorProgress> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_sliding_door_progress"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, UpdateSlidingDoorProgress> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            UpdateSlidingDoorProgress::doorProgress,
            BlockPos.STREAM_CODEC,
            UpdateSlidingDoorProgress::pos,
            UpdateSlidingDoorProgress::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        if (level.getBlockEntity(pos) instanceof LargeSlidingDoorBlockEntity blockEntity) {
            blockEntity.setDoorProgress(doorProgress);
        }
    }
}
