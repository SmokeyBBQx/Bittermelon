package com.site21.bittermelon.content.medical.compartments.conditions;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.compartments.Condition;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;
import java.util.UUID;

public class Infection extends Condition {
    public Infection(String name, Compartment owner, int maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentType.INFECTION), name, owner, maxHealth, character, entity);
    }

    public UUID getOrganism() {
        return UUID.randomUUID();
    }
}
