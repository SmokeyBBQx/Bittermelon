package com.site21.bittermelon.medical.compartments.conditions;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Injury;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Bruise extends Injury {
    public Bruise(String name, Compartment owner, int maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentType.BRUISE), name, owner, maxHealth, character, entity);
    }
}
