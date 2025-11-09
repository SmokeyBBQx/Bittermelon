package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.IntercomPhoneItem;
import com.site21.bittermelon.common.content.items.StickyNote;
import com.site21.bittermelon.common.content.items.handheldsysteminterface.HandheldSystemInterface;
import com.site21.bittermelon.common.content.items.medical.tools.SyringeItem;
import com.site21.bittermelon.common.content.items.mop.MopItem;
import com.site21.bittermelon.common.content.items.scps.SCP109;
import com.site21.bittermelon.common.content.items.base.BitterItem;
import com.site21.bittermelon.common.content.items.base.ItemWeight;
import com.site21.bittermelon.common.content.items.scps.scp2398.SCP2398;
import com.site21.bittermelon.common.content.items.scps.scp377.SCP377;
import com.site21.bittermelon.common.content.items.scps.scp377.SCP3771;
import com.site21.bittermelon.common.content.items.scps.scp377.SCP377Cookie;
import com.site21.bittermelon.common.content.items.screwdriver.ScrewdriverItem;
import com.site21.bittermelon.common.content.items.smokable.SmokableItem;
import com.site21.bittermelon.common.content.items.substance.PowderedSubstanceItem;
import com.site21.bittermelon.common.content.items.substance.pill.PillItem;
import com.site21.bittermelon.common.content.items.taser.TaserItem;
import com.site21.bittermelon.common.content.items.wirecutters.WireCuttersItem;
import com.site21.bittermelon.common.content.items.substance.FluidContainerItem;
import com.site21.bittermelon.common.content.items.substance.GasContainerItem;
import com.site21.bittermelon.common.content.items.substance.GlassFluidContainerItem;
import com.site21.bittermelon.common.content.items.laserdesignator.LaserDesignatorItem;
import com.site21.bittermelon.common.content.items.wire.WireItem;
import com.site21.bittermelon.common.content.items.writablepaper.WritablePaper;
import com.site21.bittermelon.common.content.items.writingutensils.ChalkItem;
import com.site21.bittermelon.common.content.items.writingutensils.HighlighterItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.*;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;

