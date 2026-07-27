package com.site21.bittermelon.common.systems.medical.wound;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class HealthContainer {
    private final PartInstance root;
    private final List<PartInstance> parts;
    private final EnumMap<LimbSlot, PartInstance> limbs;

    public HealthContainer(PartInstance root) {
        this.root = root;
        this.parts = new ArrayList<>();
        this.limbs = new EnumMap<>(LimbSlot.class);
    }

    public PartInstance getRoot() {
        return root;
    }

    public List<PartInstance> getParts() {
        return parts;
    }

    public void addPart(PartInstance part) {
        parts.add(part);
    }

    public boolean isLimbOccupied(String partName) {
        return limbs.containsKey(LimbSlot.fromIdentifier(partName));
    }
}
