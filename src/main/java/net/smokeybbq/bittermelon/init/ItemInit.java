package net.smokeybbq.bittermelon.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smokeybbq.bittermelon.Bittermelon;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bittermelon.MODID);

    public static final RegistryObject<BlockItem> PUDDLE_ITEM = ITEMS.register("puddle", () -> new BlockItem(BlockInit.PUDDLE.get(),
            new Item.Properties()
    ));
}
