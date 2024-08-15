package net.smokeybbq.bittermelon.items.cigarettes;

import net.smokeybbq.bittermelon.items.base.BaseItem;
import net.smokeybbq.bittermelon.items.base.ItemSize;
import net.smokeybbq.bittermelon.items.base.ItemWeight;

public class CigaretteItem extends BaseItem {
    public CigaretteItem(Properties pProperties) {
        super(pProperties);
        itemSize = ItemSize.TINY;
        itemWeight = ItemWeight.VERY_LIGHT;
    }
}
