package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.GermTest;
import com.site21.bittermelon.content.items.IntercomPhoneItem;
import com.site21.bittermelon.content.items.KeycardItem;
import com.site21.bittermelon.content.items.medical.tools.SyringeItem;
import com.site21.bittermelon.content.items.mop.MopItem;
import com.site21.bittermelon.content.items.scps.SCP109;
import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.handheldprogrammer.HandheldProgrammerItem;
import com.site21.bittermelon.content.items.scps.scp2398.SCP2398;
import com.site21.bittermelon.content.items.smokable.SmokableItem;
import com.site21.bittermelon.content.items.substance.PowderedSubstanceItem;
import com.site21.bittermelon.content.items.substance.pill.PillItem;
import com.site21.bittermelon.content.items.taser.TaserItem;
import com.site21.bittermelon.content.items.wires.networkcable.NetworkCable;
import com.site21.bittermelon.content.items.cardboardbox.CardboardBoxItem;
import com.site21.bittermelon.content.items.cardboardbox.CollapsedCardboardBoxItem;
import com.site21.bittermelon.content.items.substance.FluidContainerItem;
import com.site21.bittermelon.content.items.substance.GasContainerItem;
import com.site21.bittermelon.content.items.substance.GlassFluidContainerItem;
import com.site21.bittermelon.content.items.laserdesignator.LaserDesignatorItem;
import com.site21.bittermelon.content.items.medical.organic.BodyPart;
import com.site21.bittermelon.content.items.medical.tools.*;
import com.site21.bittermelon.content.items.toolbox.ToolBoxItem;
import com.site21.bittermelon.content.items.wires.wire.Wire;
import com.site21.bittermelon.content.items.writablepaper.WritablePaper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.*;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.ENERGY_LOSS_ON_BOUNCE;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.MAX_BOUNCES;

