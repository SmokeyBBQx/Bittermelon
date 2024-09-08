package net.smokeybbq.bittermelon.items.pda;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.smokeybbq.bittermelon.items.base.BaseItem;
import net.smokeybbq.bittermelon.items.base.ItemSize;
import net.smokeybbq.bittermelon.items.base.ItemWeight;

public class PIMCard extends BaseItem {
    public static final String OWNER_ID_KEY = "OwnerID";

    public PIMCard(Properties pProperties) {
        super(pProperties, ItemSize.TINY, ItemWeight.VERY_LIGHT);
    }

    public void setOwner(ItemStack stack, String ownerID) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains(OWNER_ID_KEY)) {
            CompoundTag ownerTag = nbt.getCompound(OWNER_ID_KEY);
            ownerTag.putString(OWNER_ID_KEY, ownerID);
        }
    }
}
