package com.site21.bittermelon.common.systems.medical.bodypart;

import java.util.ArrayList;
import java.util.List;

public class HealthContainer {
    private final PartInstance root;
    private final List<PartInstance> parts;

    public HealthContainer(PartInstance root) {
        this.root = root;
        this.parts = new ArrayList<>();
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

}
