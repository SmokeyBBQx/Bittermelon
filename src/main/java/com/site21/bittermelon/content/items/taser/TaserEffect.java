package com.site21.bittermelon.content.items.taser;

import com.site21.bittermelon.content.mobeffects.electrocuted.ElectrocutedEffect;
import com.site21.bittermelon.content.mobeffects.electrocuted.networking.CutOffChat;
import com.site21.bittermelon.content.entities.ai.behavior.misc.FeelsPain;
import com.site21.bittermelon.content.stumble.StumbleHandler;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.TASERED;

public class TaserEffect extends ElectrocutedEffect {
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.tickCount % 19 == 0) {
            entity.level().playSound(null, entity.getOnPos(), BitterSounds.TASER.get(), SoundSource.PLAYERS, 0.05f, 1);
        }
        return true;
    }

    @Override
    public void onEffectStarted(@NotNull LivingEntity entity, int amplifier) {
        StumbleHandler.stumble(entity, entity.getEffect(TASERED).getDuration(), entity.getLookAngle());

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
