package com.site21.bittermelon.content.medical.compartments.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.Injury;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Necrotic extends Injury {
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
