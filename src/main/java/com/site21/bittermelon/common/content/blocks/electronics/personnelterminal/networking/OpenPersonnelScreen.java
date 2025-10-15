package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.gui.ScreenHandler;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.PersonnelTerminalBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenPersonnelScreen(BlockPos pos) implements CustomPacketPayload {
    public static final Type<OpenPersonnelScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_personnel_screen"));

    public static final StreamCodec<ByteBuf, OpenPersonnelScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenPersonnelScreen::pos,
            OpenPersonnelScreen::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(pos) instanceof PersonnelTerminalBlockEntity terminalBlockEntity) {
            ScreenHandler.displayPersonnelScreen(terminalBlockEntity);
        }
    }
}