public class BitterItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Bittermelon.MOD_ID);

    public static final DeferredItem<FluidContainerItem> BEER_BOTTLE = ITEMS.register("beer_bottle", () -> new GlassFluidContainerItem(
            new Item.Properties(),
            3,
            2,
            ItemWeight.MEDIUM,
            75,
            10,
            true
            ));

    public static final DeferredItem<ToolBoxItem> BLUE_TOOLBOX = ITEMS.register("blue_toolbox", () -> new ToolBoxItem(
            new Item.Properties(),
            5,
            4,
            ItemWeight.MEDIUM
            ));

    public static final DeferredItem<CardboardBoxItem> CARDBOARD_BOX = ITEMS.register("cardboard_box", () -> new CardboardBoxItem(
            new Item.Properties(),
            3,
            3,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<CollapsedCardboardBoxItem> COLLAPSED_CARDBOARD_BOX = ITEMS.register("collapsed_cardboard_box", () -> new CollapsedCardboardBoxItem(
            new Item.Properties(),
            4,
            1,
            ItemWeight.VERY_LIGHT
            ));

    public static final DeferredItem<BlockItem> SMALL_CARDBOARD_BOX = ITEMS.register("small_cardboard_box", () -> new BlockItem(
            BitterBlocks.SMALL_CARDBOARD_BOX.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> ATM = ITEMS.register("atm", () -> new BlockItem(
            BitterBlocks.ATM.get(),
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

    public static final DeferredItem<LaserDesignatorItem> LASER_DESIGNATOR = ITEMS.register("laser_designator", () -> new LaserDesignatorItem(
            new Item.Properties().stacksTo(1), 1, 2, ItemWeight.MEDIUM));

    public static final DeferredItem<BlockItem> CONTAINMENT_PANEL = ITEMS.register("containment_panel", () -> new BlockItem(
            BitterBlocks.CONTAINMENT_PANEL.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> WALL_THERMOMETER = ITEMS.register("wall_thermometer", () -> new BlockItem(
            THERMOMETER.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<GasContainerItem> GAS_CYLINDER = ITEMS.register("gas_cylinder", () -> new GasContainerItem(
            new Item.Properties(),
            1,
            3,
            ItemWeight.MEDIUM,
            100,
            2000,
            1200
    ));

    public static final DeferredItem<BaseItem> WHISKEY_BOTTLE = ITEMS.register("whiskey_bottle", () -> new FluidContainerItem(
            new Item.Properties(),
            3,
            2,
            ItemWeight.MEDIUM,
            100,
            10,
            true)
    );

    public static final DeferredItem<BlockItem> INTERCOM = ITEMS.register("intercom", () -> new BlockItem(
            BitterBlocks.INTERCOM.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<IntercomPhoneItem> INTERCOM_PHONE = ITEMS.register("intercom_phone", () -> new IntercomPhoneItem(
            new Item.Properties(),
            1,
            2,
            ItemWeight.LIGHT
    ));

    public static final DeferredItem<BlockItem> ENVIRONMENT_SENSOR = ITEMS.register("environment_sensor", () -> new BlockItem(
            BitterBlocks.ENVIRONMENT_SENSOR.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> CONTAINMENT_ALARM = ITEMS.register("containment_alarm", () -> new BlockItem(
            BitterBlocks.CONTAINMENT_ALARM.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> STRUCTURAL_BLOCK = ITEMS.register("structural_block", () -> new BlockItem(
            BitterBlocks.STRUCTURAL_BLOCK.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<NetworkCable> NETWORK_CABLE = ITEMS.register("network_cable", () -> new NetworkCable(
            new Item.Properties(),
            1,
            1,
            ItemWeight.LIGHT
    ));

    public static final DeferredItem<Wire> WIRE = ITEMS.register("wire", () -> new Wire(
            new Item.Properties(),
            1,
            1,
            ItemWeight.LIGHT
    ));

    public static final DeferredItem<HandheldProgrammerItem> HANDHELD_PROGRAMMER = ITEMS.register("handheld_programmer", () -> new HandheldProgrammerItem(
            new Item.Properties(),
            2,
            2,
            ItemWeight.MEDIUM
    ));

    public static final DeferredItem<BlockItem> DETONATOR = ITEMS.register("detonator", () -> new BlockItem(
            BitterBlocks.DETONATOR.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> SPEAKER = ITEMS.register("speaker", () -> new BlockItem(
            BitterBlocks.SPEAKER.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<SCP109> SCP_109 = ITEMS.register("scp109", () -> new SCP109(
            new Item.Properties()
    ));

    public static final DeferredItem<BodyPart> COLON = ITEMS.register("colon", () -> new BodyPart(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.MEDIUM
    ));

    public static final DeferredItem<BaseItem> HELLO_KITTY_CELLPHONE = ITEMS.register("hello_kitty_cellphone", () -> new BaseItem(
            new Item.Properties(),
            1,
            2,
            ItemWeight.LIGHT
    ));

    public static final DeferredItem<SCP2398> SCP_2398 = ITEMS.register("scp2398", () -> new SCP2398(
            new Item.Properties().stacksTo(1),
            1,
            3,
            ItemWeight.MEDIUM
    ));

    public static final DeferredItem<BaseItem> BASEBALL = ITEMS.register("baseball", () -> new BaseItem(
            new Item.Properties(),
            1,
            1,
            ItemWeight.LIGHT
    ));

    public static final DeferredItem<BaseItem> CIGARETTE_BUTT = ITEMS.register("cigarette_butt", () -> new BaseItem(
            new Item.Properties(),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<BaseItem> CIGARETTE = ITEMS.register("cigarette", () -> new SmokableItem(
            new Item.Properties(),
            1,
            1,
            ItemWeight.VERY_LIGHT,
            20,
            CIGARETTE_BUTT.get()
    ));

    public static final DeferredItem<KeycardItem> KEYCARD = ITEMS.register("keycard", () -> new KeycardItem(
            new Item.Properties(),
            1,
            1,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<BlockItem> SECURE_DOOR = ITEMS.register("secure_door", () -> new BlockItem(
            BitterBlocks.SECURE_DOOR.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> KEYCARD_READER_SECURE_DOOR = ITEMS.register("keycard_reader_secure_door", () -> new BlockItem(
            BitterBlocks.KEYCARD_READER_SECURE_DOOR.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> LARGE_SLIDING_DOOR = ITEMS.register("large_sliding_door", () -> new BlockItem(
            BitterBlocks.LARGE_SLIDING_DOOR.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<WritablePaper> WRITABLE_PAPER = ITEMS.register("paper", () -> new WritablePaper(
            new Item.Properties(),
            1,
            2,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<BaseItem> TASER_CARTRIDGE = ITEMS.register("taser_cartridge", () -> new BaseItem(
            new Item.Properties(),
            1,
            1,
            ItemWeight.LIGHT
    ));

    public static final DeferredItem<TaserItem> TASER = ITEMS.register("taser", () -> new TaserItem(
            new Item.Properties().stacksTo(1),
            1,
            1,
            ItemWeight.LIGHT
    ));

    public static final DeferredItem<BlockItem> YELLOW_INSPECTION_POSTER = ITEMS.register("yellow_inspection_poster", () -> new BlockItem(
            BitterBlocks.YELLOW_INSPECTION_POSTER.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> DISTRIBUTION_BOARD = ITEMS.register("distribution_board", () -> new BlockItem(
            BitterBlocks.DISTRIBUTION_BOARD.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> SCP151 = ITEMS.register("scp151", () -> new BlockItem(
            BitterBlocks.SCP151.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BaseItem> BODY_PART = ITEMS.register("body_part", () -> new BaseItem(
            new Item.Properties(),
            1,
            1,
            ItemWeight.LIGHT
    ));

    public static final DeferredItem<BaseItem> BITTERMELON = ITEMS.register("bittermelon", () -> new BaseItem(
            new Item.Properties(),
            1,
            1,
            ItemWeight.LIGHT
    ));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> SCP131_SPAWN_EGG = ITEMS.register("scp131_spawn_egg",
            () -> new DeferredSpawnEggItem(BitterEntities.SCP_131, 0xE36124, 0xF5CB42, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> SCP650_SPAWN_EGG = ITEMS.register("scp650_spawn_egg",
            () -> new DeferredSpawnEggItem(BitterEntities.SCP_650, 0x242221, 0x403E3D, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> SCP939_SPAWN_EGG = ITEMS.register("scp939_spawn_egg",
            () -> new DeferredSpawnEggItem(BitterEntities.SCP_939, 0xA33434, 0xDE2C2C, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> SCP1507_SPAWN_EGG = ITEMS.register("scp1507_spawn_egg",
            () -> new DeferredSpawnEggItem(BitterEntities.SCP_1507, 0xC967A4, 0xE687DB, new Item.Properties()));

    public static final DeferredItem<BaseItem> SCP_018 = ITEMS.register("scp018", () -> new BaseItem(
            new Item.Properties().component(ENERGY_LOSS_ON_BOUNCE, 1.5f).component(MAX_BOUNCES, 10000),
            1,
            1,
            ItemWeight.LIGHT
    ));

    public static final DeferredItem<SyringeItem> SYRINGE = ITEMS.register("syringe", () -> new SyringeItem(
            new Item.Properties(),
            1,
            2,
            ItemWeight.VERY_LIGHT,
            10,
            10
    ));

    public static final DeferredItem<MopItem> MOP = ITEMS.register("mop", () -> new MopItem(
            new Item.Properties(),
            1,
            2,
            ItemWeight.VERY_LIGHT
    ));

    public static final DeferredItem<PowderedSubstanceItem> POWDER = ITEMS.register("powder", () -> new PowderedSubstanceItem(
            new Item.Properties(),
            1,
            1,
            ItemWeight.VERY_LIGHT,
            20
    ));

    public static final DeferredItem<PillItem> PILL = ITEMS.register("pill", () -> new PillItem(
            new Item.Properties(),
            1,
            1,
            ItemWeight.VERY_LIGHT,
            20
    ));

    public static final DeferredItem<BlockItem> PERSONNEL_TERMINAL = ITEMS.register("personnel_terminal", () -> new BlockItem(
            BitterBlocks.PERSONNEL_TERMINAL.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> KEYCARD_PRINTER = ITEMS.register("keycard_printer", () -> new BlockItem(
            BitterBlocks.KEYCARD_PRINTER.get(),
            new Item.Properties()
    ));
}
