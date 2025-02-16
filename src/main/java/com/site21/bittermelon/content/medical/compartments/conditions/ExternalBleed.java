package com.site21.bittermelon.content.medical.compartments.conditions;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import net.minecraft.world.entity.LivingEntity;

public class ExternalBleed extends Bleed {
    public ExternalBleed(CompartmentType bleedType, String name, Compartment owner, float maxHealth, Character character, LivingEntity entity, float bleedRate) {
        super(bleedType, name, owner, maxHealth, character, entity, bleedRate);
    }
//    public ExternalBleed(String name, Compartment owner, float maxHealth, Character character, LivingEntity entity) {
//        super(name, owner, maxHealth, character, entity, 0.2f);
//    }
//
//    @Override
//    public void onDeath(MedicalStats mammalMedicalStats) {
//        super.onDeath(mammalMedicalStats);
//        Compartment scab = new Scab(name + "scab", owner, maxHealth, character, entity);
//        mammalMedicalStats.addCompartment(scab);
//    }
}
