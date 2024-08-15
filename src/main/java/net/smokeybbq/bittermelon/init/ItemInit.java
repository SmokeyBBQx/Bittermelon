package net.smokeybbq.bittermelon.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.items.cigarettes.CigaretteItem;
import net.smokeybbq.bittermelon.items.cigarettes.CigarettePackItem;
import net.smokeybbq.bittermelon.items.handlabeler.HandLabeler;
import net.smokeybbq.bittermelon.items.toolbox.ToolBoxItem;
import net.smokeybbq.bittermelon.items.substancecontainers.BrownieItem;
import net.smokeybbq.bittermelon.items.substancecontainers.BucketItem;
public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bittermelon.MODID);

    public static final RegistryObject<BlockItem> PUDDLE_ITEM = ITEMS.register("puddle", () -> new BlockItem(BlockInit.PUDDLE.get(),
            new Item.Properties()
    ));

    public static final RegistryObject<Item> BUCKET_ITEM = ITEMS.register("bucket", () -> new BucketItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BROWNIE_ITEM = ITEMS.register("brownie", () -> new BrownieItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HAND_LABELER_ITEM = ITEMS.register("hand_labeler", () -> new HandLabeler(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BLUE_TOOLBOX_ITEM = ITEMS.register("blue_toolbox", () -> new ToolBoxItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CIGARETTE_PACK_RED_ITEM = ITEMS.register("cigarette_pack_red", () -> new CigarettePackItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CIGARETTE_ITEM = ITEMS.register("cigarette", () -> new CigaretteItem(new Item.Properties().stacksTo(8)));
}
