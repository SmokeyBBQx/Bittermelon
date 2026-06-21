package com.site21.bittermelon.common.systems.telecomms.intercom.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.telecomms.intercom.IntercomManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record SyncIntercomList(Map<BlockPos, String> intercomList) implements CustomPacketPayload {
    public static final Type<SyncIntercomList> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "sync_intercom_list"));

    public static final StreamCodec<ByteBuf, Map<BlockPos, String>> INTERCOM_MAP_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    BlockPos.STREAM_CODEC,
                    ByteBufCodecs.STRING_UTF8
            );

    public static final StreamCodec<ByteBuf, SyncIntercomList> STREAM_CODEC = StreamCodec.composite(
            INTERCOM_MAP_CODEC,
            SyncIntercomList::intercomList,
            SyncIntercomList::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
       IntercomManager.get(ctx.player().level()).updateAllFromServer(intercomList);
    }
}