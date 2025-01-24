package com.site21.bittermelon.medical.compartments.conditions;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.client.effects.ScreenshakeHandler;
import com.site21.bittermelon.medical.compartments.*;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.Random;

public class Pain extends Condition {

    public Pain(String name, Compartment owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentType.PAIN), name, owner, maxHealth, character, entity);
        attributes.put(FunctionType.FUNCTION, -maxHealth / 2);
        attributes.put(FunctionType.TREMOR, maxHealth);
        attributes.put(FunctionType.PAIN, maxHealth);
    }
}
