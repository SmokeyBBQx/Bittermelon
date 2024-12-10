package com.site21.bittermelon.medical.compartments.firstaid;

import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.Set;

import static com.site21.bittermelon.init.BitterItems.RETRACTOR;

public class Retractor extends FirstAid {
    public Retractor(String name, Compartment owner, int maxHealth, float quality, ItemStack item) {
        super(EnumSet.of(CompartmentType.RETRACTOR), name, owner, maxHealth, quality);
        this.item = item;

        for (Compartment compartment : owner.getChildren()) {
            compartment.reveal();
        }
    }

    @Override
    public void onDeath(MedicalStats medicalStats) {
        super.onDeath(medicalStats);
        for (Compartment child : owner.getChildren()) {
            child.setHidden(true);
        }
    }

    @Override
    public void onExtract(MedicalStats medicalStats) {
        super.onExtract(medicalStats);
        for (Compartment child : owner.getChildren()) {
            child.setHidden(true);
        }
    }
}
