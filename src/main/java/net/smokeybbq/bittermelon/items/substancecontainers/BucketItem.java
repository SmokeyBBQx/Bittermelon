package net.smokeybbq.bittermelon.items.substancecontainers;

import net.smokeybbq.bittermelon.items.base.ItemSize;
import net.smokeybbq.bittermelon.items.base.ItemWeight;

public class BucketItem extends SubstanceContainerItem {
    private static final int capacity = 100;
    public BucketItem(Properties pProperties) {
        super(pProperties, capacity);
        itemSize = ItemSize.NORMAL;
        itemWeight = ItemWeight.LIGHT;
    }
}
