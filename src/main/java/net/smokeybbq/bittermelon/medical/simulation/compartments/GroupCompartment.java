package net.smokeybbq.bittermelon.medical.simulation.compartments;

import java.util.List;

public abstract class GroupCompartment {

    protected List<SimpleCompartment> compartments;
    protected String name;

    protected double getAverageHealth() {
        if (compartments == null || compartments.isEmpty()) {
            return 0;
        }

        double totalHealth = 0;
        for (SimpleCompartment compartment : compartments) {
            totalHealth += compartment.getHealth();
        }

        return totalHealth / compartments.size();
    }
}
