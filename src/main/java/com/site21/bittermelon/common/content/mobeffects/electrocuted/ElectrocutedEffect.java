package com.site21.bittermelon.common.content.mobeffects.electrocuted;

import com.site21.bittermelon.common.content.mobeffects.electrocuted.networking.CutOffChat;
import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.ELECTROCUTED;

public class ElectrocutedEffect extends MobEffect {
    public ElectrocutedEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    public void onEffectStarted(@NotNull LivingEntity entity, int amplifier) {
        StumbleHandler.stumble(entity, entity.getEffect(ELECTROCUTED).getDuration(), entity.getLookAngle());
    }

    public void onEffectAdded(@NotNull LivingEntity entity, int amplifier) {
        super.onEffectAdded(entity, amplifier);

        if (entity instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new CutOffChat());
        }
    }
}
