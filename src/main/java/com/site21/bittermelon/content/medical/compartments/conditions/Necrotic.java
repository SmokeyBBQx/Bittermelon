package com.site21.bittermelon.content.medical.compartments.conditions;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.compartments.Injury;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class Necrotic extends Injury {
    public Necrotic(String name, Compartment owner, int maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentType.NECROTIC), name, owner, maxHealth, character, entity);
    }

    @Override
    public void update(MedicalStats medicalStats) {
        super.update(medicalStats);
        owner.modifyHealth(-0.01f);
        modifyHealth(0.01f);
    }
}
