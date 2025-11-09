package com.site21.bittermelon.common.systems.medical.client.screen.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record OpenHealthScreenC2S(UUID playerUUID, UUID hitEntityUUID) implements CustomPacketPayload {
    public static final Type<OpenHealthScreenC2S> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_health_screen_c2s"));

    public static final StreamCodec<ByteBuf, OpenHealthScreenC2S> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            OpenHealthScreenC2S::playerUUID,
            UUIDUtil.STREAM_CODEC,
            OpenHealthScreenC2S::hitEntityUUID,
            OpenHealthScreenC2S::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();

        if (level instanceof ServerLevel serverLevel) {
            Character targetCharacter = null;
            Entity hitEntity = serverLevel.getEntities().get(hitEntityUUID);
            Entity playerEntity = serverLevel.getEntities().get(playerUUID);

            if (hitEntity != null) {
                targetCharacter = CharacterManager.get(level).getActiveCharacter(hitEntity);
            } else if (playerEntity != null) {
                targetCharacter = CharacterManager.get(level).getActiveCharacter(playerEntity);
            }

            if (targetCharacter != null && ctx.player() instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new OpenHealthScreenS2C(targetCharacter, serverPlayer.getMainHandItem()));
            }
        }
    }
}
