package com.site21.bittermelon.substance.reactions;

import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.SubstanceStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reaction {
    private final Map<Substance, Integer> reactants;
    private final Map<Substance, Integer> products;
    private final Map<Substance, Integer> orders;
    private final float activationEnergy;
    private final float preExponentialFactor;
    private final float enthalpyChange;

    private Reaction(ReactionBuilder builder) {
        this.reactants = new HashMap<>(builder.reactants);
        this.products = new HashMap<>(builder.products);
        this.orders = new HashMap<>(builder.orders);
        this.activationEnergy = builder.activationEnergy;
        this.preExponentialFactor = builder.preExponentialFactor;
        this.enthalpyChange = builder.enthalpyChange;
    }

    public double calculateReactionRate(Map<Substance, Float> concentrations, float temperature) {
        float R = 8.314f;
        double rate = (preExponentialFactor * Math.exp(-activationEnergy * 1000 / (R * temperature))) / 20;

        for (Map.Entry<Substance, Integer> entry : orders.entrySet()) {
            Substance substance = entry.getKey();
            int order = entry.getValue();
            float concentration = concentrations.getOrDefault(substance, 0.0f);
            rate *= Math.pow(concentration, order);
        }

        return rate;
    }

    public Map<Substance, Integer> getReactants() {
        return new HashMap<>(reactants);
    }

    public Map<Substance, Integer> getProducts() {
        return new HashMap<>(products);
    }

    public Map<Substance, Integer> getOrders() {
        return new HashMap<>(orders);
    }

    public int getReactantProportion(Substance substance) {
        return reactants.get(substance);
    }

    public int getOrder(Substance substance) {
        return orders.get(substance);
    }

    public float getActivationEnergy() {
        return activationEnergy;
    }

    public float getPreExponentialFactor() {
        return preExponentialFactor;
    }

    public float getEnthalpyChange() {
        return enthalpyChange;
    }

    public static class ReactionBuilder {
        private final Map<Substance, Integer> reactants = new HashMap<>();
        private final Map<Substance, Integer> products = new HashMap<>();
        private final Map<Substance, Integer> orders = new HashMap<>();

        private float activationEnergy = 2.5f;
        private float preExponentialFactor = 1e4f;
        private float enthalpyChange = 0;

        public ReactionBuilder addReactant(Substance substance, int proportion, int order) {
            reactants.put(substance, proportion);
            orders.put(substance, order);
            return this;
        }

        public ReactionBuilder addProduct(Substance substance, int proportion) {
            products.put(substance, proportion);
            return this;
        }

        public ReactionBuilder addCatalyst(Substance substance, int order) {
            orders.put(substance, order);
            return this;
        }

        public ReactionBuilder activationEnergy(float activationEnergy) {
            this.activationEnergy = activationEnergy;
            return this;
        }

        public ReactionBuilder preExponentialFactor(float preExponentialFactor) {
            this.preExponentialFactor = preExponentialFactor;
            return this;
        }

        public ReactionBuilder enthalpyChange(float enthalpyChange) {
            this.enthalpyChange = enthalpyChange;
            return this;
        }

        public Reaction build() {
            return new Reaction(this);
        }
    }
}