package com.site21.bittermelon.common.content.items.wirecutters.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.gui.ScreenHandler;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenWireCutterScreen(BlockPos pos) implements CustomPacketPayload {
    public static final Type<OpenWireCutterScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_wire_cutter_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenWireCutterScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenWireCutterScreen::pos,
            OpenWireCutterScreen::new
    );

    public void handle(IPayloadContext ctx) {
        if (Minecraft.getInstance().player == null) return;

        if (Minecraft.getInstance().player.level().getBlockEntity(pos) instanceof ElectronicDevice blockEntity) {
            ScreenHandler.displayWireCutterScreen(blockEntity);
        }
    }
}
