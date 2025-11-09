package com.site21.bittermelon.common.systems.medical.compartments.deprecated;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.compartments.MedicalAttribute;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class InjuryOld extends ConditionOld {
    public InjuryOld(EnumSet<CompartmentTag> types, String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity) {
        super(types, name, owner, maxHealth, character, entity);
        attributes.put(MedicalAttribute.FUNCTION, -maxHealth);
//        character.getMedicalStats().addCompartment(new PainEffect("PainEffect", this, maxHealth, character, entity));
    }
}
