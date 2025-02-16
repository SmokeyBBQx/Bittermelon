package com.site21.bittermelon.content.items.base;

public enum ItemWeight {
    VERY_LIGHT(1, "Very Light"),
    LIGHT(2, "Light"),
    MEDIUM(3, "Medium"),
    HEAVY(4, "Heavy"),
    VERY_HEAVY(5, "Very Heavy");

    public final int value;
    public final String description;

    ItemWeight(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
