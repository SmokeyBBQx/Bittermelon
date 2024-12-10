package com.site21.bittermelon.medical.compartments.conditions;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Condition;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Scar extends Condition {
    public Scar(String name, Compartment owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentType.SCAR), name, owner, maxHealth, character, entity);
    }
}
