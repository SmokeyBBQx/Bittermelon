package net.smokeybbq.bittermelon.systems.medical.simulation.pbpk;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.systems.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.systems.medical.compartments.CompartmentTag;
import net.smokeybbq.bittermelon.systems.medical.compartments.GroupCompartment;
import net.smokeybbq.bittermelon.systems.medical.substance.Substance;

public class OralAdministration extends PBPKModel {
    Compartment liver, circulatory;
    GroupCompartment GI;

    public OralAdministration(float dosage, Character character, Substance drug) {
        super(dosage, character, drug);
        initializeSimulation();
    }

    @Override
    protected void initializeSimulation() {
        GI = new GroupCompartment("gastrointestinal", 1, 1, 1);

        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                if (compartment.hasTag(CompartmentTag.GASTROINTESTINAL)) {
                    GI.addSubCompartment(compartment);
                }
            });
        }

        liver = compartments.get("abdomen").getCompartment("liver").getMainCompartment();
        circulatory = compartments.get("circulatory_system").getMainCompartment();

        GI.updateConcentration(substance, dosage);
        GI.setExcludeFromCirculation(true);

        totalConcentration = getTotalConcentration();
    }

    @Override
    public void simulation() {
        GI.moveConcentration(liver, substance, substance.getAbsorptionRateConstant(), timeStep);
        liver.eliminateConcentration(substance, substance.getMetabolismRateConstant());
        liver.moveConcentration(circulatory, substance, liver.getBloodFlow(), timeStep);

        handleSimpleCompartments();
    }
}
