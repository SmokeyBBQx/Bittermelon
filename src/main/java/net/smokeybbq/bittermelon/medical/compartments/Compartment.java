package net.smokeybbq.bittermelon.medical.compartments;

import net.smokeybbq.bittermelon.medical.substance.Substance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Compartment {
    protected String name;
    protected float immunePrivilege = 100;
    protected float health = 100;
    protected float bloodFlow = 1;
    protected float bloodFlowModifier = 1;
    protected float inflammation = 0.1F;
    protected float function = 1;
    protected float pain = 0;
    protected float nervousFunction = 1;
    protected float volume;
    protected float permeability;
    protected Map<Substance, Float> concentrations = new HashMap<>();
    public boolean excludeFromCirculation = false;
    protected boolean bleeding = false;
    protected List<CompartmentTag> tags = new ArrayList<>();

    public Compartment(String name, float permeability, float volume) {
        this.name = name;
        this.permeability = permeability;
        this.volume = volume;
        updateBloodFlow();
    }


    public float getConcentration(Substance substance) {
        return concentrations.getOrDefault(substance, 0.0F);
    }

    public void updateConcentration(Substance substance, float delta) {
        concentrations.compute(substance, (k, v) -> Math.max(0, (v == null ? 0 : v) + delta));
//        concentrations.compute(substance, (k, v) -> Math.max(0, (v == null ? 0 : v) + (delta / getVolume())));

        if (concentrations.get(substance) <= 0.1) {
            clearConcentration(substance);
        }
    }

    public void clearConcentration(Substance substance) {
        concentrations.remove(substance);
    }

    public Map<Substance, Float> getConcentrations() {
        return concentrations;
    }

    public String getName() {
        return name;
    }

    public float getHealth() {
        return health;
    }

    public float getBloodFlow() {
        return bloodFlow;
    }

    public float getInflammation() {
        return inflammation;
    }

    public float getImmunePrivilege() {
        return immunePrivilege;
    }

    public Compartment getCompartment(String name) {
        return this;
    }

    public Compartment getMainCompartment() {
        return this;
    }

    public float getFunction() {
        return function * health / 100 / inflammation;
    }

    public float getPain() {
        return pain * nervousFunction;
    }

    public float getVolume() {
        return volume;
    }
    public float getBleedingAmount() {return volume / 100;}
    public boolean isBleeding() {return bleeding;}
    public float getNervousFunction() {return nervousFunction;}

    // Setters and modifiers
    public void setBleeding(boolean value) {
        bleeding = value;
    }

    public void modifyHealth(float delta) {
        health = Math.max(0, health + delta);
    }

    public void setBloodFlow(float newBloodFlow) {
        bloodFlow = newBloodFlow * inflammation * permeability;
    }

    public void modifyBloodFlow(float delta) {
        bloodFlow = Math.max(0.0001F, (bloodFlowModifier + delta) * inflammation * permeability);
    }

    private void updateBloodFlow() {
        bloodFlow = Math.max(0.0001F, bloodFlowModifier * inflammation * permeability);
    }

    public void modifyInflammation(float delta) {
        inflammation = Math.min(1, Math.max(0.1F, inflammation + delta));
        updateBloodFlow();
    }

    public void modifyImmunePrivilege(float delta) {
        immunePrivilege = Math.max(0, Math.min(100, immunePrivilege + delta));
    }

    public void eliminateConcentration(Substance substance, float rate) {
        updateConcentration(substance, -rate * getConcentration(substance));
    }

    public void moveConcentration(Compartment target, Substance substance, float rate, float timeStep) {
        float amount = Math.min(getConcentration(substance), getConcentration(substance) * timeStep * rate);
        if (amount > 0) {
            updateConcentration(substance, -amount);
            target.updateConcentration(substance, amount);
        }
    }

    public void traverseCompartments(Consumer<Compartment> consumer) {
        consumer.accept(this);
    }

    public void addTag(CompartmentTag tag) {
        tags.add(tag);
    }

    public boolean hasTag(CompartmentTag tag) {
        return tags.contains(tag);
    }

    public void setExcludeFromCirculation(boolean value) {
        excludeFromCirculation = value;
    }
}
