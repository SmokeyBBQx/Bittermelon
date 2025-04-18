package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.itemcontainers.client.ContainerMenu;
import com.site21.bittermelon.content.items.itemcontainers.client.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.site21.bittermelon.init.neoforge.BitterMenus.TOOLBOX_MENU;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BitterScreens {
    @SubscribeEvent
    public static void register(RegisterMenuScreensEvent event) {
        event.register(TOOLBOX_MENU.get(), (ContainerMenu menu, Inventory inventory, Component title) ->
                new ContainerScreen<>(
                        menu,
                        inventory,
                        title,
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/container/toolbox.png")
                ));
    }
}
