package com.site21.bittermelon.medical.compartments.bodyparts;

import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.compartments.FunctionType;
import com.site21.bittermelon.medical.compartments.conditions.Cut;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BodyPart extends Compartment {
    private final List<Compartment> connective = new ArrayList<>();
    private boolean doesBleed;

    public BodyPart(EnumSet<CompartmentType> types, String name, Compartment owner, float maxHealth) {
        super(types, name, owner, maxHealth);
        connective.add(this);
        setAttribute(FunctionType.NERVOUS, 1f);
    }

    public BodyPart(EnumSet<CompartmentType> types, String name, Compartment owner, float maxHealth, boolean hidden) {
        this(types, name, owner, maxHealth);
        this.hidden = hidden;
    }

    public boolean doesBleed() {
        return doesBleed;
    }

    public void setDoesBleed(boolean doesBleed) {
        this.doesBleed = doesBleed;
    }

    public void addConnector(Compartment compartment) {
        connective.add(compartment);
    }

    public boolean isConnected(Compartment compartment) {
        return connective.contains(compartment);
    }

    @Override
    public boolean canExtract() {
        return connective.stream()
                .allMatch(compartment -> compartment.getChildren().stream()
                        .anyMatch(child -> child instanceof Cut));
    }
}
