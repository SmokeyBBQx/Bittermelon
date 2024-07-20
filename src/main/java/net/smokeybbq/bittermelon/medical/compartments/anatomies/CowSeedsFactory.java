package net.smokeybbq.bittermelon.medical.compartments.anatomies;

import net.smokeybbq.bittermelon.medical.compartments.Compartment;

import java.util.HashMap;
import java.util.Map;

public class CowSeedsFactory {
    public static Map<String, Compartment> createCompartments() {
        Map<String, Compartment> compartments = new HashMap<>();

//        GroupCompartment head = new GroupCompartment("head" ,0.1F);
//        head.addSubCompartment(new SingleGroupCompartment("brain", 0.1F));
//        head.addSubCompartment(new SingleGroupCompartment("plant_matter", 0.1F));
//        head.addSubCompartment(new SingleGroupCompartment("root_tongue", 0.1F));
//        SingleGroupCompartment headStem = new SingleGroupCompartment("head_stem", 0.1F);
//        headStem.addTag(CompartmentTag.IMMUNE);
//        head.addSubCompartment(headStem);

        return compartments;
    }
}
