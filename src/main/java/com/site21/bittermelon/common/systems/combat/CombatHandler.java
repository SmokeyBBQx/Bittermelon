package com.site21.bittermelon.common.systems.combat;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.damage.DamageResult;
import com.site21.bittermelon.common.systems.medical.damage.InjuryResult;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class CombatHandler {
    public record AttackResult(boolean hit, DamageResult damageResult, String message) {
    }

    public static void handleAttack(LivingEntity attacker, LivingEntity target, AttackTemplate attackTemplate) {
        if (target == null || attacker.level().isClientSide) return;

        CharacterManager characterManager = CharacterManager.get(attacker.level());
        Character attackerCharacter = characterManager.getActiveCharacter(attacker);
        Character targetCharacter = characterManager.getActiveCharacter(target);

        if (attackerCharacter == null || targetCharacter == null) return;

        AttackResult result = executeAttack(
                attackerCharacter,
                targetCharacter,
                attackTemplate,
                attacker,
                target
        );

        if (result == null) return;

        sendCombatMessages(result, attacker, target, attackerCharacter, targetCharacter, result.hit());

        playAttackSound(attacker.getOnPos(), attackTemplate.sound(), attacker);
    }

    private static void sendCombatMessages(@NotNull AttackResult result, Entity attacker, Entity target, @NotNull Character attackerCharacter, @NotNull Character targetCharacter, boolean hit) {
        int textColor = attackerCharacter.getEmoteColor();
        LocalMessageHelper.sendLocalMessage(attacker, 10, Component.literal(result.message()).withColor(textColor));

        if (hit && result.damageResult() != null) {
            for (String amputationMessage : getAmputationMessages(targetCharacter, result.damageResult())) {
                LocalMessageHelper.sendLocalMessage(target, 10, Component.literal(amputationMessage).withColor(0XD41313));
            }
        }
    }


    public static @Nullable AttackResult executeAttack(
            @NotNull Character attacker,
            Character target,
            @NotNull AttackTemplate attackTemplate,
            LivingEntity attackerEntity,
            LivingEntity targetEntity) {
        float performance = attackTemplate.calculatePerformance(attacker.getMedicalStats());
        if (performance <= 0) return null;

        DamageResult damageResult = attackTemplate.damageGeneratorSupplier().get().generateDamage(
                target.getMedicalStats(),
                attackTemplate.area(),
                attackTemplate.minDepth(),
                attackTemplate.maxDepth(),
                attackTemplate.damage(),
                performance,
                target,
                targetEntity);

        if (damageResult == null) return null;

        String targetBodyPart = damageResult.targetBodyPart().getName().toLowerCase();

        if (attackerEntity.getRandom().nextFloat() > performance) {
            String missMessage = formatMissMessage(attacker, target, targetBodyPart);
            return new AttackResult(false, null, missMessage);
        }

        String hitMessage = formatHitMessage(attacker, target, attackTemplate, damageResult, performance, targetBodyPart);
        attackTemplate.executeSpecialAction(attackerEntity, targetEntity);

        // Apply minimal damage to trigger Minecraft's damage visuals
        targetEntity.hurt(attackerEntity.damageSources().mobAttack(attackerEntity), 0.1f);
        targetEntity.heal(0.1f);

        return new AttackResult(true, damageResult, hitMessage);
    }

    public static void playAttackSound(BlockPos pos, SoundEvent sound, Entity entity) {
        if (sound != null) {
            entity.level().playSound(null, pos, sound, SoundSource.AMBIENT);
        }
    }

    private static @NotNull String formatMissMessage(@NotNull Character attacker, @NotNull Character target, String targetBodyPart) {
        return String.format("%s attempts to strike %s's %s but misses.",
                attacker.getName(), target.getName(), targetBodyPart);
    }

    private static @Nullable String formatHitMessage(
            Character attacker,
            Character target,
            AttackTemplate attack,
            DamageResult damageResult,
            float performance,
            String targetBodyPart
    ) {
        String injuryDescription = getInjuryDescription(damageResult, performance, target.getMedicalStats());
        if (injuryDescription == null) return null;

        return attack.getFormattedMessage(attacker.getName(), target.getName(), targetBodyPart)
                + ", " + injuryDescription + ".";
    }

    public static List<String> getAmputationMessages(Character target, @NotNull DamageResult damageResult) {
        return damageResult.injuryResults().stream()
//                .filter(result -> result.injury().hasTag(CompartmentTag.TRAUMATIC_AMPUTATION))
                .map(result -> target.getName() + "'s " + result.message())
                .toList();
    }

    private static @Nullable String getInjuryDescription(@NotNull DamageResult damageResult, float performance, MedicalStats medicalStats) {
        List<InjuryResult> injuryResults = damageResult.injuryResults().stream()
//                .filter(result -> !result.injury().hasTag(CompartmentTag.TRAUMATIC_AMPUTATION))
                .toList();

        if (injuryResults.isEmpty()) return null;

        if (injuryResults.size() > 2) {
            return formatMultipleInjuries(injuryResults, performance, medicalStats);
        } else if (injuryResults.size() > 1) {
            return formatTwoInjuries(injuryResults, performance);
        } else {
            return getSeverityDescription(performance) + " " + injuryResults.getFirst().message();
        }
    }

    private static @NotNull String formatMultipleInjuries(@NotNull List<InjuryResult> injuryResults, float performance, MedicalStats medicalStats) {
//        InjuryResult deepestResult = injuryResults.stream()
//                .max(Comparator.comparingInt(a -> getCompartmentDepth(a.injury().getParent(medicalStats), medicalStats)))
//                .orElse(injuryResults.getLast());
//
//        String targetName = deepestResult.injury().getName().toLowerCase();
//        if (deepestResult.injury().getParent(medicalStats) != null) {
//            targetName = deepestResult.injury().getParent(medicalStats).getName().toLowerCase();
//        }
//        String action = deepestResult.message().split(" ")[0];
//
//        return String.format("the injury reaching through and %s %s the %s",
//                getSeverityDescription(performance), action, targetName);

        return null;
    }

    private static @NotNull String formatTwoInjuries(@NotNull List<InjuryResult> injuryResults, float performance) {
        String firstInjury = injuryResults.getFirst().message();

        for (InjuryResult result : injuryResults) {
            if (!Objects.equals(firstInjury, result.message())) {
                return getSeverityDescription(performance) + " " + firstInjury + " and " + result.message();
            }
        }

        return getSeverityDescription(performance) + " " + firstInjury;
    }

    private static int getCompartmentDepth(CompartmentInstance compartment, MedicalStats medicalStats) {
//        int depth = 0;
//        CompartmentInstance current = compartment;
//        if (current == null) return 0;
//
//        while (current.getParent(medicalStats) != null) {
//            depth++;
//            current = current.getParent(medicalStats);
//        }
//        return depth;

        return 0;
    }

    @Contract(pure = true)
    private static @NotNull String getSeverityDescription(float performance) {
        if (performance <= 0.2f) return "weakly";
        else if (performance <= 0.4f) return "slightly";
        else if (performance <= 0.6f) return "moderately";
        else if (performance <= 0.8f) return "viciously";
        else if (performance <= 1f) return "brutally";
        else return "devastatingly";
    }
}
