package com.site21.bittermelon.common.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.component.medical.Scalpel;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class CommonSetup {

    @SubscribeEvent
    public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.IRON_SWORD, builder ->
                builder.set(BitterDataComponents.SCALPEL.get(), new Scalpel(0.7f)));
        event.modify(Items.NETHERITE_SWORD, builder ->
                builder.set(BitterDataComponents.SCALPEL.get(), new Scalpel(0.8f)));
    }
}
