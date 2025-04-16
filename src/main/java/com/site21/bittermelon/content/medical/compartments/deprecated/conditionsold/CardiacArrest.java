package com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.deprecated.InjuryOld;
import com.site21.bittermelon.content.medical.compartments.deprecated.organs.HeartRhythm;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class CardiacArrest extends InjuryOld {

    public CardiacArrest(String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity, HeartRhythm heartRhythm) {
        super(EnumSet.of(CompartmentTag.CARDIAC_ARREST), name, owner, maxHealth, character, entity);

    }
}
