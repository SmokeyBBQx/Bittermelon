package com.site21.bittermelon.content.medical.mobeffects;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class Asphyxiation extends MobEffect {
    public Asphyxiation() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 200 == 0;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return true;
        entity.setAirSupply(entity.getAirSupply() - 2);

        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);

        if (character != null) {
            Component message = Component.literal(character.getName() + " gasps for air.")
                    .withColor(character.getEmoteColor());
            LocalMessageHelper.sendLocalMessage(entity, 10, message);
        }

        return true;
    }
}
