package com.site21.bittermelon.content.medical.compartments.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.Injury;
import com.site21.bittermelon.content.medical.compartments.organs.HeartRhythm;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class CardiacArrest extends Injury  {

    public CardiacArrest(String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity, HeartRhythm heartRhythm) {
        super(EnumSet.of(CompartmentTag.CARDIAC_ARREST), name, owner, maxHealth, character, entity);

    }
}
