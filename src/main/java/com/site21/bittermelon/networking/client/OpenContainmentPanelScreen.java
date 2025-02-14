package com.site21.bittermelon.networking.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.blocks.blockentities.ContainmentPanelBlockEntity;
import com.site21.bittermelon.containment.client.ContainmentPanelScreen;
import com.site21.bittermelon.database.PersonnelEntry;
import com.site21.bittermelon.database.PersonnelRegistry;
import com.site21.bittermelon.economy.client.ATMScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record OpenContainmentPanelScreen(BlockPos blockPos) implements CustomPacketPayload {
    public static final Type<OpenContainmentPanelScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_containment_panel_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenContainmentPanelScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenContainmentPanelScreen::blockPos,
            OpenContainmentPanelScreen::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().player == null) return;

        if (Minecraft.getInstance().player.level().getBlockEntity(blockPos) instanceof ContainmentPanelBlockEntity blockEntity) {
            Minecraft.getInstance().setScreen(new ContainmentPanelScreen(blockEntity, false));
        }
    }
}
