package com.site21.bittermelon.common.systems.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import net.minecraft.world.entity.LivingEntity;

public class ExternalBleed extends Bleed {
    public ExternalBleed(CompartmentTag bleedType, String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity, float bleedRate) {
        super(bleedType, name, owner, maxHealth, character, entity, bleedRate);
    }
//    public ExternalBleed(String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity) {
//        super(name, owner, maxHealth, character, entity, 0.2f);
//    }
//
//    @Override
//    public void onDeath(MedicalStatsOld mammalMedicalStats) {
//        super.onDeath(mammalMedicalStats);
//        CompartmentOld scab = new Scab(name + "scab", owner, maxHealth, character, entity);
//        mammalMedicalStats.addCompartment(scab);
//    }
}
