package com.site21.bittermelon.content.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InventoryMenu extends AbstractContainerMenu {
    private final List<ContainerMenu> containers = new ArrayList<>();

    protected InventoryMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    private void drawSlots() {

    }


    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
