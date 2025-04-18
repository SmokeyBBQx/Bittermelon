package com.site21.bittermelon.content.medical.client.screen.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.client.screen.deprecated.HealthScreen;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UpdateHealthScreen(UUID characterID, MedicalStats medicalStats) implements CustomPacketPayload {
    public static final Type<UpdateHealthScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_health_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateHealthScreen> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            UpdateHealthScreen::characterID,
            MedicalStats.STREAM_CODEC,
            UpdateHealthScreen::medicalStats,
            UpdateHealthScreen::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().screen instanceof HealthScreen healthScreen) {
            if (healthScreen.getCharacter().getUUID().equals(characterID)) {
                healthScreen.setMedicalStats(medicalStats);
                healthScreen.refreshCompartmentList();
            }
        }
    }
}
