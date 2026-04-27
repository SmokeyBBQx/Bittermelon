package com.site21.bittermelon.common.systems.medical.medicalstats;

import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;

import java.util.UUID;

public class MedicalStatsUtil {

    /**
     * Retrieves a limb compartment instance from MedicalStats using the limb name.
     * @param partName The name of the body part/model part.
     * @param stats The target's MedicalStats.
     * @return The CompartmentInstance corresponding to the specified body part.
     */
    public static CompartmentInstance getBodyPart(String partName, MedicalStats stats) {
        UUID limbId = stats.getAnatomyModel().getBodyParts().get(partName);
        CompartmentInstance limb = stats.getCompartment(limbId);

        if (limb == null) {
            throw new IllegalArgumentException("Limb " + partName + " does not exist in the provided MedicalStats.");
        }

        return limb;
    }
}
