package com.site21.bittermelon.content.items.wires.wire.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.ClientHandler;
import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.client.IntercomScreen;
import com.site21.bittermelon.content.items.wires.wire.client.WiringScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

public record OpenWiringScreen(BlockPos pos, ItemStack wireStack) implements CustomPacketPayload {
    public static final Type<OpenWiringScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_wiring_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenWiringScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenWiringScreen::pos,
            ItemStack.STREAM_CODEC,
            OpenWiringScreen::wireStack,
            OpenWiringScreen::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().player == null) return;

        if (Minecraft.getInstance().player.level().getBlockEntity(pos) instanceof IElectronic blockEntity) {
            ClientHandler.displayWiringScreen(blockEntity, wireStack);
        }
    }
}
