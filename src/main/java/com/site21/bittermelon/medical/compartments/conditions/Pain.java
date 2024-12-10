package com.site21.bittermelon.medical.compartments.conditions;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.client.effects.ScreenshakeHandler;
import com.site21.bittermelon.medical.compartments.*;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.Random;

public class Pain extends Condition implements HasReminder {
    private int ticksSinceLastReminder = 0;
    private int REMINDER_MINIMUM_TICKS = 800;
    private int REMINDER_MAXIMUM_TICKS = 1200;
    private final Random random = new Random();

    public Pain(String name, Compartment owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentType.PAIN), name, owner, maxHealth, character, entity);
        int minReduction = (int) (maxHealth * 5);
        int maxReduction = (int) (maxHealth * 10);
        REMINDER_MINIMUM_TICKS = Math.max(100, REMINDER_MINIMUM_TICKS - minReduction);
        REMINDER_MAXIMUM_TICKS = Math.max(REMINDER_MINIMUM_TICKS + 100, REMINDER_MAXIMUM_TICKS - maxReduction);
        attributes.put(FunctionType.FUNCTION, -maxHealth / 2);
        attributes.put(FunctionType.TREMOR, maxHealth);
    }

    @Override
    public void update(MedicalStats medicalStats) {
        super.update(medicalStats);
        ticksSinceLastReminder++;

        int TICKS_BETWEEN_REMINDERS = random.nextInt(REMINDER_MINIMUM_TICKS, REMINDER_MAXIMUM_TICKS);

        if (ticksSinceLastReminder >= TICKS_BETWEEN_REMINDERS) {
            effects();
            ticksSinceLastReminder = 0;
        }

        if (entity instanceof Player player) {
            ScreenshakeHandler.startScreenshake(player, 80, Math.min(0.8f, getHealth() / 10));
        }
    }

    @Override
    public void effects() {
        if (getSeverity() != Severity.MILD && getSeverity() != Severity.MODERATE) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40 + (int) getHealth(), (int) getHealth(), false, true));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40 + (int) getHealth(), (int) getHealth(), false, true));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40 + (int) getHealth(), (int) getHealth(), false, true));
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40 + (int) getHealth(), (int) getHealth(), false, true));
        }

        if (entity instanceof ServerPlayer player) {
            player.sendSystemMessage(Component.literal(getReminder()).withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public Severity getSeverity() {
        if (getHealth() <= 4) {
            return Severity.MILD;
        } else if (getHealth() > 4 && getHealth() <= 8) {
            return Severity.MODERATE;
        } else if (getHealth() > 8 && getHealth() <= 12) {
            return Severity.SEVERE;
        } else if (getHealth() > 12 && getHealth() <= 16) {
            return Severity.CRITICAL;
        } else {
            return Severity.TERMINAL;
        }
    }

    @Override
    public String getMildReminder() {
        return "You feel discomforting pain in your " + owner.getName();
    }

    @Override
    public String getModerateReminder() {
        return "You feel significant pain in your " + owner.getName();
    }

    @Override
    public String getSevereReminder() {
        return "You feel overwhelming pain in your " + owner.getName();
    }

    @Override
    public String getCriticalReminder() {
        return "You feel excruciating pain in your " + owner.getName();
    }

    @Override
    public String getTerminalReminder() {
        return "You feel unbearable and blinding pain in your " + owner.getName();
    }
}
