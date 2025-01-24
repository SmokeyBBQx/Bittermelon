package com.site21.bittermelon.medical.compartments.conditions;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Injury;
import com.site21.bittermelon.medical.compartments.organs.HeartRhythm;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class CardiacArrest extends Injury  {

    public CardiacArrest(String name, Compartment owner, float maxHealth, Character character, LivingEntity entity, HeartRhythm heartRhythm) {
        super(EnumSet.of(CompartmentType.CARDIAC_ARREST), name, owner, maxHealth, character, entity);

    }
}
