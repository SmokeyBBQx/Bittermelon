package com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.deprecated.ConditionOld;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Pain extends ConditionOld {

    public Pain(String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.PAIN), name, owner, maxHealth, character, entity);
        attributes.put(FunctionType.FUNCTION, -maxHealth / 2);
        attributes.put(FunctionType.TREMOR, maxHealth);
        attributes.put(FunctionType.PAIN, maxHealth);
    }
}
