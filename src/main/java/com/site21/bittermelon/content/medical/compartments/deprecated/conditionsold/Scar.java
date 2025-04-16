package com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.deprecated.ConditionOld;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Scar extends ConditionOld {
    public Scar(String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.SCAR), name, owner, maxHealth, character, entity);
    }
}
