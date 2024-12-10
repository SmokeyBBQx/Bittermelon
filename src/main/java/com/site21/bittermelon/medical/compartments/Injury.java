package com.site21.bittermelon.medical.compartments;

import com.site21.bittermelon.character.Character;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;
import java.util.Set;

public class Injury extends Condition {
    public Injury(EnumSet<CompartmentType> types, String name, Compartment owner, float maxHealth, Character character, LivingEntity entity) {
        super(types, name, owner, maxHealth, character, entity);
        attributes.put(FunctionType.FUNCTION, -maxHealth);
    }
}
