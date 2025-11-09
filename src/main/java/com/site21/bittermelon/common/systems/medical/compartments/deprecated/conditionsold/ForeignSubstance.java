package com.site21.bittermelon.common.systems.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.ConditionOld;
import com.site21.bittermelon.common.systems.medical.medicalstats.deprecated.MedicalStatsOld;
import com.site21.bittermelon.common.systems.substance.Substance;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class ForeignSubstance extends ConditionOld {
    private final Substance substance;

    public ForeignSubstance(String name, CompartmentOld owner, int maxHealth, Substance substance, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.FOREIGN_SUBSTANCE), name, owner, maxHealth, character, entity);
        this.substance = substance;
    }

    @Override
    public void update(MedicalStatsOld medicalStats) {
        super.update(medicalStats);
        // TODO: Substance effect
    }
}
