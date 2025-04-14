package com.site21.bittermelon.content.medical.compartments.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.Condition;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Scar extends Condition {
    public Scar(String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.SCAR), name, owner, maxHealth, character, entity);
    }
}
