package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;

//Bone Adipose Muscle Skin Group Compartment
public class BodyPartGroupCompartment extends GroupCompartment {
    public BodyPartGroupCompartment(String name, MedicalStats medicalStats) {
        super(name, medicalStats);
    }

    @Override
    public void initializeCompartments() {
        compartments.put("Bone", new SimpleCompartment("Bone", medicalStats));
        compartments.put("Skin", new SimpleCompartment("Skin", medicalStats));
        compartments.put("Muscle", new SimpleCompartment("Muscle", medicalStats));
        compartments.put("Adipose Tissue", new SimpleCompartment("Adipose Tissue", medicalStats));
        compartments.put("Sinews", new SimpleCompartment("Sinews", medicalStats));
        compartments.put("Nerves", new SimpleCompartment("Nerves", medicalStats));
    }
}
