package com.site21.bittermelon.common.content.items.wire.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.gui.ScreenHandler;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenWiringScreen(BlockPos pos, InteractionHand hand) implements CustomPacketPayload {
    public static final Type<OpenWiringScreen> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_wiring_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenWiringScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenWiringScreen::pos,
            ByteBufCodecs.BYTE.map(
                    b -> b == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND,
                    hand -> hand == InteractionHand.MAIN_HAND ? (byte)0 : (byte)1
            ),
            OpenWiringScreen::hand,
            OpenWiringScreen::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().player == null) return;

        if (Minecraft.getInstance().player.level().getBlockEntity(pos) instanceof ElectronicDevice blockEntity) {
            ScreenHandler.displayWiringScreen(blockEntity, hand);
        }
    }
}
