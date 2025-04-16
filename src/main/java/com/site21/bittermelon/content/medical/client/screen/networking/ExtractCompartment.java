package com.site21.bittermelon.content.medical.client.screen.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.items.medical.MedicalItem;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ExtractCompartment(UUID compartmentID, UUID characterID, UUID playerID) implements CustomPacketPayload {
    public static final Type<ExtractCompartment> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "extract_compartment"));

    public static final StreamCodec<ByteBuf, ExtractCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ExtractCompartment::compartmentID,
            UUIDUtil.STREAM_CODEC,
            ExtractCompartment::characterID,
            UUIDUtil.STREAM_CODEC,
            ExtractCompartment::playerID,
            ExtractCompartment::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        MedicalStats medicalStats = CharacterManager.get(level).getCharacter(characterID).getMedicalStats();

        CompartmentInstance compartment = medicalStats.getCompartment(compartmentID);
        Player player = level.getPlayerByUUID(playerID);
        medicalStats.extractCompartment(compartment);
        if (player != null) {
            player.getInventory().add(compartment.getItem().copy());
        }

        if (ctx.player() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new UpdateHealthScreen(characterID));
        }
    }
}
