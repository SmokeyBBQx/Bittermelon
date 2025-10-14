package com.site21.bittermelon.content.items.writablepaper.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.ClientHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenPaperEditScreen(ItemStack paper) implements CustomPacketPayload {
    public static final Type<OpenPaperEditScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_paper_edit_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenPaperEditScreen> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            OpenPaperEditScreen::paper,
            OpenPaperEditScreen::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        ClientHandler.displayPaperEditScreen(paper);
    }
}
