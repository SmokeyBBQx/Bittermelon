package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.damage.*;
import com.site21.bittermelon.medical.damage.generators.Bite;
import com.site21.bittermelon.medical.damage.generators.BluntForceTrauma;
import com.site21.bittermelon.medical.damage.generators.Lacerations;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

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
        Optional<Character> entityCharacterOpt = characterManager.getActiveCharacter(entity.getUUID());
        Optional<Character> targetCharacterOpt = characterManager.getActiveCharacter(target.getUUID());
        if (entityCharacterOpt.isEmpty() || targetCharacterOpt.isEmpty())
            return;

        Character entityCharacter = entityCharacterOpt.get();
        Character targetCharacter = targetCharacterOpt.get();

        attackTemplates.add(AttackTemplate.of(
                EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE),
                () -> new Bite(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE)),
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
        ));

        attackTemplates.add(AttackTemplate.of(
                EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE),
                () -> new Lacerations(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE)),
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
        ));

        attackTemplates.add(AttackTemplate.of(
                EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE),
                () -> new BluntForceTrauma(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE)),
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
                "%s delivers a crushing blow to %s's %s",
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
        ));

        // TODO: Weaker attacks

        Object[] array = attackTemplates.toArray();
        AttackTemplate selectedAttack = (AttackTemplate) array[new Random().nextInt(array.length)];
        DamageGenerator damageSequence = selectedAttack.damageSequenceSupplier().get();
        Optional<DamageResult> damageResultOpt = damageSequence.generateDamage(targetCharacter.getMedicalStats(), 2, 1, 6, 10, targetCharacter, target);

        damageResultOpt.ifPresent(damageResult ->
                LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(
                selectedAttack.getFormattedMessage(entityCharacter.getName(),
                        targetCharacter.getName(),
                        damageResult.targetBodyPart().getName().toLowerCase()
                ) + ", " + getInjuryDescription(damageResult) + "."))
        );

        target.hurt(entity.damageSources().mobAttack(entity), 0);
    }

    private @NotNull String getInjuryDescription(@NotNull DamageResult damageResult) {
        List<InjuryResult> injuryResults = damageResult.injuryResults();

        if (injuryResults.size() > 2) {
            InjuryResult lastResult = injuryResults.getLast();
            String targetName = lastResult.injury().getOwner().getName().toLowerCase();
            String action = lastResult.message().split(" ")[0];
            return String.format("the injury reaching through and %s the %s", action, targetName);
        } else if (injuryResults.size() > 1) {
            String injuryDescription = injuryResults.getFirst().message();
            for (InjuryResult injuryResult : injuryResults) {
                if (!Objects.equals(injuryDescription, injuryResult.message())) {
                    injuryDescription = injuryDescription + " and " + injuryResult.message();
                    break;
                }
            }
            return injuryDescription;
        } else {
            return injuryResults.getFirst().message();
        }
    }
}
