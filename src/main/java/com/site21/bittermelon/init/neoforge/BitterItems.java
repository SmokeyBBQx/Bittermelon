package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.IntercomPhoneItem;
import com.site21.bittermelon.common.content.items.StickyNote;
import com.site21.bittermelon.common.content.items.laserdesignator.LaserDesignatorItem;
import com.site21.bittermelon.common.content.items.medical.tools.*;
import com.site21.bittermelon.common.content.items.mop.MopItem;
import com.site21.bittermelon.common.content.items.scps.SCP109;
import com.site21.bittermelon.common.content.items.scps.scp377.SCP377;
import com.site21.bittermelon.common.content.items.scps.scp377.SCP3771;
import com.site21.bittermelon.common.content.items.scps.scp377.SCP377Cookie;
import com.site21.bittermelon.common.content.items.screwdriver.ScrewdriverItem;
import com.site21.bittermelon.common.content.items.substance.FluidContainerItem;
import com.site21.bittermelon.common.content.items.substance.GasContainerItem;
import com.site21.bittermelon.common.content.items.substance.GlassFluidContainerItem;
import com.site21.bittermelon.common.content.items.substance.PowderedSubstanceItem;
import com.site21.bittermelon.common.content.items.substance.pill.PillItem;
import com.site21.bittermelon.common.content.items.taser.TaserItem;
import com.site21.bittermelon.common.content.items.wirecutters.WireCuttersItem;
import com.site21.bittermelon.common.content.items.wire.WireItem;
import com.site21.bittermelon.common.content.items.writablepaper.WritablePaper;
import com.site21.bittermelon.common.content.items.writingutensils.ChalkItem;
import com.site21.bittermelon.common.content.items.writingutensils.HighlighterItem;
import com.site21.bittermelon.common.systems.component.Screwdriver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;

