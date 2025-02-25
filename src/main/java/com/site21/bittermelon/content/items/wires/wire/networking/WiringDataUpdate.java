package com.site21.bittermelon.content.items.wires.wire.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.atmosphere.networking.AtmosChunkUpdate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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

public record WiringDataUpdate(BlockPos portPos, String portID, ItemStack wireItem) implements CustomPacketPayload {
    public static final Type<WiringDataUpdate> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "wiring_data_update"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, WiringDataUpdate> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            WiringDataUpdate::portPos,
            ByteBufCodecs.STRING_UTF8,
            WiringDataUpdate::portID,
            ItemStack.STREAM_CODEC,
            WiringDataUpdate::wireItem,
            WiringDataUpdate::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        wireItem.set(CORD_CONNECTION, portPos);
        wireItem.set(PORT_ID, portID);
    }
}
