package net.smokeybbq.bittermelon.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class KeycardItem extends Item {

    public KeycardItem(Item.Properties properties) {
        super(properties);
    }

    public static void addPermissionGroup(ItemStack itemStack, String group) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        Set<String> groups = getPermissionGroups(itemStack);
        groups.add(group);
        nbt.putString("PermissionGroups", String.join(",", groups));
    }

    public static Set<String> getPermissionGroups(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        String groups = nbt.getString("PermissionGroups");
        Set<String> groupSet = new HashSet<>();
        if (!groups.isEmpty()) {
            String[] groupArray = groups.split(",");
            for (String group : groupArray) {
                groupSet.add(group);
            }
        }
        return groupSet;
    }
}
