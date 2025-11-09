package com.site21.bittermelon.common.content.items.wire.networking;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

public record WiringDataUpdate(BlockPos portPos, String portID, UUID playerID, InteractionHand hand) implements CustomPacketPayload {
    public static final Type<WiringDataUpdate> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "wiring_data_update"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, WiringDataUpdate> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            WiringDataUpdate::portPos,
            ByteBufCodecs.STRING_UTF8,
            WiringDataUpdate::portID,
            UUIDUtil.STREAM_CODEC,
            WiringDataUpdate::playerID,
            ByteBufCodecs.BYTE.map(
                    b -> b == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND,
                    hand -> hand == InteractionHand.MAIN_HAND ? (byte)0 : (byte)1
            ),
            WiringDataUpdate::hand,
            WiringDataUpdate::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Player player = ctx.player().level().getPlayerByUUID(playerID);
        if (player != null) {
            ItemStack wireItem = player.getItemInHand(hand);
            wireItem.set(CORD_CONNECTION, portPos);
            wireItem.set(PORT_ID, portID);
        }
    }
}
