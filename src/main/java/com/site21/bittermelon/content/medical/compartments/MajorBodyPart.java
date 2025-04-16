package com.site21.bittermelon.content.medical.compartments;

import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Consumer;

public class MajorBodyPart extends Compartment {
    public MajorBodyPart(String id, EnumSet<CompartmentTag> defaultTags) {
        super(id, defaultTags, compartmentInstance -> {});
    }

    @Override
    public void tick(MedicalStats medicalStats, @NotNull CompartmentInstance instance) {

    }
}
