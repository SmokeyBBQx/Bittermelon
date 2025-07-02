package com.site21.bittermelon.content.medical.client.screen.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.client.screen.deprecated.HealthScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UpdateCompartmentHealth(UUID characterID, UUID compartmentID, float healthDamage, float maxHealthDamage) implements CustomPacketPayload {
    public static final Type<UpdateCompartmentHealth> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_compartment_health"));

    public static final StreamCodec<ByteBuf, UpdateCompartmentHealth> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            UpdateCompartmentHealth::characterID,
            UUIDUtil.STREAM_CODEC,
            UpdateCompartmentHealth::compartmentID,
            ByteBufCodecs.FLOAT,
            UpdateCompartmentHealth::healthDamage,
            ByteBufCodecs.FLOAT,
            UpdateCompartmentHealth::maxHealthDamage,
            UpdateCompartmentHealth::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().screen instanceof HealthScreen healthScreen) {
//            if (healthScreen.getCharacter().getUUID() == characterID) {
//                healthScreen.getMedicalStats().getCompartment(compartmentID).modifyHealth(healthDamage);
//                healthScreen.getMedicalStats().getCompartment(compartmentID).modifyMaxHealth(maxHealthDamage);
//            }
        }
    }
}