public class BitterItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Bittermelon.MOD_ID);

    // Block Items
    public static final DeferredItem<BlockItem> SMALL_CARDBOARD_BOX = ITEMS.registerSimpleBlockItem(BitterBlocks.SMALL_CARDBOARD_BOX);
    public static final DeferredItem<BlockItem> ATM = ITEMS.registerSimpleBlockItem(BitterBlocks.ATM);
    public static final DeferredItem<BlockItem> CONTAINMENT_PANEL = ITEMS.registerSimpleBlockItem(BitterBlocks.CONTAINMENT_PANEL);
    public static final DeferredItem<BlockItem> THERMOMETER = ITEMS.registerSimpleBlockItem(BitterBlocks.THERMOMETER);
    public static final DeferredItem<BlockItem> INTERCOM = ITEMS.registerSimpleBlockItem(BitterBlocks.INTERCOM);
    public static final DeferredItem<BlockItem> ENVIRONMENT_SENSOR = ITEMS.registerSimpleBlockItem(BitterBlocks.ENVIRONMENT_SENSOR);
    public static final DeferredItem<BlockItem> CONTAINMENT_ALARM = ITEMS.registerSimpleBlockItem(BitterBlocks.CONTAINMENT_ALARM);
    public static final DeferredItem<BlockItem> STRUCTURAL_BLOCK = ITEMS.registerSimpleBlockItem(BitterBlocks.STRUCTURAL_BLOCK);
    public static final DeferredItem<BlockItem> DETONATOR = ITEMS.registerSimpleBlockItem(BitterBlocks.DETONATOR);
    public static final DeferredItem<BlockItem> SPEAKER = ITEMS.registerSimpleBlockItem(BitterBlocks.SPEAKER);
    public static final DeferredItem<BlockItem> SECURE_DOOR = ITEMS.registerSimpleBlockItem(BitterBlocks.SECURE_DOOR);
    public static final DeferredItem<BlockItem> KEYCARD_READER_SECURE_DOOR = ITEMS.registerSimpleBlockItem(BitterBlocks.KEYCARD_READER_SECURE_DOOR);
    public static final DeferredItem<BlockItem> LARGE_SLIDING_DOOR = ITEMS.registerSimpleBlockItem(BitterBlocks.LARGE_SLIDING_DOOR);
    public static final DeferredItem<BlockItem> YELLOW_INSPECTION_POSTER = ITEMS.registerSimpleBlockItem(BitterBlocks.YELLOW_INSPECTION_POSTER);
    public static final DeferredItem<BlockItem> DISTRIBUTION_BOARD = ITEMS.registerSimpleBlockItem(BitterBlocks.DISTRIBUTION_BOARD);
    public static final DeferredItem<BlockItem> SCP_151 = ITEMS.registerSimpleBlockItem(BitterBlocks.SCP_151);
    public static final DeferredItem<BlockItem> PERSONNEL_TERMINAL = ITEMS.registerSimpleBlockItem(BitterBlocks.PERSONNEL_TERMINAL);
    public static final DeferredItem<BlockItem> KEYCARD_PRINTER = ITEMS.registerSimpleBlockItem(BitterBlocks.KEYCARD_PRINTER);
    public static final DeferredItem<BlockItem> WINDOWED_SLIDING_DOOR = ITEMS.registerSimpleBlockItem(BitterBlocks.WINDOWED_SLIDING_DOOR);
    public static final DeferredItem<BlockItem> KEYCARD_READER = ITEMS.registerSimpleBlockItem(BitterBlocks.KEYCARD_READER);
    public static final DeferredItem<BlockItem> REDSTONE_DEVICE = ITEMS.registerSimpleBlockItem(BitterBlocks.REDSTONE_DEVICE);

    public static final DeferredItem<FluidContainerItem> BEER_BOTTLE = ITEMS.register("beer_bottle", registryName ->
            new GlassFluidContainerItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(VOLUME, 75.0f)));

    public static final DeferredItem<FluidContainerItem> WHISKEY_BOTTLE = ITEMS.register("whiskey_bottle", registryName ->
            new FluidContainerItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(VOLUME, 100.0f)));

    public static final DeferredItem<SCP109> SCP_109 = ITEMS.register("scp_109", registryName ->
            new SCP109(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(VOLUME, 0.0f)));

    public static final DeferredItem<Item> CIGARETTE = ITEMS.register("cigarette", registryName ->
            new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(VOLUME, 20.0f)));

    public static final DeferredItem<Item> SCP_018 = ITEMS.register("scp_018", registryName ->
            new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(ENERGY_LOSS_ON_BOUNCE, 1.5f)
                    .component(MAX_BOUNCES, 10000)));

    public static final DeferredItem<SyringeItem> SYRINGE = ITEMS.register("syringe", registryName ->
            new SyringeItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(VOLUME, 10.0f)));

    public static final DeferredItem<MopItem> MOP = ITEMS.register("mop", registryName ->
            new MopItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(VOLUME, 50.0f)
                    .component(MAX_TRANSFER_RATE, 20)));

    public static final DeferredItem<PowderedSubstanceItem> POWDER = ITEMS.register("powder", registryName ->
            new PowderedSubstanceItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(VOLUME, 20.0f)));

    public static final DeferredItem<PillItem> PILL = ITEMS.register("pill", registryName ->
            new PillItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(VOLUME, 20.0f)));


    public static final DeferredItem<Item> SCALPEL = ITEMS.registerSimpleItem("scalpel",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> HEMOSTAT = ITEMS.registerSimpleItem("hemostat",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> RETRACTOR = ITEMS.registerSimpleItem("retractor",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> CAUTERY = ITEMS.registerSimpleItem("cautery",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> KIDNEY = ITEMS.registerSimpleItem("kidney",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> LIVER = ITEMS.registerSimpleItem("liver",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> STOMACH = ITEMS.registerSimpleItem("stomach",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> GALLBLADDER = ITEMS.registerSimpleItem("gallbladder",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> PELVIS = ITEMS.registerSimpleItem("pelvis",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> BLADDER = ITEMS.registerSimpleItem("bladder",
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> SURGICAL_SPONGE = ITEMS.registerSimpleItem("surgical_sponge",
            new Item.Properties().stacksTo(3));

    public static final DeferredItem<Item> BANDAGE = ITEMS.registerSimpleItem("bandage",
            new Item.Properties().stacksTo(3));

    public static final DeferredItem<Item> GLASS_SHARD = ITEMS.registerSimpleItem("glass_shard",
            new Item.Properties().stacksTo(8));

    public static final DeferredItem<LaserDesignatorItem> LASER_DESIGNATOR = ITEMS.registerItem("laser_designator", LaserDesignatorItem::new,
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<GasContainerItem> GAS_CYLINDER = ITEMS.registerItem("gas_cylinder", GasContainerItem::new);

    public static final DeferredItem<IntercomPhoneItem> INTERCOM_PHONE = ITEMS.registerItem("intercom_phone", IntercomPhoneItem::new);

    public static final DeferredItem<Item> NETWORK_CABLE = ITEMS.registerSimpleItem("network_cable");

    public static final DeferredItem<WireItem> WIRE = ITEMS.registerItem("wire", WireItem::new);

    public static final DeferredItem<Item> COLON = ITEMS.registerSimpleItem("colon",
            new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> HELLO_KITTY_CELLPHONE = ITEMS.registerSimpleItem("hello_kitty_cellphone");

    public static final DeferredItem<Item> SCP_2398 = ITEMS.registerSimpleItem("scp_2398",
            new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> BASEBALL = ITEMS.registerSimpleItem("baseball");

    public static final DeferredItem<Item> CIGARETTE_BUTT = ITEMS.register("cigarette_butt", registryName ->
            new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(DataComponents.CONSUMABLE, Consumable.builder()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.NAUSEA, 100, 0)))
                            .build())));

    public static final DeferredItem<Item> KEYCARD = ITEMS.registerSimpleItem("keycard");

    public static final DeferredItem<WritablePaper> WRITABLE_PAPER = ITEMS.registerItem("paper", WritablePaper::new);

    public static final DeferredItem<Item> TASER_CARTRIDGE = ITEMS.registerSimpleItem("taser_cartridge");

    public static final DeferredItem<TaserItem> TASER = ITEMS.registerItem("taser", TaserItem::new,
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> BODY_PART = ITEMS.registerSimpleItem("body_part");

    public static final DeferredItem<Item> BITTERMELON = ITEMS.register("bittermelon",
            registryName -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationModifier(0.1f)
                            .build())
                    .component(DataComponents.CONSUMABLE, Consumable.builder()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8f))
                            .build())));

    public static final DeferredItem<SCP377> SCP_377 = ITEMS.registerItem("scp_377", SCP377::new);

    public static final DeferredItem<SCP3771> SCP_377_1 = ITEMS.registerItem("scp_377_1", SCP3771::new);

    public static final DeferredItem<SCP377Cookie> FORTUNE_COOKIE = ITEMS.registerItem("fortune_cookie", SCP377Cookie::new);

    public static final DeferredItem<Item> CRACKED_FORTUNE_COOKIE = ITEMS.registerSimpleItem("cracked_fortune_cookie",
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(1).alwaysEdible().build()));

    public static final DeferredItem<Item> HANDHELD_SYSTEM_INTERFACE = ITEMS.registerSimpleItem("handheld_system_interface");

    public static final DeferredItem<ScrewdriverItem> SCREWDRIVER = ITEMS.register("screwdriver", registryName ->
            new ScrewdriverItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .component(BitterDataComponents.SCREWDRIVER, Screwdriver.DEFAULT)));

    public static final DeferredItem<ChalkItem> CHALK = ITEMS.register("chalk", registryName ->
            new ChalkItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .stacksTo(16)
                    .component(DataComponents.CONSUMABLE, Consumable.builder().build())));

    public static final DeferredItem<HighlighterItem> HIGHLIGHTER = ITEMS.registerItem("highlighter", HighlighterItem::new);

    public static final DeferredItem<StickyNote> STICKY_NOTE = ITEMS.registerItem("sticky_note",
            props -> new StickyNote(BitterBlocks.STICKY_NOTE.get(), props),
            new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> PEN = ITEMS.registerSimpleItem("pen");

    public static final DeferredItem<WireCuttersItem> WIRE_CUTTERS = ITEMS.registerItem("wire_cutters", WireCuttersItem::new);
}
