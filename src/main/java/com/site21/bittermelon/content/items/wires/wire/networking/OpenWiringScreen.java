package com.site21.bittermelon.content.items.wires.wire.networking;

import com.site21.bittermelon.Bittermelon;
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

public record OpenWiringScreen() implements CustomPacketPayload {
    public static final Type<OpenWiringScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_wiring_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
    }
}
