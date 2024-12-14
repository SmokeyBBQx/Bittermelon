package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.conditions.Cut;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Attack extends AnimatableMeleeAttack<SCP939> {
    Set<AttackSequence> attackSequences = new HashSet<>();
    Character targetCharacter;

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

        attackSequences.add(new AttackSequence("bites", Set.of(CompartmentType.SOFT_TISSUE)));

        Object[] array = attackSequences.toArray();
        AttackSequence selectedAttack = (AttackSequence) array[new Random().nextInt(array.length)];

        Set<Compartment> validCompartments = new HashSet<>();
        for (Compartment compartment : targetCharacter.getMedicalStats().getCompartments()) {
            if (compartment.getTypes().stream().anyMatch(type -> selectedAttack.targets().contains(type))) {
                validCompartments.add(compartment);
            }
        }

        if (!validCompartments.isEmpty()) {
            Object[] compartmentArray = validCompartments.toArray();
            Compartment targetCompartment = (Compartment) compartmentArray[new Random().nextInt(compartmentArray.length)];
            targetCharacter.getMedicalStats().addCompartment(new Cut("Cut", targetCompartment, 20, targetCharacter, target));
        }

        LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(
                entityCharacter.getName() + selectedAttack.message() + targetCharacter.getName() + "."));
    }
}
