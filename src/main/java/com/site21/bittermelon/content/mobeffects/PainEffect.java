package com.site21.bittermelon.content.mobeffects;

import com.site21.bittermelon.systems.character.Character;
import com.site21.bittermelon.systems.character.CharacterManager;
import com.site21.bittermelon.systems.ai.behavior.misc.FeelsPain;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class PainEffect extends MobEffect {
    public PainEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % (300 - amplifier * 10) == 0;
    }

    @Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity entity, int amplifier) {
        float painChance = entity.getRandom().nextFloat();

        if (painChance < 0.5) return false;

        if (amplifier > 3) {
            int duration = amplifier * 2;

            entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration,
                    amplifier, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration,
                    amplifier, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, duration,
                    amplifier, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration,
                    amplifier, false, false));
        }

        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);

        if (character != null) {
            Component message = Component.literal(character.getName() + " " + ((FeelsPain) entity)
                    .getPainMessage(amplifier)).withColor(character.getEmoteColor());
            LocalMessageHelper.sendLocalMessage(entity, 10, message);

            entity.level().playSound(null, entity.getOnPos(), ((FeelsPain) entity)
                    .getPainSound(amplifier), SoundSource.AMBIENT);
        }

        return true;
    }
}
