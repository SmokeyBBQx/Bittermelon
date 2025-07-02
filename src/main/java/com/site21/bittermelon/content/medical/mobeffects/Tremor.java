package com.site21.bittermelon.content.medical.mobeffects;

import com.site21.bittermelon.client.visualeffects.screenshake.StartScreenshake;
import com.site21.bittermelon.content.medical.client.screen.networking.UpdateTremor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.TREMOR;

public class Tremor extends InstantenousMobEffect {
    public Tremor() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return true;

        int duration = entity.getEffect(TREMOR).getDuration();

        if (entity instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new StartScreenshake(duration, Math.min(0.8f, (float) amplifier / 10)));
            PacketDistributor.sendToPlayer(player, new UpdateTremor(amplifier));
        }

        return true;
    }
}
