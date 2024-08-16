package net.smokeybbq.bittermelon.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.items.itemcontainers.ContainerMenu;
import net.smokeybbq.bittermelon.items.itemcontainers.ContainerScreen;

import static net.smokeybbq.bittermelon.init.MenuInit.CIGARETTE_PACK_MENU;
import static net.smokeybbq.bittermelon.init.MenuInit.TOOLBOX_MENU;

public class ModScreens {
    public static void register() {
        MenuScreens.register(CIGARETTE_PACK_MENU.get(), (ContainerMenu menu, Inventory inventory, Component title) ->
                new ContainerScreen<>(
                        menu,
                        inventory,
                        title,
                        new ResourceLocation(Bittermelon.MODID, "textures/gui/container/cigarette_pack.png")
                )
        );

        MenuScreens.register(TOOLBOX_MENU.get(), (ContainerMenu menu, Inventory inventory, Component title) ->
                new ContainerScreen<>(
                        menu,
                        inventory,
                        title,
                        new ResourceLocation(Bittermelon.MODID, "textures/gui/container/toolbox.png")
                )
        );
    }
}
