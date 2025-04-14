package com.site21.bittermelon.content.inventory.client;

import com.site21.bittermelon.content.inventory.menu.InventoryMenu;
import net.minecraft.network.chat.Component;

public class InventoryScreen extends AbstractContainerScreen<InventoryMenu> {

    protected InventoryScreen(Component title, InventoryMenu menu) {
        super(title, menu);
    }
}
