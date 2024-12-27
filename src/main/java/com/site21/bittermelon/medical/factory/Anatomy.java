package com.site21.bittermelon.medical.factory;

public enum Anatomy {
    HUMAN(new HumanFactory()),
    CHICKEN(new ChickenFactory());

    private final AnatomyFactory factory;

    Anatomy(AnatomyFactory factory) {
        this.factory = factory;
    }

    public AnatomyFactory getFactory() {
        return factory;
    }
}
