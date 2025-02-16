package com.site21.bittermelon.content.medical.compartments;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.conditions.Pain;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Injury extends Condition {
    public Injury(EnumSet<CompartmentType> types, String name, Compartment owner, float maxHealth, Character character, LivingEntity entity) {
        super(types, name, owner, maxHealth, character, entity);
        attributes.put(FunctionType.FUNCTION, -maxHealth);
        character.getMedicalStats().addCompartment(new Pain("Pain", this, maxHealth, character, entity));
    }
}
