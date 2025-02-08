package com.site21.bittermelon.medical.compartments.conditions;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Condition;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.substance.Substance;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class ForeignSubstance extends Condition {
    private final Substance substance;

    public ForeignSubstance(String name, Compartment owner, int maxHealth, Substance substance, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentType.FOREIGN_SUBSTANCE), name, owner, maxHealth, character, entity);
        this.substance = substance;
    }

    @Override
    public void update(MedicalStats medicalStats) {
        super.update(medicalStats);
        // TODO: Substance effect
    }
}
