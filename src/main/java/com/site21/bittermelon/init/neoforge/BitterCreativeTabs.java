package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BitterCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Bittermelon.MOD_ID);

    public static final Supplier<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () ->
            CreativeModeTab.builder()
                    .title(Component.literal("Bittermelon"))
                    .icon(() -> new ItemStack(BitterItems.BITTERMELON.get()))
                    .displayItems((parameters, output) -> {
                        for (DeferredHolder<Item, ? extends Item> item : BitterItems.ITEMS.getEntries()) {
                            output.accept(item.get());
                        }
                    })
                    .build()
    );
}
