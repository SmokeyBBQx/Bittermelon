package com.site21.bittermelon.combat;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.client.effects.ScreenshakeHandler;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.damage.DamageResult;
import com.site21.bittermelon.medical.damage.InjuryResult;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CombatHandler {
    public record AttackResult(boolean hit, DamageResult damageResult, String message) {}

    public static void handleAttack(LivingEntity attacker, LivingEntity target, AttackTemplate attackTemplate) {
        if (target == null) return;

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
            @NotNull com.site21.bittermelon.character.Character attacker,
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
        String injuryDescription = getInjuryDescription(damageResult, performance);
        if (injuryDescription == null) return null;

        return attack.getFormattedMessage(attacker.getName(), target.getName(), targetBodyPart)
                + ", " + injuryDescription + ".";
    }

    public static List<String> getAmputationMessages(Character target, @NotNull DamageResult damageResult) {
        return damageResult.injuryResults().stream()
                .filter(result -> result.injury().hasType(CompartmentType.TRAUMATIC_AMPUTATION))
                .map(result -> target.getName() + "'s " + result.message())
                .toList();
    }

    private static @Nullable String getInjuryDescription(@NotNull DamageResult damageResult, float performance) {
        List<InjuryResult> injuryResults = damageResult.injuryResults().stream()
                .filter(result -> !result.injury().hasType(CompartmentType.TRAUMATIC_AMPUTATION))
                .toList();

        if (injuryResults.isEmpty()) return null;

        if (injuryResults.size() > 2) {
            return formatMultipleInjuries(injuryResults, performance);
        } else if (injuryResults.size() > 1) {
            return formatTwoInjuries(injuryResults, performance);
        } else {
            return getSeverityDescription(performance) + " " + injuryResults.getFirst().message();
        }
    }

    private static @NotNull String formatMultipleInjuries(@NotNull List<InjuryResult> injuryResults, float performance) {
        InjuryResult deepestResult = injuryResults.stream()
                .max(Comparator.comparingInt(a -> getCompartmentDepth(a.injury().getOwner())))
                .orElse(injuryResults.getLast());

        String targetName = deepestResult.injury().getOwner().getName().toLowerCase();
        String action = deepestResult.message().split(" ")[0];

        return String.format("the injury reaching through and %s %s the %s",
                getSeverityDescription(performance), action, targetName);
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

    private static int getCompartmentDepth(Compartment compartment) {
        int depth = 0;
        Compartment current = compartment;
        while (current.getOwner() != null) {
            depth++;
            current = current.getOwner();
        }
        return depth;
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
