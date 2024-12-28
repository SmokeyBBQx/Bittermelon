package com.site21.bittermelon.entities.behavior.attack;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.damage.*;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.site21.bittermelon.init.BitterSounds.STAB;

public class Attack<E extends Mob> extends AnimatableMeleeAttack<E> {
    private final List<AttackTemplate> attackTemplates;

    public Attack(int delayTicks, List<AttackTemplate> attackTemplates) {
        super(delayTicks);
        this.attackTemplates = attackTemplates;
    }

    @Override
    protected void doDelayedAction(E entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.apply(entity));

        if (this.target == null) return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target)) return;

        CharacterManager characterManager = CharacterManager.getInstance();
        Character entityCharacter = characterManager.getActiveCharacter(entity.getUUID());
        Character targetCharacter = characterManager.getActiveCharacter(target.getUUID());

        if (entityCharacter == null || targetCharacter == null) return;

        AttackTemplate selectedAttack = selectAttack(entity);
        if (selectedAttack == null) return;

        executeAttack(entity, entityCharacter, targetCharacter, selectedAttack);
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

    private void executeAttack(E entity, @NotNull Character attackerCharacter, Character targetCharacter, @NotNull AttackTemplate selectedAttack) {
        float performance = selectedAttack.calculatePerformance(attackerCharacter.getMedicalStats());
        if (performance <= 0) {
            return;
        }

        DamageResult damageResult = selectedAttack.damageGeneratorSupplier().get().generateDamage(
                targetCharacter.getMedicalStats(),
                selectedAttack.area(),
                selectedAttack.minDepth(),
                selectedAttack.maxDepth(),
                selectedAttack.damage(),
                performance,
                targetCharacter,
                target);

        if (damageResult == null) return;

        String targetBodyPart = damageResult.targetBodyPart().getName().toLowerCase();
        int textColor = attackerCharacter.getEmoteColor();

        if (entity.getRandom().nextFloat() > performance) {
            sendMissMessage(attackerCharacter, targetCharacter, targetBodyPart, textColor);
            entity.level().playSound(null, entity.getOnPos(), STAB.get(), SoundSource.AMBIENT);
            return;
        }

        sendHitMessages(entity, attackerCharacter, targetCharacter, selectedAttack, damageResult, performance,
                targetBodyPart, textColor);
        selectedAttack.executeSpecialAction(entity, target);

        if (selectedAttack.sound() != null) {
            entity.level().playSound(null, entity.getOnPos(), selectedAttack.sound(), SoundSource.AMBIENT);
        }

        assert this.target != null;
        this.target.hurt(entity.damageSources().mobAttack(entity), 0.1f);
        this.target.heal(0.1f);
    }

    private void sendMissMessage(@NotNull Character attackerCharacter, @NotNull Character targetCharacter, String targetBodyPart, int textColor) {
        String missMessage = String.format("%s attempts to strike %s's %s but misses.", attackerCharacter.getName(),
                targetCharacter.getName(), targetBodyPart);
        assert target != null;
        LocalMessageHelper.sendLocalMessage(target, 10, Component.literal(missMessage).withColor(textColor));
    }

    private void sendHitMessages(E entity, Character attacker, Character target, AttackTemplate attack, DamageResult damageResult, float performance, String targetBodyPart, int textColor) {
        String injuryDescription = getInjuryDescription(damageResult, performance);
        if (injuryDescription != null) {
            String hitMessage = attack.getFormattedMessage(attacker.getName(), target.getName(), targetBodyPart)
                    + ", " + injuryDescription + ".";
            LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(hitMessage).withColor(textColor));
        }

        sendAmputationMessages(target, damageResult);
    }

    private void sendAmputationMessages(Character targetCharacter, @NotNull DamageResult damageResult) {
        for (InjuryResult injuryResult : damageResult.injuryResults()) {
            if (injuryResult.injury().hasType(CompartmentType.TRAUMATIC_AMPUTATION)) {
                String amputationMessage = targetCharacter.getName() + "'s " + injuryResult.message();
                assert target != null;
                LocalMessageHelper.sendLocalMessage(target, 10, Component.literal(amputationMessage)
                        .withColor(0XD41313));
            }
        }
    }

    private @Nullable String getInjuryDescription(@NotNull DamageResult damageResult, float performance) {
        List<InjuryResult> injuryResults = damageResult.injuryResults().stream()
                .filter(result -> !result.injury().hasType(CompartmentType.TRAUMATIC_AMPUTATION))
                .toList();

        if (injuryResults.isEmpty()) {
            return null;
        }

        if (injuryResults.size() > 2) {
            return formatMultipleInjuries(injuryResults, performance);
        } else if (injuryResults.size() > 1) {
            return formatTwoInjuries(injuryResults, performance);
        } else {
            return getSeverityDescription(performance) + " " + injuryResults.getFirst().message();
        }
    }

    private @NotNull String formatMultipleInjuries(@NotNull List<InjuryResult> injuryResults, float performance) {
        InjuryResult deepestResult = injuryResults.stream()
                .max((a, b) -> Integer.compare(
                        getCompartmentDepth(a.injury().getOwner()),
                        getCompartmentDepth(b.injury().getOwner())
                ))
                .orElse(injuryResults.getLast());

        String targetName = deepestResult.injury().getOwner().getName().toLowerCase();
        String action = deepestResult.message().split(" ")[0];

        return String.format("the injury reaching through and %s %s the %s",
                getSeverityDescription(performance), action, targetName);
    }

    private @NotNull String formatTwoInjuries(@NotNull List<InjuryResult> injuryResults, float performance) {
        String firstInjury = injuryResults.getFirst().message();

        for (InjuryResult result : injuryResults) {
            if (!Objects.equals(firstInjury, result.message())) {
                return getSeverityDescription(performance) + " " + firstInjury + " and " + result.message();
            }
        }

        return getSeverityDescription(performance) + " " + firstInjury;
    }

    private int getCompartmentDepth(Compartment compartment) {
        int depth = 0;
        Compartment current = compartment;
        while (current.getOwner() != null) {
            depth++;
            current = current.getOwner();
        }
        return depth;
    }

    @Contract(pure = true)
    private @NotNull String getSeverityDescription(float performance) {
        if (performance <= 0.2f) {
            return "weakly";
        } else if (performance <= 0.4f) {
            return "slightly";
        } else if (performance <= 0.6f) {
            return "moderately";
        } else if (performance <= 0.8f) {
            return "viciously";
        } else if (performance <= 1f) {
            return "brutally";
        } else {
            return "devastatingly";
        }
    }
}
