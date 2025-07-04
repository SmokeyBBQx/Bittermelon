package com.site21.bittermelon.content.effects.electrocuted;

import com.site21.bittermelon.content.effects.electrocuted.networking.CutOffChat;
import com.site21.bittermelon.content.entities.ai.behavior.misc.FeelsPain;
import com.site21.bittermelon.content.stumble.StumbleHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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

        if (entity instanceof FeelsPain feelsPain) {
            entity.level().playSound(null, entity.getOnPos(), feelsPain.getPainSound(amplifier), SoundSource.AMBIENT);
        }
    }

    public void onEffectAdded(@NotNull LivingEntity entity, int amplifier) {
        super.onEffectAdded(entity, amplifier);

        if (entity instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new CutOffChat());
        }
    }
}
