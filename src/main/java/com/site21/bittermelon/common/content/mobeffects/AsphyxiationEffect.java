package com.site21.bittermelon.common.content.mobeffects;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class AsphyxiationEffect extends MobEffect {
    public AsphyxiationEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 200 == 0;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return true;
        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);

        if (character != null) {
            Component message = Component.literal(character.getName() + " gasps for air.")
                    .withColor(character.getEmoteColor());
            LocalMessageHelper.sendLocalMessage(entity, 10, message);
        }

        return true;
    }
}
