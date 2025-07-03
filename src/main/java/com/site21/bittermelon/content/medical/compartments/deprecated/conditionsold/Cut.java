package com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.deprecated.InjuryOld;
import com.site21.bittermelon.content.medical.medicalstats.deprecated.MedicalStatsOld;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold.Bleed.generateBleed;

public class Cut extends InjuryOld {
    public Cut(String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity) {
        super(EnumSet.of(CompartmentTag.CUT), name, owner, maxHealth, character, entity);

        generateBleed(this, character, entity, maxHealth);
//        character.getMedicalStats().addCompartment(new PainEffect("PainEffect", this, maxHealth, character, entity));
        // TODO: Figure out a way to refresh health screen
    }

    @Override
    public void onDeath(@NotNull MedicalStatsOld medicalStats) {
        CompartmentOld scar = new Scar(name + "scar", owner, maxHealth, character, entity);
        scar.reveal();
        medicalStats.addCompartment(scar);
        super.onDeath(medicalStats);
    }
}
