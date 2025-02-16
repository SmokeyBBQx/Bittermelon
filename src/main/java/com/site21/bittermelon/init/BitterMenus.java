package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.toolbox.client.ToolBoxMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BitterMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Bittermelon.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ToolBoxMenu>> TOOLBOX_MENU = MENUS.register("toolbox_menu", () -> IMenuTypeExtension.create(((windowId, inv, data) -> new ToolBoxMenu(windowId, inv, ItemStack.EMPTY))));
}
