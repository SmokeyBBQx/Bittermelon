package net.smokeybbq.bittermelon.medical.simulation.compartments;

import java.util.ArrayList;
import java.util.List;

//Bone Adipose Muscle Skin Group Compartment
public class BAMSGroupCompartment extends GroupCompartment {
    protected List<SimpleCompartment> compartments;

    public BAMSGroupCompartment(String name) {
        this.name = name;
        this.compartments = new ArrayList<SimpleCompartment>();
        this.compartments.add(new SimpleCompartment("bone", 0 /*TO DO VOLUME*/));
        this.compartments.add(new SimpleCompartment("skin", 0 /*TO DO VOLUME*/));
        this.compartments.add(new SimpleCompartment("muscle", 0 /*TO DO VOLUME*/));
        this.compartments.add(new SimpleCompartment("adipose", 0 /*TO DO VOLUME*/));
    }
}
