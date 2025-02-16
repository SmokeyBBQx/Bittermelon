package com.site21.bittermelon.content.inventory;

public class Slot {
    public int itemIndex = -1;
    public int index;
    public final SlotComponent slotComponent;
    public final int x;
    public final int y;

    public Slot(SlotComponent slotComponent, int x, int y) {
        this.slotComponent = slotComponent;
        this.x = x;
        this.y = y;
    }

    public boolean hasItem() {
        return itemIndex >= 0;
    }
}
