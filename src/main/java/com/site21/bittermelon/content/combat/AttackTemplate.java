package com.site21.bittermelon.content.combat;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.damage.DamageGenerator;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

public class AttackTemplate {
    private final EnumSet<CompartmentType> allowedCompartments;
    private final Supplier<DamageGenerator> damageGeneratorSupplier;
    private final int area;
    private final float damage;
    private final int minDepth;
    private final int maxDepth;
    private final float priority;
    private final List<Pair<Function<MedicalStats, Float>, Float>> performanceModifiers;
    private final String[] messages;
    private final BiPredicate<LivingEntity, LivingEntity> condition;
    private final BiConsumer<LivingEntity, LivingEntity> specialAction;
    private final SoundEvent sound;

    @Contract(pure = true)
    public AttackTemplate(@NotNull AttackTemplateBuilder builder) {
        this.allowedCompartments = builder.allowedCompartments;
        this.damageGeneratorSupplier = builder.damageGeneratorSupplier;
        this.area = builder.area;
        this.damage = builder.damage;
        this.minDepth = builder.minDepth;
        this.maxDepth = builder.maxDepth;
        this.priority = builder.priority;
        this.performanceModifiers = builder.performanceModifiers;
        this.messages = builder.messages;
        this.condition = builder.condition;
        this.specialAction = builder.specialAction;
        this.sound = builder.sound;;
    }

    public float calculatePerformance(MedicalStats stats) {
        if (performanceModifiers.isEmpty()) return 1f;

        float totalWeight = 0f;
        float weightedSum = 0f;

        for (var modifier : performanceModifiers) {
            float modifierValue = modifier.getFirst().apply(stats);
            float weight = modifier.getSecond();
            weightedSum += modifierValue * weight;
            totalWeight += weight;
        }

        return weightedSum / totalWeight;
    }


    public String getFormattedMessage(String attacker, String target, String bodyPart) {
        return String.format(messages[new Random().nextInt(messages.length)],
                attacker, target, bodyPart);
    }

    public boolean canPerform(LivingEntity attacker, LivingEntity target) {
        return condition == null || condition.test(attacker, target);
    }

    public void executeSpecialAction(LivingEntity attacker, LivingEntity target) {
        if (specialAction != null) {
            specialAction.accept(attacker, target);
        }
    }

    public EnumSet<CompartmentType> allowedCompartments() {
        return allowedCompartments;
    }

    public Supplier<DamageGenerator> damageGeneratorSupplier() {
        return damageGeneratorSupplier;
    }

    public int area() {
        return area;
    }

    public float damage() {
        return damage;
    }

    public int minDepth() {
        return minDepth;
    }

    public int maxDepth() {
        return maxDepth;
    }

    public BiPredicate<LivingEntity, LivingEntity> condition() {
        return condition;
    }

    public float priority() {
        return priority;
    }

    public SoundEvent sound() {
        return sound;
    }

    public static class AttackTemplateBuilder {
        private EnumSet<CompartmentType> allowedCompartments = EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE);
        private Supplier<DamageGenerator> damageGeneratorSupplier;
        private int area = 2;
        private float damage = 5;
        private int minDepth = 1;
        private int maxDepth = 2;
        private float priority = 1;
        private final List<Pair<Function<MedicalStats, Float>, Float>> performanceModifiers = new ArrayList<>();
        private String[] messages;
        private BiPredicate<LivingEntity, LivingEntity> condition = (attacker, target) -> true;
        private BiConsumer<LivingEntity, LivingEntity> specialAction;
        private SoundEvent sound;

        public AttackTemplateBuilder setAllowedCompartments(EnumSet<CompartmentType> allowedCompartments) {
            this.allowedCompartments = allowedCompartments;
            return this;
        }

        public AttackTemplateBuilder setDamageSupplier(Supplier<DamageGenerator> supplier) {
            this.damageGeneratorSupplier = supplier;
            return this;
        }

        public AttackTemplateBuilder setArea(int area) {
            this.area = area;
            return this;
        }

        public AttackTemplateBuilder setDamage(float damage) {
            this.damage = damage;
            return this;
        }

        public AttackTemplateBuilder setDepthRange(int min, int max) {
            this.minDepth = min;
            this.maxDepth = max;
            return this;
        }

        public AttackTemplateBuilder setPriority(float priority) {
            this.priority = priority;
            return this;
        }

        public AttackTemplateBuilder setMessages(String... messages) {
            this.messages = messages;
            return this;
        }

        public AttackTemplateBuilder addModifier(Function<MedicalStats, Float> modifier, float weight) {
            performanceModifiers.add(new Pair<>(modifier, weight));
            return this;
        }

        public AttackTemplateBuilder setCondition(BiPredicate<LivingEntity, LivingEntity> condition) {
            this.condition = condition;
            return this;
        }

        public AttackTemplateBuilder setSpecialAction(BiConsumer<LivingEntity, LivingEntity> action) {
            this.specialAction = action;
            return this;
        }

        public AttackTemplateBuilder setSound(SoundEvent sound) {
            this.sound = sound;
            return this;
        }

        public AttackTemplate build() {
            if (damageGeneratorSupplier == null || messages == null || messages.length == 0) {
                throw new IllegalStateException("Required fields not set");
            }
            return new AttackTemplate(this);
        }
    }
}
