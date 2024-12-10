package com.site21.bittermelon.items.base;

public enum ItemSize {
    TINY(1, 2, "Tiny"),
    SMALL(3, 4, "Small"),
    NORMAL(5, 6, "Normal"),
    BULKY(7, 9, "Bulky"),
    HUGE(10, 12, "Huge"),
    GIGANTIC(13, 16, "Gigantic");

    private final int minArea;
    private final int maxArea;
    public final String description;

    ItemSize(int minArea, int maxArea, String description) {
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.description = description;
    }

    public static ItemSize fromDimensions(int width, int height) {
        int area = width * height;

        for (ItemSize size : ItemSize.values()) {
            if (area >= size.minArea && area <= size.maxArea) {
                return size;
            }
        }

        return GIGANTIC;
    }
}
