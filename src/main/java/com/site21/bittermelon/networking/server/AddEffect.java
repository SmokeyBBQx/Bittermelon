package com.site21.bittermelon.networking.server;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AddEffect(MobEffectInstance instance, int entityID) implements CustomPacketPayload {
    public static final Type<AddEffect> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "add_effect"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, AddEffect> STREAM_CODEC = StreamCodec.composite(
            MobEffectInstance.STREAM_CODEC,
            AddEffect::instance,
            ByteBufCodecs.INT,
            AddEffect::entityID,
            AddEffect::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getEntity(entityID) instanceof LivingEntity entity) {
            entity.addEffect(instance);
        }
    }
}
