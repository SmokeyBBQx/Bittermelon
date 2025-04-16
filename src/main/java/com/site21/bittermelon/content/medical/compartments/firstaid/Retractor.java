package com.site21.bittermelon.content.medical.compartments.firstaid;

import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Consumer;

public class Retractor extends Compartment {
    public Retractor(String id, EnumSet<CompartmentTag> defaultTags) {
        super(id, defaultTags, compartmentInstance -> {});
    }

    @Override
    public void onExtract(MedicalStats medicalStats, CompartmentInstance instance) {
        super.onExtract(medicalStats, instance);

        for (UUID childID : instance.getParent(medicalStats).getChildren()) {
            CompartmentInstance child = medicalStats.getCompartment(childID);
            if (child != null) {
                child.setHidden(true);
            }
        }
    }

    @Override
    public boolean canExtract(CompartmentInstance instance, MedicalStats medicalStats) {
        return true;
    }
}
