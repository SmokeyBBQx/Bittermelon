package net.smokeybbq.bittermelon.medical.symptoms;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.common.Severity;

import java.util.concurrent.ThreadLocalRandom;

public class Pain extends Symptom {
    private int ticksSinceLastReminder = 0;
    private int REMINDER_MINIMUM_TICKS = 400;
    private int REMINDER_MAXIMUM_TICKS = 600;

    public Pain(Character character, String affectedArea, float amplifier) {
        super(character, affectedArea, amplifier);
        this.REMINDER_MINIMUM_TICKS -= (int) (amplifier * 5);
        this.REMINDER_MAXIMUM_TICKS -= (int) (amplifier * 10);
    }

    @Override
    public void update() {
        ticksSinceLastReminder++;

        int TICKS_BETWEEN_REMINDERS = ThreadLocalRandom.current().nextInt(REMINDER_MINIMUM_TICKS, REMINDER_MAXIMUM_TICKS);

        if (ticksSinceLastReminder >= TICKS_BETWEEN_REMINDERS) {
            effects();
            ticksSinceLastReminder = 0;
        }
    }

    @Override
    public void initializeDescriptions() {
        MILD_DESCRIPTION = "";
        MODERATE_DESCRIPTION = "";
        SEVERE_DESCRIPTION = "";
        CRITICAL_DESCRIPTION = "";
        TERMINAL_DESCRIPTION = "";

        MILD_REMINDER = "You feel discomforting pain in your " + affectedArea;
        MODERATE_REMINDER = "You feel significant pain in your " + affectedArea;
        SEVERE_REMINDER = "You feel overwhelming pain in your " + affectedArea;
        CRITICAL_REMINDER = "You feel excruciating pain in your " + affectedArea;
        TERMINAL_REMINDER = "You feel unbearable and blinding pain in your " + affectedArea;

    }

    @Override
    public void effects() {
        if (severity != Severity.MILD && severity != Severity.MODERATE) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40 + (int) amplifier, (int) amplifier, false, true));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40 + (int) amplifier, (int) amplifier, false, true));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40 + (int) amplifier, (int) amplifier, false, true));
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40 + (int) amplifier, (int) amplifier, false, true));
        }

        if (entity instanceof ServerPlayer player) {
            player.sendSystemMessage(Component.literal(getReminder()).withStyle(ChatFormatting.RED));
        }
    }
}
