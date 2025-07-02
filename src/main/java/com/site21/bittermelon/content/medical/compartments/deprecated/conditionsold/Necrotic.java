package com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.deprecated.InjuryOld;
import com.site21.bittermelon.content.medical.medicalstats.deprecated.MedicalStatsOld;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Necrotic extends InjuryOld {
    public Necrotic(String name, CompartmentOld owner, int maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.NECROTIC), name, owner, maxHealth, character, entity);
    }

    @Override
    public void update(MedicalStatsOld medicalStats) {
        super.update(medicalStats);
        owner.modifyHealth(-0.01f);
        modifyHealth(0.01f);
    }
}
