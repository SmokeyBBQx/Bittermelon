package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.CompartmentType;
import net.smokeybbq.bittermelon.character.medical.MedicalStats;

public class OrganGroupCompartment extends GroupCompartment {

    Compartment mainOrgan;
    public OrganGroupCompartment(String name, MedicalStats medicalStats, Compartment mainOrgan) {
        super(name, medicalStats);
        this.mainOrgan = mainOrgan;
        compartments.put(name, mainOrgan);
    }

    @Override
    public void initializeCompartments() {
        compartments.put(name, mainOrgan);
    }

    @Override
    public Compartment getMainOrgan() {
        return mainOrgan;
    }
}
