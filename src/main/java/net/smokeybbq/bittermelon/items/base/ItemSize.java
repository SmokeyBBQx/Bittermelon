package net.smokeybbq.bittermelon.items.base;

public enum ItemSize {
    TINY(1, "Tiny"),
    SMALL(2, "Small"),
    NORMAL(3, "Normal"),
    BULKY(4, "Bulky"),
    HUGE(5, "Huge"),
    GIGANTIC(6, "Gigantic");

    public final int value;
    public final String description;

    ItemSize(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
