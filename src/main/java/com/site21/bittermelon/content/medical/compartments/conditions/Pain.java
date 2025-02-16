package com.site21.bittermelon.content.medical.compartments.conditions;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.compartments.Condition;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Pain extends Condition {

    public Pain(String name, Compartment owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentType.PAIN), name, owner, maxHealth, character, entity);
        attributes.put(FunctionType.FUNCTION, -maxHealth / 2);
        attributes.put(FunctionType.TREMOR, maxHealth);
        attributes.put(FunctionType.PAIN, maxHealth);
    }
}
