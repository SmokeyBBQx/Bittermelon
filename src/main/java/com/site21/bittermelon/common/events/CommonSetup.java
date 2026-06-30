package com.site21.bittermelon.common.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.component.medical.Scalpel;
import com.site21.bittermelon.datagen.BitterEntityLootProvider;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class CommonSetup {

    @SubscribeEvent
    public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.IRON_SWORD, builder ->
                builder.set(BitterDataComponents.SCALPEL.get(), new Scalpel(0.7f)));
        event.modify(Items.NETHERITE_SWORD, builder ->
                builder.set(BitterDataComponents.SCALPEL.get(), new Scalpel(0.8f)));
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(
                        BitterEntityLootProvider::new,
                        LootContextParamSets.ENTITY)),
                lookupProvider
        ));
    }
}
