package com.site21.bittermelon.content.medical.compartments.deprecated.bodyparts;

import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.compartments.deprecated.conditionsold.Cut;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BodyPart extends CompartmentOld {
    private final List<CompartmentOld> connective = new ArrayList<>();
    private boolean doesBleed;

    public BodyPart(EnumSet<CompartmentTag> types, String name, CompartmentOld owner, float maxHealth) {
        super(types, name, owner, maxHealth);
        connective.add(this);
        setAttribute(FunctionType.NERVOUS, 1f);
    }

    public BodyPart(EnumSet<CompartmentTag> types, String name, CompartmentOld owner, float maxHealth, boolean hidden) {
        this(types, name, owner, maxHealth);
        this.hidden = hidden;
    }

    public boolean doesBleed() {
        return doesBleed;
    }

    public void setDoesBleed(boolean doesBleed) {
        this.doesBleed = doesBleed;
    }

    public void addConnector(CompartmentOld compartment) {
        connective.add(compartment);
    }

    public boolean isConnected(CompartmentOld compartment) {
        return connective.contains(compartment);
    }

    @Override
    public boolean canExtract() {
        return connective.stream()
                .allMatch(compartment -> compartment.getChildren().stream()
                        .anyMatch(child -> child instanceof Cut));
    }
}
