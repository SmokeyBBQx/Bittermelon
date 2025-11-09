package com.site21.bittermelon.common.systems.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.ConditionOld;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Scab extends ConditionOld {
    public Scab(String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.SCAB), name, owner, maxHealth, character, entity);
    }
}
