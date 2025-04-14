package com.site21.bittermelon.content.medical.compartments.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.Condition;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;
import java.util.UUID;

public class Infection extends Condition {
    public Infection(String name, CompartmentOld owner, int maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.INFECTION), name, owner, maxHealth, character, entity);
    }

    public UUID getOrganism() {
        return UUID.randomUUID();
    }
}
