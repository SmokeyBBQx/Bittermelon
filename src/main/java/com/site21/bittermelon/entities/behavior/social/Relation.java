package com.site21.bittermelon.entities.behavior.social;

public class Relation {
    private final String name;
    private final float modifier;

    public Relation(String name, float modifier) {
        this.name = name;
        this.modifier = modifier;
    }

    public float getModifier() {
        return modifier;
    }
}
