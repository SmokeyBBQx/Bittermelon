package net.smokeybbq.bittermelon.medical.simulation;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.compartments.CompartmentTag;
import net.smokeybbq.bittermelon.medical.simulation.pbpk.PBPKModel;
import net.smokeybbq.bittermelon.medical.simulation.pbpk.SimpleAdministration;
import net.smokeybbq.bittermelon.medical.substance.ImmuneResponse;
import net.smokeybbq.bittermelon.medical.substance.Substance;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImmuneSimulationHandler extends SimulationHandler {
    // Immunity and inflammatory constants
    private static final float INFLAMMATORY_DECAY = -0.1F;
    private static final float INFLAMMATORY_RESPONSE = 0.5F;
    private static final float IMMUNE_ACTIVATION = 0.01F;
    private static final float RESERVE_CAPACITY = 1000;
    private static final float IMMUNITY_MAXIMUM = 1000;
    private static final float SUPPRESSION_DECAY = -0.1F;
    private float immuneSuppression = 0;
    private float immuneReserve;
    Set<Compartment> immuneCompartments = new HashSet<>();
    PBPKModel immuneSystemSimulation;

    public ImmuneSimulationHandler(Character character, Map<String, Compartment> compartments) {
        super(character, compartments);
    }

    @Override
    protected void initialize() {
        immuneSystemSimulation = new SimpleAdministration(1000, character, new ImmuneResponse("Immune Response", 0.001F), compartments.get("circulatory_system").getMainCompartment());
        addSimulation(immuneSystemSimulation);

        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                if (compartment.hasTag(CompartmentTag.IMMUNE)) {
                    immuneCompartments.add(compartment.getMainCompartment());
                }
            });
        }
    }

    @Override
    public void update() {
        for (PBPKModel simulation : simulations) {
            simulation.runSimulation();
        }

        modifyImmuneSuppression(SUPPRESSION_DECAY);
        decay();
        substanceEffect();
        growImmuneReserve();
    }

    private void decay() {
        for (Compartment outerCompartment : compartments.values()) {
            outerCompartment.traverseCompartments(compartment -> {
                compartment.modifyInflammation(INFLAMMATORY_DECAY);
            });
        }
    }

    @Override
    protected void toxicDamage(Substance substance, Compartment compartment) {
        float toxicDamage = substance.getToxicDamage(compartment) * compartment.getConcentration(substance);
        compartment.modifyHealth(toxicDamage);

        if (toxicDamage >= 0.5 && immuneSystemSimulation.getTotalConcentration() < IMMUNITY_MAXIMUM) {
            immuneResponse(compartment);
        }
    }

    private float activateImmuneReserve() {
        float activationAmount = IMMUNE_ACTIVATION * (1 - immuneSuppression);
        float actualActivation = Math.min(activationAmount, immuneReserve);
        immuneReserve -= actualActivation;
        return actualActivation;
    }

    private void growImmuneReserve() {
        if (immuneReserve < RESERVE_CAPACITY) {
            for (Compartment compartment : immuneCompartments) {
                immuneReserve += compartment.getHealth() / 10;
            }
        }
    }

    private void immuneResponse(Compartment compartment) {
        Compartment circulatorySystem = compartments.get("circulatory_system");
        for (Map.Entry<Substance, Float> entry : circulatorySystem.getMainCompartment().getConcentrations().entrySet()) {
            if (entry.getKey() instanceof ImmuneResponse) {
                circulatorySystem.getMainCompartment().updateConcentration(entry.getKey(), activateImmuneReserve());
            }
        }

        compartment.modifyInflammation(INFLAMMATORY_RESPONSE);
    }

    public void modifyImmuneSuppression(float delta) {
        immuneSuppression = Math.min(Math.max(immuneSuppression + delta, 0), 1);
    }
}
