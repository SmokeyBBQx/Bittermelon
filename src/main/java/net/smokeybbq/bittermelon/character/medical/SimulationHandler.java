package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.conditions.Condition;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SimulationHandler {

    private List<PBPKModel> simulations = new ArrayList<>();
    private Character character;
    private Map<String, Compartment> compartmentMap;
    public SimulationHandler(Character character, Map<String, Compartment> compartmentMap) {
        this.character = character;
        this.compartmentMap = compartmentMap;
    }

    public void update() {
        for (PBPKModel simulation : simulations) {
            simulation.runSimulation();
        }

        drugEffect();
    }

    public double drugEffect() {
        List<Condition> conditions = character.getMedicalStats().getConditions();

        for (Condition condition : conditions) {
            String affectedArea = condition.getAffectedArea();
            Compartment compartment = compartmentMap.get(affectedArea);
            for (Substance substance : compartment.getConcentrations().keySet())
                if (Arrays.stream(condition.getSuitableTreatments()).anyMatch(substance.getName()::equals)) {
                    double effectiveness = calculateE(substance, compartment);
                    condition.treat(substance, effectiveness);
                }
        }
        return 0;
    }

    public double calculateE(Substance substance, Compartment compartment) {
        double concentration = compartment.getConcentration(substance);
        double EMax = substance.getEMax();
        double halfMaximalEffectiveConcentration = substance.getHalfMaximalEffectiveConcentration();

        return EMax * concentration / (EMax * halfMaximalEffectiveConcentration + concentration);
    }

    public void addSimulation(PBPKModel simulation) {
        simulations.add(simulation);
    }

    public void removeSimulation(PBPKModel simulation) {
        simulations.remove(simulation);
    }
}
