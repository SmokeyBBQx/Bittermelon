package com.site21.bittermelon.medical.compartments.conditions;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.Injury;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

import static com.site21.bittermelon.medical.compartments.conditions.Bleed.generateBleed;

public class Cut extends Injury {
    public Cut(String name, Compartment owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentType.CUT), name, owner, maxHealth, character, entity);

        generateBleed(this, character, entity, maxHealth);
        character.getMedicalStats().addCompartment(new Pain("Pain", this, maxHealth, character, entity));
        // TODO: Figure out a way to refresh health screen
    }

    @Override
    public void onDeath(MedicalStats medicalStats) {
        Compartment scar = new Scar(name + "scar", owner, maxHealth, character, entity);
        scar.reveal();
        medicalStats.addCompartment(scar);
        super.onDeath(medicalStats);
    }
}
