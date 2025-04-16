package com.site21.bittermelon.content.medical.compartments;

import com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold.Cut;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Consumer;

public class BodyPart extends Compartment {
    public BodyPart(String id, EnumSet<CompartmentTag> defaultTags, Consumer<CompartmentInstance> attributeInitializer) {
        super(id, defaultTags, attributeInitializer);
    }

    @Override
    public boolean canExtract(@NotNull CompartmentInstance instance, MedicalStats medicalStats) {
        for (UUID childID : instance.getChildren()) {
            CompartmentInstance child = medicalStats.getCompartment(childID);
            if (child != null) {
                if (child.hasTag(CompartmentTag.CUT)) {
                    return true;
                }
            }
        }
        return false;
    }
}
