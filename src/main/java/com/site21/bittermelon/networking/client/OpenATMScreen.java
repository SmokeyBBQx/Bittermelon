package com.site21.bittermelon.networking.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.database.PersonnelEntry;
import com.site21.bittermelon.database.PersonnelRegistry;
import com.site21.bittermelon.economy.screen.ATMScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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

    public void handle(IPayloadContext ctx) {
        PersonnelEntry entry = PersonnelRegistry.getInstance().getData(userID);
        if (entry != null) {
            Minecraft.getInstance().setScreen(new ATMScreen(entry));
        }
    }
}
