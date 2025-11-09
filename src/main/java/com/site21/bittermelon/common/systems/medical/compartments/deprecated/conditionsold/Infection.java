package com.site21.bittermelon.common.systems.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.ConditionOld;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;
import java.util.UUID;

public class Infection extends ConditionOld {
    public Infection(String name, CompartmentOld owner, int maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.INFECTION), name, owner, maxHealth, character, entity);
    }

    public UUID getOrganism() {
        return UUID.randomUUID();
    }
}
