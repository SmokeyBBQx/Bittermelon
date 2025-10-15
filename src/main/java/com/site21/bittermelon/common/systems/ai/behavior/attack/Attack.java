package com.site21.bittermelon.common.systems.ai.behavior.attack;

import com.site21.bittermelon.common.systems.combat.AttackTemplate;
import com.site21.bittermelon.common.systems.combat.CombatHandler;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class Attack<E extends Mob> extends AnimatableMeleeAttack<E> {
    private final List<AttackTemplate> attackTemplates;

    public Attack(int delayTicks, List<AttackTemplate> attackTemplates) {
        super(delayTicks);
        this.attackTemplates = attackTemplates;
    }

    @Override
    protected void doDelayedAction(E entity) {
        BrainUtil.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.applyAsInt(entity));

        if (this.target == null) return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target)) return;

        CombatHandler.handleAttack(entity, target, selectAttack(entity));
    }

    private @Nullable AttackTemplate selectAttack(E entity) {
        List<AttackTemplate> validAttacks = attackTemplates.stream()
                .filter(template -> template.canPerform(entity, target))
                .toList();

        if (validAttacks.isEmpty()) return null;

        float random = new Random().nextFloat() * validAttacks.stream()
                .map(AttackTemplate::priority)
                .reduce(0f, Float::sum);

        float cumulative = 0;
        for (AttackTemplate attack : validAttacks) {
            cumulative += attack.priority();
            if (random <= cumulative) {
                return attack;
            }
        }

        return validAttacks.getLast();
    }
}