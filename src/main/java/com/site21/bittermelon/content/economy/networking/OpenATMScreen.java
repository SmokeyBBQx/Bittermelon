package com.site21.bittermelon.content.economy.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.ClientHandler;
import com.site21.bittermelon.content.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.content.personnel.registry.PersonnelRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenATMScreen(int userID) implements CustomPacketPayload {
    public static final Type<OpenATMScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_atm_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenATMScreen> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            OpenATMScreen::userID,
            OpenATMScreen::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PersonnelEntry entry = PersonnelRegistry.get(ctx.player().level()).getEntry(userID());
        if (entry != null) {
            ClientHandler.displayATMScreen(entry);
        }
    }
}
