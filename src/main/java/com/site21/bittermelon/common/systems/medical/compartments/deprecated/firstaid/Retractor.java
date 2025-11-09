package com.site21.bittermelon.common.systems.medical.compartments.deprecated.firstaid;

import com.site21.bittermelon.common.systems.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.medicalstats.deprecated.MedicalStatsOld;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class Retractor extends FirstAid {
    public Retractor(String name, CompartmentOld owner, int maxHealth, float quality, ItemStack item) {
        super(EnumSet.of(CompartmentTag.RETRACTOR), name, owner, maxHealth, quality);
        this.item = item;

        for (CompartmentOld compartment : owner.getChildren()) {
            compartment.reveal();
        }
    }

    @Override
    public void onDeath(MedicalStatsOld medicalStats) {
        super.onDeath(medicalStats);
        for (CompartmentOld child : owner.getChildren()) {
            child.setHidden(true);
        }
    }

    @Override
    public void onExtract(MedicalStatsOld medicalStats) {
        super.onExtract(medicalStats);
        for (CompartmentOld child : owner.getChildren()) {
            child.setHidden(true);
        }
    }
}