public class BitterItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Bittermelon.MOD_ID);

    public static final DeferredItem<FluidContainerItem> BEER_BOTTLE = ITEMS.register("beer_bottle", () -> new GlassFluidContainerItem(
            new Item.Properties().component(VOLUME, 75.0f),
            true
            ));

    public static final DeferredItem<Item> BLUE_TOOLBOX = ITEMS.register("blue_toolbox", () -> new Item(
            new Item.Properties()
            ));

    public static final DeferredItem<Item> CARDBOARD_BOX = ITEMS.register("cardboard_box", () -> new Item(
            new Item.Properties()
    ));

    public static final DeferredItem<Item> COLLAPSED_CARDBOARD_BOX = ITEMS.register("collapsed_cardboard_box", () -> new Item(
            new Item.Properties()
            ));

    public static final DeferredItem<BlockItem> SMALL_CARDBOARD_BOX = ITEMS.register("small_cardboard_box", () -> new BlockItem(
            BitterBlocks.SMALL_CARDBOARD_BOX.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> ATM = ITEMS.register("atm", () -> new BlockItem(
            BitterBlocks.ATM.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<Item> SCALPEL = ITEMS.register("scalpel", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> HEMOSTAT = ITEMS.register("hemostat", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> RETRACTOR = ITEMS.register("retractor", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> CAUTERY = ITEMS.register("cautery", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> KIDNEY = ITEMS.register("kidney", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> LIVER = ITEMS.register("liver", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> STOMACH = ITEMS.register("stomach", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> GALLBLADDER = ITEMS.register("gallbladder", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> PELVIS = ITEMS.register("pelvis", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> BLADDER = ITEMS.register("bladder", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<Item> SURGICAL_SPONGE = ITEMS.register("surgical_sponge", () -> new Item(
            new Item.Properties().stacksTo(3)
    ));

    public static final DeferredItem<Item> BANDAGE = ITEMS.register("bandage", () -> new Item(
            new Item.Properties().stacksTo(3)
    ));

    public static final DeferredItem<BitterItem> GLASS_SHARD = ITEMS.register("glass_shard", () -> new BitterItem(
            new Item.Properties().stacksTo(8)));

    public static final DeferredItem<LaserDesignatorItem> LASER_DESIGNATOR = ITEMS.register("laser_designator", () -> new LaserDesignatorItem(
            new Item.Properties().stacksTo(1)));

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
            1200
    ));

    public static final DeferredItem<BitterItem> WHISKEY_BOTTLE = ITEMS.register("whiskey_bottle", () -> new FluidContainerItem(
            new Item.Properties().component(VOLUME, 100.0f),
            true)
    );

    public static final DeferredItem<BlockItem> INTERCOM = ITEMS.register("intercom", () -> new BlockItem(
            BitterBlocks.INTERCOM.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<IntercomPhoneItem> INTERCOM_PHONE = ITEMS.register("intercom_phone", () -> new IntercomPhoneItem(
            new Item.Properties()
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

    public static final DeferredItem<WireItem> WIRE = ITEMS.register("wire", () -> new WireItem(
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> DETONATOR = ITEMS.register("detonator", () -> new BlockItem(
            BitterBlocks.DETONATOR.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> SPEAKER = ITEMS.register("speaker", () -> new BlockItem(
            BitterBlocks.SPEAKER.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<SCP109> SCP_109 = ITEMS.register("scp_109", () -> new SCP109(
            new Item.Properties().component(VOLUME, 0.0f)
    ));

    public static final DeferredItem<Item> COLON = ITEMS.register("colon", () -> new Item(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<BitterItem> HELLO_KITTY_CELLPHONE = ITEMS.register("hello_kitty_cellphone", () -> new BitterItem(
            new Item.Properties()
    ));

    public static final DeferredItem<SCP2398> SCP_2398 = ITEMS.register("scp_2398", () -> new SCP2398(
            new Item.Properties().stacksTo(1)
    ));

    public static final DeferredItem<BitterItem> BASEBALL = ITEMS.register("baseball", () -> new BitterItem(
            new Item.Properties()
    ));

    public static final DeferredItem<BitterItem> CIGARETTE_BUTT = ITEMS.register("cigarette_butt", () -> new BitterItem(
            new Item.Properties()
    ));

    public static final DeferredItem<BitterItem> CIGARETTE = ITEMS.register("cigarette", () -> new SmokableItem(
            new Item.Properties().component(VOLUME, 20.0f),
            CIGARETTE_BUTT.get()
    ));

    public static final DeferredItem<Item> KEYCARD = ITEMS.register("keycard", () -> new Item(
            new Item.Properties()
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
            new Item.Properties()
    ));

    public static final DeferredItem<BitterItem> TASER_CARTRIDGE = ITEMS.register("taser_cartridge", () -> new BitterItem(
            new Item.Properties()
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

    public static final DeferredItem<BlockItem> SCP_151 = ITEMS.register("scp_151", () -> new BlockItem(
            BitterBlocks.SCP_151.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BitterItem> BODY_PART = ITEMS.register("body_part", () -> new BitterItem(
            new Item.Properties()
    ));

    public static final DeferredItem<BitterItem> BITTERMELON = ITEMS.register("bittermelon", () -> new BitterItem(
            new Item.Properties()
    ));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> SCP_131_SPAWN_EGG = ITEMS.register("scp_131_spawn_egg",
            () -> new DeferredSpawnEggItem(BitterEntities.SCP_131, 0xE36124, 0xF5CB42, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> SCP_650_SPAWN_EGG = ITEMS.register("scp_650_spawn_egg",
            () -> new DeferredSpawnEggItem(BitterEntities.SCP_650, 0x242221, 0x403E3D, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> SCP_939_SPAWN_EGG = ITEMS.register("scp_939_spawn_egg",
            () -> new DeferredSpawnEggItem(BitterEntities.SCP_939, 0xA33434, 0xDE2C2C, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> SCP_1507_SPAWN_EGG = ITEMS.register("scp_1507_spawn_egg",
            () -> new DeferredSpawnEggItem(BitterEntities.SCP_1507, 0xC967A4, 0xE687DB, new Item.Properties()));

    public static final DeferredItem<BitterItem> SCP_018 = ITEMS.register("scp_018", () -> new BitterItem(
            new Item.Properties().component(ENERGY_LOSS_ON_BOUNCE, 1.5f).component(MAX_BOUNCES, 10000)
    ));

    public static final DeferredItem<SyringeItem> SYRINGE = ITEMS.register("syringe", () -> new SyringeItem(
            new Item.Properties().component(VOLUME, 10.0f)
    ));

    public static final DeferredItem<MopItem> MOP = ITEMS.register("mop", () -> new MopItem(
            new Item.Properties().component(VOLUME, 50.0f).component(MAX_TRANSFER_RATE, 20)
    ));

    public static final DeferredItem<PowderedSubstanceItem> POWDER = ITEMS.register("powder", () -> new PowderedSubstanceItem(
            new Item.Properties().component(VOLUME, 20.0f)
    ));

    public static final DeferredItem<PillItem> PILL = ITEMS.register("pill", () -> new PillItem(
            new Item.Properties().component(VOLUME, 20.0f)
    ));

    public static final DeferredItem<BlockItem> PERSONNEL_TERMINAL = ITEMS.register("personnel_terminal", () -> new BlockItem(
            BitterBlocks.PERSONNEL_TERMINAL.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> KEYCARD_PRINTER = ITEMS.register("keycard_printer", () -> new BlockItem(
            BitterBlocks.KEYCARD_PRINTER.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<SCP377> SCP_377 = ITEMS.register("scp_377", () -> new SCP377(
            new Item.Properties()
    ));

    public static final DeferredItem<SCP3771> SCP_377_1 = ITEMS.register("scp_377_1", () -> new SCP3771(
            new Item.Properties()
    ));

    public static final DeferredItem<SCP377Cookie> FORTUNE_COOKIE = ITEMS.register("fortune_cookie", () -> new SCP377Cookie(
            new Item.Properties()
    ));

    public static final DeferredItem<Item> CRACKED_FORTUNE_COOKIE = ITEMS.register("cracked_fortune_cookie", () -> new Item(
            new Item.Properties().food(new FoodProperties(1, 1, true, 1,
                    Optional.empty(), List.of()))
    ));

    public static final DeferredItem<HandheldSystemInterface> HANDHELD_SYSTEM_INTERFACE= ITEMS.register("handheld_system_interface", () -> new HandheldSystemInterface(
            new Item.Properties()
    ));

    public static final DeferredItem<ScrewdriverItem> SCREWDRIVER = ITEMS.register("screwdriver", () -> new ScrewdriverItem(
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> SLIDING_DOOR = ITEMS.register("sliding_door", () -> new BlockItem(
            BitterBlocks.SLIDING_DOOR.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<ChalkItem> CHALK = ITEMS.register("chalk", () -> new ChalkItem(
            new Item.Properties()));

    public static final DeferredItem<HighlighterItem> HIGHLIGHTER = ITEMS.register("highlighter", () -> new HighlighterItem(
            new Item.Properties()));

    public static final DeferredItem<StickyNote> STICKY_NOTE = ITEMS.register("sticky_note", () -> new StickyNote(
            BitterBlocks.STICKY_NOTE.get(),
            new Item.Properties().stacksTo(16)
    ));

    public static final DeferredItem<Item> PEN = ITEMS.register("pen", () -> new Item(
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> KEYCARD_READER = ITEMS.register("keycard_reader", () -> new BlockItem(
            BitterBlocks.KEYCARD_READER.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> REDSTONE_DEVICE = ITEMS.register("redstone_device", () -> new BlockItem(
            BitterBlocks.REDSTONE_DEVICE.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<WireCuttersItem> WIRE_CUTTERS = ITEMS.register("wire_cutters", () -> new WireCuttersItem(
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> EMERGENCY_EXIT_LAMP = ITEMS.register("emergency_exit_lamp", () -> new BlockItem(
            BitterBlocks.EMERGENCY_EXIT_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> CAGE_LAMP = ITEMS.register("cage_lamp", () -> new BlockItem(
            BitterBlocks.CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> RED_CAGE_LAMP = ITEMS.register("red_cage_lamp", () -> new BlockItem(
            BitterBlocks.RED_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> BLUE_CAGE_LAMP = ITEMS.register("blue_cage_lamp", () -> new BlockItem(
            BitterBlocks.BLUE_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> GREEN_CAGE_LAMP = ITEMS.register("green_cage_lamp", () -> new BlockItem(
            BitterBlocks.GREEN_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> YELLOW_CAGE_LAMP = ITEMS.register("yellow_cage_lamp", () -> new BlockItem(
            BitterBlocks.YELLOW_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> ORANGE_CAGE_LAMP = ITEMS.register("orange_cage_lamp", () -> new BlockItem(
            BitterBlocks.ORANGE_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> PURPLE_CAGE_LAMP = ITEMS.register("purple_cage_lamp", () -> new BlockItem(
            BitterBlocks.PURPLE_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> LIME_CAGE_LAMP = ITEMS.register("lime_cage_lamp", () -> new BlockItem(
            BitterBlocks.LIME_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> PINK_CAGE_LAMP = ITEMS.register("pink_cage_lamp", () -> new BlockItem(
            BitterBlocks.PINK_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> MAGENTA_CAGE_LAMP = ITEMS.register("magenta_cage_lamp", () -> new BlockItem(
            BitterBlocks.MAGENTA_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> CYAN_CAGE_LAMP = ITEMS.register("cyan_cage_lamp", () -> new BlockItem(
            BitterBlocks.CYAN_CAGE_LAMP.get(),
            new Item.Properties()
    ));

    public static final DeferredItem<BlockItem> LIGHT_BLUE_CAGE_LAMP = ITEMS.register("light_blue_cage_lamp", () -> new BlockItem(
            BitterBlocks.LIGHT_BLUE_CAGE_LAMP.get(),
            new Item.Properties()
    ));
}
