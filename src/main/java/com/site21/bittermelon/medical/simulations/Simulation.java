package com.site21.bittermelon.medical.simulations;

import com.site21.bittermelon.substance.SubstanceStack;

public abstract class Simulation {
    private final SubstanceStack stack;

    public Simulation(SubstanceStack stack) {
        this.stack = stack;
    }

    public abstract void update();
}
