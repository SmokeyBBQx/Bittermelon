package com.site21.bittermelon.common.systems.medical.factory;

public enum Anatomy {
    HUMAN(new HumanFactoryNew());

    private final AnatomyFactory factory;

    Anatomy(AnatomyFactory factory) {
        this.factory = factory;
    }

    public AnatomyFactory getFactory() {
        return factory;
    }
}
