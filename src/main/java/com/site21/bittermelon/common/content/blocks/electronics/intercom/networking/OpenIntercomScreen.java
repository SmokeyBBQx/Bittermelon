package com.site21.bittermelon.common.content.blocks.electronics.intercom.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.gui.ScreenSetter;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.IntercomBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenIntercomScreen(BlockPos blockPos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenIntercomScreen> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_intercom_screen"));

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenIntercomScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenIntercomScreen::blockPos,
            OpenIntercomScreen::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().player == null) return;

        if (Minecraft.getInstance().player.level().getBlockEntity(blockPos) instanceof IntercomBlockEntity blockEntity) {
            ScreenSetter.displayIntercomScreen(blockEntity, true);
        }
    }
}
