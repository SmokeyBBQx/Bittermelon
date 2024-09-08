package net.smokeybbq.bittermelon.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.items.base.BaseItem;
import net.smokeybbq.bittermelon.items.base.ItemSize;
import net.smokeybbq.bittermelon.items.base.ItemWeight;
import net.smokeybbq.bittermelon.items.cigarettes.CigaretteItem;
import net.smokeybbq.bittermelon.items.cigarettes.CigarettePackItem;
import net.smokeybbq.bittermelon.items.handlabeler.HandLabeler;
import net.smokeybbq.bittermelon.items.radio.RadioItem;
import net.smokeybbq.bittermelon.items.substancecontainers.GlassContainerItem;
import net.smokeybbq.bittermelon.items.substancecontainers.SubstanceContainerItem;
import net.smokeybbq.bittermelon.items.substancecontainers.SubstanceSolidItem;
import net.smokeybbq.bittermelon.items.toolbox.ToolBoxItem;

import static net.smokeybbq.bittermelon.init.SubstanceInit.*;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bittermelon.MODID);

    public static final RegistryObject<Item> DIRTY_DECAL = ITEMS.register("dirty_decal", () -> new BlockItem(BlockInit.DIRTY.get(), new Item.Properties()));
    public static final RegistryObject<Item> LARGE_BEAKER = ITEMS.register("large_beaker", () -> new BlockItem(BlockInit.LARGE_BEAKER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> WINE_BOTTLE = ITEMS.register("wine_bottle", () -> new GlassContainerItem(
            new Item.Properties().stacksTo(1),
            ItemSize.NORMAL,
            ItemWeight.MEDIUM,
            100)
            .addInitialSubstance(WINE, 95)
    );
    public static final RegistryObject<Item> BEER_BOTTLE = ITEMS.register("beer_bottle", () -> new GlassContainerItem(
            new Item.Properties().stacksTo(1),
            ItemSize.NORMAL,
            ItemWeight.MEDIUM,
            75)
            .addInitialSubstance(BEER, 70)
    );
    public static final RegistryObject<Item> KETCHUP_BOTTLE = ITEMS.register("ketchup_bottle", () -> new SubstanceContainerItem(
            new Item.Properties().stacksTo(1),
            ItemSize.NORMAL,
            ItemWeight.MEDIUM,
            75)
            .addInitialSubstance(KETCHUP, 70)
    );

    public static final RegistryObject<Item> JUG = ITEMS.register("jug", () -> new SubstanceContainerItem(
            new Item.Properties().stacksTo(1),
            ItemSize.NORMAL,
            ItemWeight.LIGHT,
            1000
    ));

    public static final RegistryObject<Item> BROWNIE = ITEMS.register("brownie", () -> new SubstanceSolidItem(
            new Item.Properties().stacksTo(1),
            ItemSize.SMALL,
            ItemWeight.VERY_LIGHT,
            50)
            .addInitialSubstance(BROWNIE_MIXTURE, 40)
            .addInitialSubstance(HASHISH, 10)
    );

    public static final RegistryObject<Item> DRINKING_GLASS = ITEMS.register("drinking_glass", () -> new BlockItem(BlockInit.DRINKING_GLASS_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> HAND_LABELER = ITEMS.register("hand_labeler", () -> new HandLabeler(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BLUE_TOOLBOX = ITEMS.register("blue_toolbox", () -> new ToolBoxItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CIGARETTE_PACK_RED = ITEMS.register("cigarette_pack_red", () -> new CigarettePackItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CIGARETTE = ITEMS.register("cigarette", () -> new CigaretteItem(new Item.Properties().stacksTo(8)));
    public static final RegistryObject<Item> GLASS_SHARD = ITEMS.register("glass_shard", () -> new BaseItem(new Item.Properties().stacksTo(8), ItemSize.TINY, ItemWeight.VERY_LIGHT));
    public static final RegistryObject<Item> RADIO_TEST = ITEMS.register("radio_test", () -> new RadioItem(
            new Item.Properties().stacksTo(1),
            ItemSize.NORMAL,
            ItemWeight.LIGHT,
            0.5F,
            1000,
            1000,
            true
    ));
}
