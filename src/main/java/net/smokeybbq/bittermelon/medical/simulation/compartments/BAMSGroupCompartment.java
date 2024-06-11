package net.smokeybbq.bittermelon.medical.simulation.compartments;

import java.util.ArrayList;
import java.util.List;

//Bone Adipose Muscle Skin Group Compartment
public class BAMSGroupCompartment extends GroupCompartment {
    protected List<SimpleCompartment> compartments;

    public BAMSGroupCompartment(String name) {
        this.name = name;
        this.compartments = new ArrayList<SimpleCompartment>();
        this.compartments.add(new SimpleCompartment("Bone", 0 /*TO DO VOLUME*/));
        this.compartments.add(new SimpleCompartment("Skin", 0 /*TO DO VOLUME*/));
        this.compartments.add(new SimpleCompartment("Muscle", 0 /*TO DO VOLUME*/));
        this.compartments.add(new SimpleCompartment("Adipose Tissue", 0 /*TO DO VOLUME*/));
    }
}
