package net.smokeybbq.bittermelon.medical.simulation.compartments;

import net.smokeybbq.bittermelon.character.medical.MedicalStats;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class GroupCompartment extends Compartment {

    protected Map<String, Compartment> subCompartments = new HashMap<>();

    public GroupCompartment(String name, float permeability) {
        super(name, permeability);
        initializeCompartments();
    }

    public void initializeCompartments() {
    }

    @Override
    public Compartment getCompartment(String name) {
        return subCompartments.get(name);
    }

    public void addSubCompartment(Compartment compartment) {
        subCompartments.put(compartment.getName(), compartment);
    }

    public Map<String, Compartment> getSubCompartments() {
        return subCompartments;
    }

    @Override
    public void setBloodFlow(float newBloodFlow) {
        super.setBloodFlow(newBloodFlow);
        subCompartments.values().forEach(c -> c.setBloodFlow(newBloodFlow));
    }

    @Override
    public void modifyBloodFlow(float delta) {
        super.modifyBloodFlow(delta);
        subCompartments.values().forEach(c -> modifyBloodFlow(delta));
    }

    @Override
    public float getHealth() {
        return (float) subCompartments.values().stream()
                .mapToDouble(Compartment::getHealth)
                .average()
                .orElse(0.0);
    }

    @Override
    public void traverseCompartments(Consumer<Compartment> consumer) {
        consumer.accept(this);
        subCompartments.values().forEach(c -> c.traverseCompartments(consumer));
    }
}
