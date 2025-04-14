package com.site21.bittermelon.content.medical.compartments;

import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.UUID;

public class MajorBodyPart extends Compartment {
    public MajorBodyPart(String id, EnumSet<CompartmentTag> defaultTags) {
        super(id, defaultTags);
    }

    @Override
    public void tick(MedicalStats medicalStats, @NotNull CompartmentInstance instance) {
        float functionMultiplier = 1;
        float childrenTotalMaxHealth = 0;
        for (UUID childID : instance.getChildren()) {
            CompartmentInstance child = medicalStats.getCompartment(childID);
            instance.setHealth(Math.max(0, Math.min(instance.getMaxHealth(), instance.getHealth() +
                    child.getAttribute(FunctionType.DAMAGE))));
            functionMultiplier *= child.getAttribute(FunctionType.FUNCTION);
            if (child.hasTag(CompartmentTag.BODY_PART)) {
                childrenTotalMaxHealth += child.getTrueMaxHealth();
            }
        }
        instance.updateFunction(functionMultiplier);
    }
}
