package com.site21.bittermelon.content.blocks.electronics.intercom.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.electronics.intercom.IntercomBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record IntercomTargetUpdate(String targetID, BlockPos pos) implements CustomPacketPayload {
    public static final Type<IntercomTargetUpdate> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "intercom_target_update"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, IntercomTargetUpdate> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            IntercomTargetUpdate::targetID,
            BlockPos.STREAM_CODEC,
            IntercomTargetUpdate::pos,
            IntercomTargetUpdate::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        if (level.getBlockEntity(pos()) instanceof IntercomBlockEntity blockEntity) {
            blockEntity.setTargetID(targetID());
        }
    }
}
