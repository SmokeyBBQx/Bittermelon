package com.site21.bittermelon.common.systems.medical.client.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.content.items.medical.MedicalItem;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record CompleteMinigame(ItemStack stack, UUID compartmentID, UUID characterID, UUID playerID,
                               float quality) implements CustomPacketPayload {
    public static final Type<CompleteMinigame> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "complete_minigame"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CompleteMinigame> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            CompleteMinigame::stack,
            UUIDUtil.STREAM_CODEC,
            CompleteMinigame::compartmentID,
            UUIDUtil.STREAM_CODEC,
            CompleteMinigame::characterID,
            UUIDUtil.STREAM_CODEC,
            CompleteMinigame::playerID,
            ByteBufCodecs.FLOAT,
            CompleteMinigame::quality,
            CompleteMinigame::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        MedicalStats medicalStats = CharacterManager.get(level).getCharacter(characterID).getMedicalStats();

        if (stack.getItem() instanceof MedicalItem medicalItem) {
            medicalItem.finishAction(medicalStats.getCompartment(compartmentID), medicalStats, quality, stack);
            medicalItem.consumeItem(stack, level.getPlayerByUUID(playerID));
        }

        if (ctx.player() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new UpdateHealthScreen(characterID, medicalStats));
        }
    }
}
