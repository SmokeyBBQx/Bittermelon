package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.items.GermTest;
import com.site21.bittermelon.items.base.BaseItem;
import com.site21.bittermelon.items.base.ItemWeight;
import com.site21.bittermelon.items.cardboardbox.CardboardBoxItem;
import com.site21.bittermelon.items.cardboardbox.CollapsedCardboardBoxItem;
import com.site21.bittermelon.items.containers.substance.FluidContainerItem;
import com.site21.bittermelon.items.containers.substance.implementations.GlassFluidContainerItem;
import com.site21.bittermelon.items.medical.organic.BodyPart;
import com.site21.bittermelon.items.medical.tools.*;
import com.site21.bittermelon.items.toolbox.ToolBoxItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.BitterBlocks.ATM;
import static com.site21.bittermelon.init.BitterBlocks.SMALL_CARDBOARD_BOX;

public class BitterItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Bittermelon.MOD_ID);

    public static final DeferredItem<FluidContainerItem> BEER_BOTTLE = ITEMS.register("beer_bottle", () -> new GlassFluidContainerItem(
            new Item.Properties(),
            3,
            2,
            ItemWeight.MEDIUM,
            75,
            10)
    );

    public static final DeferredItem<ToolBoxItem> BLUE_TOOLBOX = ITEMS.register("blue_toolbox", () -> new ToolBoxItem(
            new Item.Properties(),
            5,
            4,
            ItemWeight.MEDIUM)
    );

    public static final DeferredItem<CardboardBoxItem> CARDBOARD_BOX = ITEMS.register("cardboard_box", () -> new CardboardBoxItem(
            new Item.Properties(),
            3,
            3,
            ItemWeight.VERY_LIGHT)
    );

    public static final DeferredItem<CollapsedCardboardBoxItem> COLLAPSED_CARDBOARD_BOX = ITEMS.register("collapsed_cardboard_box", () -> new CollapsedCardboardBoxItem(
            new Item.Properties(),
            4,
            1,
            ItemWeight.VERY_LIGHT)
    );

    public static final DeferredItem<BlockItem> SMALL_CARDBOARD_BOX_ITEM = ITEMS.register("small_cardboard_box_item", () -> new BlockItem(
            SMALL_CARDBOARD_BOX.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> ATM_ITEM = ITEMS.register("atm_item", () -> new BlockItem(
            ATM.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<Scalpel> SCALPEL = ITEMS.register("scalpel", () -> new Scalpel(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<Hemostat> HEMOSTAT = ITEMS.register("hemostat", () -> new Hemostat(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<Retractor> RETRACTOR = ITEMS.register("retractor", () -> new Retractor(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<Cautery> CAUTERY = ITEMS.register("cautery", () -> new Cautery(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<BodyPart> KIDNEY = ITEMS.register("kidney", () -> new BodyPart(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<BodyPart> LIVER = ITEMS.register("liver", () -> new BodyPart(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<BodyPart> STOMACH = ITEMS.register("stomach", () -> new BodyPart(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<BodyPart> GALLBLADDER = ITEMS.register("gallbladder", () -> new BodyPart(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<BodyPart> PELVIS = ITEMS.register("pelvis", () -> new BodyPart(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<BodyPart> BLADDER = ITEMS.register("bladder", () -> new BodyPart(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<SurgicalSponge> SURGICAL_SPONGE = ITEMS.register("surgical_sponge", () -> new SurgicalSponge(
            new Item.Properties().stacksTo(3),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<Bandage> BANDAGE = ITEMS.register("bandage", () -> new Bandage(
            new Item.Properties().stacksTo(3),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));


    public static final DeferredItem<GermTest> GERM_TEST = ITEMS.register("germ_test", () -> new GermTest(
            new Item.Properties().stacksTo(3)
    ));

    public static final DeferredItem<BaseItem> GLASS_SHARD = ITEMS.register("glass_shard", () -> new BaseItem(
            new Item.Properties().stacksTo(8), 1, 1, ItemWeight.VERY_LIGHT));
}
