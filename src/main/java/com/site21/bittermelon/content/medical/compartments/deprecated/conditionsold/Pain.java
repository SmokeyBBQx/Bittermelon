package com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.MedicalAttribute;
import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.deprecated.ConditionOld;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Pain extends ConditionOld {

    public Pain(String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.PAIN), name, owner, maxHealth, character, entity);
        attributes.put(MedicalAttribute.FUNCTION, -maxHealth / 2);
        attributes.put(MedicalAttribute.TREMOR, maxHealth);
        attributes.put(MedicalAttribute.PAIN, maxHealth);
    }
}
