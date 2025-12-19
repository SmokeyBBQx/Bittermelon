package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class BitterSpawnEggs {

    public static final DeferredItem<Item> SCP_131_SPAWN_EGG = BitterItems.ITEMS.register("scp_131_spawn_egg",
            registryName -> new SpawnEggItem(BitterEntities.SCP_131.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, registryName))));

    public static final DeferredItem<Item> SCP_548_SPAWN_EGG = BitterItems.ITEMS.register("scp_548_spawn_egg",
            registryName -> new SpawnEggItem(BitterEntities.SCP_548.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, registryName))));

    public static final DeferredItem<Item> SCP_650_SPAWN_EGG = BitterItems.ITEMS.register("scp_650_spawn_egg",
            registryName -> new SpawnEggItem(BitterEntities.SCP_650.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, registryName))));

    public static final DeferredItem<Item> SCP_939_SPAWN_EGG = BitterItems.ITEMS.register("scp_939_spawn_egg",
            registryName -> new SpawnEggItem(BitterEntities.SCP_939.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, registryName))));

    public static final DeferredItem<Item> SCP_1507_SPAWN_EGG = BitterItems.ITEMS.register("scp_1507_spawn_egg",
            registryName -> new SpawnEggItem(BitterEntities.SCP_1507.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, registryName))));

    @SubscribeEvent
    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        // This ensures the class is loaded and spawn eggs are registered
    }
}