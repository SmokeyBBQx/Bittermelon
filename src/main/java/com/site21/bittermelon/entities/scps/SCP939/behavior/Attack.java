package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.damage.*;
import com.site21.bittermelon.medical.damage.generators.*;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.miscellaneous.stumble.StumbleHandler;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Attack extends AnimatableMeleeAttack<SCP939> {
    Set<AttackTemplate> attackTemplates = new HashSet<>();

    public Attack(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected void doDelayedAction(SCP939 entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.apply(entity));

        if (this.target == null)
            return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target))
            return;

        CharacterManager characterManager = CharacterManager.getInstance();
        Character entityCharacter = characterManager.getActiveCharacter(entity.getUUID());
        Character targetCharacter = characterManager.getActiveCharacter(target.getUUID());
        if (entityCharacter == null || targetCharacter == null)
            return;

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new BoneBreakingBite(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE)))
                .setArea(2)
                .setDepthRange(1, 6)
                .setDamage(10)
                .addModifier(MedicalStats::getMovement, 0.2f)
                .addModifier(MedicalStats::getBite, 0.7f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s sinks its fangs deep into %s's %s",
                        "%s snaps its jaws at %s's %s viciously",
                        "%s tears into %s's %s with razor-sharp teeth",
                        "%s chomps down on %s's %s with crushing force",
                        "%s lunges with open maw at %s's %s",
                        "%s clamps its jaws around %s's %s",
                        "%s gnashes its teeth into %s's %s",
                        "%s rips and tears at %s's %s with serrated fangs",
                        "%s mauls %s's %s with powerful jaws",
                        "%s bites down on %s's %s with bone-crushing force",
                        "%s savagely bites into %s's %s",
                        "%s's fangs pierce into %s's %s",
                        "%s latches onto %s's %s with its teeth",
                        "%s snaps its fangs at %s's %s",
                        "%s's jaws close around %s's %s with frightening speed",
                        "%s violently bites down on %s's %s",
                        "%s tries to take a chunk out of %s's %s",
                        "%s's teeth flash as it bites %s's %s",
                        "%s lunges with gnashing teeth at %s's %s",
                        "%s attempts to sink its teeth into %s's %s"
                )
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new Lacerations(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE)))
                .setArea(3)
                .setDepthRange(1, 6)
                .setDamage(10)
                .addModifier(MedicalStats::getMovement, 0.3f)
                .addModifier(MedicalStats::getManipulation, 0.6f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s rakes its claws across %s's %s",
                        "%s slashes viciously at %s's %s with razor claws",
                        "%s tears into %s's %s with deadly claws",
                        "%s swipes its massive claws at %s's %s",
                        "%s rips through %s's %s with sharp claws",
                        "%s slices at %s's %s with lethal precision",
                        "%s shreds at %s's %s with wicked claws",
                        "%s carves through %s's %s's defenses",
                        "%s slashes wildly at %s's %s",
                        "%s cleaves at %s's %s with savage claws",
                        "%s's claws flash as they slice toward %s's %s",
                        "%s tears viciously at %s's %s with hooked claws",
                        "%s launches a devastating slash at %s's %s",
                        "%s's claws cut through the air toward %s's %s",
                        "%s swipes with murderous intent at %s's %s",
                        "%s slashes with frightening speed at %s's %s",
                        "%s rends at %s's %s with cruel claws",
                        "%s's claws whistle through the air at %s's %s",
                        "%s unleashes a frenzied series of slashes at %s's %s"
                )
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new BluntForceTrauma(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE)))
                .setArea(4)
                .setDepthRange(1, 5)
                .setDamage(10)
                .addModifier(MedicalStats::getMovement, 0.7f)
                .addModifier(MedicalStats::getManipulation, 0.2f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s smashes into %s's %s with devastating force",
                        "%s rams full force into %s's %s",
                        "%s crashes down upon %s's %s",
                        "%s hammers %s's %s with bone-crushing strength",
                        "%s slams bodily into %s's %s",
                        "%s batters %s's %s with overwhelming power",
                        "%s thunders into %s's %s",
                        "%s crushes %s's %s with unstoppable momentum",
                        "%s pummels %s's %s with immense force",
                        "%s drives into %s's %s with crushing weight",
                        "%s pounds %s's %s with devastating impact",
                        "%s bulldozes into %s's %s mercilessly",
                        "%s batters %s's %s with tremendous force",
                        "%s delivers a crushing blow to %s's %s"
                )
                .setCondition((attacker, target) -> !StumbleHandler.containsUUID(target.getUUID())
                        && !StumbleHandler.containsUUID(attacker.getUUID()))
                .setSpecialAction((attacker, target) -> StumbleHandler.stumble(target))
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new Bite(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE)))
                .setArea(3)
                .setDepthRange(1, 5)
                .setDamage(8)
                .setPriority(4)
                .addModifier(MedicalStats::getMovement, 0.45f)
                .addModifier(MedicalStats::getManipulation, 0.45f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s violently yanks at %s's %s with its jaws",
                        "%s grabs and tears viciously at %s's %s",
                        "%s latches onto %s's %s and pulls with savage force",
                        "%s seizes %s's %s in its teeth and wrenches back",
                        "%s clamps down on %s's %s and thrashes wildly",
                        "%s grips %s's %s tightly and rips away",
                        "%s snags %s's %s and jerks its head violently",
                        "%s catches hold of %s's %s and tears brutally",
                        "%s locks its jaws on %s's %s and yanks hard",
                        "%s grabs %s's %s and shakes ferociously",
                        "%s clutches %s's %s in its teeth and pulls savagely",
                        "%s bites down on %s's %s and drags forcefully",
                        "%s seizes and wrenches at %s's %s ruthlessly",
                        "%s catches %s's %s and rips with terrifying strength",
                        "%s snaps onto %s's %s and pulls with brutal force",
                        "%s grabs hold of %s's %s and tears viciously",
                        "%s clamps onto %s's %s and yanks mercilessly",
                        "%s latches onto %s's %s and thrashes with deadly force",
                        "%s seizes %s's %s and pulls with crushing strength",
                        "%s grips %s's %s and tears with savage intensity"
                )
                .setCondition((attacker, target) -> StumbleHandler.containsUUID(target.getUUID()))
                .setSpecialAction((attacker, target) -> {
                    Vec3 pullDirection = entity.getLookAngle().multiply(-2, 1, -2);
                    target.setDeltaMovement(pullDirection);
                    target.hurtMarked = true;
                })
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(Stab::new)
                .setArea(2)
                .setDepthRange(5, 12)
                .setDamage(15)
                .setPriority(0.8f)
                .addModifier(MedicalStats::getMovement, 0.2f)
                .addModifier(MedicalStats::getManipulation, 0.6f)
                .addModifier(MedicalStats::getSight, 0.2f)
                .setMessages(
                        "%s drives its claws deep into %s's %s",
                        "%s punctures %s's %s with razor-sharp claws",
                        "%s sinks its claws into %s's %s with deadly force",
                        "%s impales %s's %s with wickedly sharp claws",
                        "%s pierces through %s's %s with cruel claws",
                        "%s plunges its claws straight into %s's %s",
                        "%s thrusts its claws into %s's %s viciously",
                        "%s drives its claws through %s's %s with brutal force",
                        "%s skewers %s's %s with razor-tipped claws",
                        "%s stabs its claws deep within %s's %s",
                        "%s gouges its claws into %s's %s",
                        "%s punctures straight through %s's %s with its claws",
                        "%s impales deeply into %s's %s with its claws",
                        "%s sinks its claws with frightening speed into %s's %s",
                        "%s plunges its cruel claws into %s's %s"
                )
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new BluntForceTrauma(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE)))
                .setArea(4)
                .setDepthRange(1, 5)
                .setDamage(10)
                .setPriority(4)
                .addModifier(MedicalStats::getMovement, 0.4f)
                .addModifier(MedicalStats::getManipulation, 0.5f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s stomps down on %s's %s",
                        "%s stomps heavily onto %s's %s",
                        "%s brings its foot down on %s's %s",
                        "%s stomps at %s's %s",
                        "%s steps down hard on %s's %s",
                        "%s stomps forcefully on %s's %s",
                        "%s drives its foot down on %s's %s",
                        "%s stomps powerfully onto %s's %s",
                        "%s brings its weight down on %s's %s",
                        "%s stomps straight down at %s's %s",
                        "%s slams its foot onto %s's %s",
                        "%s steps violently onto %s's %s",
                        "%s stomps directly on %s's %s",
                        "%s brings its foot crashing onto %s's %s",
                        "%s stomps swiftly at %s's %s",
                        "%s stomps hard on %s's %s",
                        "%s drives its weight onto %s's %s",
                        "%s stomps viciously at %s's %s",
                        "%s brings its foot heavily onto %s's %s",
                        "%s stomps ruthlessly on %s's %s"
                )
                .setCondition((attacker, target) -> StumbleHandler.containsUUID(target.getUUID())
                        && !StumbleHandler.containsUUID(attacker.getUUID()))
                .build()
        );

        // TODO: Weaker attacks

        List<AttackTemplate> validAttackTemplates = attackTemplates.stream()
                .filter(attackTemplate -> attackTemplate.canPerform(entity, target))
                .toList();
        AttackTemplate selectedAttack = selectAttack(validAttackTemplates);
        if (selectedAttack == null) return;

        float performance = selectedAttack.calculatePerformance(entityCharacter.getMedicalStats());
        if (performance <= 0) return;

        System.out.println(entityCharacter.getName() + " performance " + performance);
        DamageGenerator damageSequence = selectedAttack.damageGeneratorSupplier().get();
        DamageResult damageResult = damageSequence.generateDamage(targetCharacter.getMedicalStats(), selectedAttack.area(),
                selectedAttack.minDepth(), selectedAttack.maxDepth(), selectedAttack.damage(), performance, targetCharacter, target);

        if (damageResult != null) {
            LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(
                    selectedAttack.getFormattedMessage(entityCharacter.getName(),
                            targetCharacter.getName(),
                            damageResult.targetBodyPart().getName().toLowerCase()
                    ) + ", " + getInjuryDescription(damageResult, performance) + "."));

            selectedAttack.executeSpecialAction(entity, target);
        }

        target.hurt(entity.damageSources().mobAttack(entity), 0);
    }

    public static @Nullable AttackTemplate selectAttack(@NotNull List<AttackTemplate> attacks) {
        if (attacks.isEmpty()) {
            return null;
        }

        float random = new Random().nextFloat() * attacks.stream()
                .map(AttackTemplate::priority)
                .reduce(0f, Float::sum);

        float cumulative = 0;
        for (AttackTemplate attack : attacks) {
            cumulative += attack.priority();
            if (random <= cumulative) {
                return attack;
            }
        }

        return attacks.getLast();
    }

    private @NotNull String getInjuryDescription(@NotNull DamageResult damageResult, float performance) {
        List<InjuryResult> injuryResults = damageResult.injuryResults();

        if (injuryResults.size() > 2) {
            InjuryResult deepestResult = injuryResults.stream()
                    .max((a, b) -> {
                        int depthA = getCompartmentDepth(a.injury().getOwner());
                        int depthB = getCompartmentDepth(b.injury().getOwner());
                        return Integer.compare(depthA, depthB);
                    })
                    .orElse(injuryResults.getLast());

            String targetName = deepestResult.injury().getOwner().getName().toLowerCase();
            String action = deepestResult.message().split(" ")[0];
            return "the injury reaching through and " + getSeverityDescription(performance) +
                    String.format(" %s the %s", action, targetName);
        } else if (injuryResults.size() > 1) {
            String injuryDescription = injuryResults.getFirst().message();
            for (InjuryResult injuryResult : injuryResults) {
                if (!Objects.equals(injuryDescription, injuryResult.message())) {
                    injuryDescription = getSeverityDescription(performance) + " " + injuryDescription + " and " + injuryResult.message();
                    break;
                }
            }
            return injuryDescription;
        } else {
            return getSeverityDescription(performance) + " " + injuryResults.getFirst().message();
        }
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
