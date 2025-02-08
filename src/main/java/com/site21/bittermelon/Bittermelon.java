package com.site21.bittermelon;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.client.colorhandlers.FluidBlockColor;
import com.site21.bittermelon.client.gui.loreopening.LoreOpeningOverlay;
import com.site21.bittermelon.init.*;
import com.site21.bittermelon.substance.reactions.Reactions;
import com.site21.bittermelon.util.ServerUtil;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static com.site21.bittermelon.init.BitterActivity.ACTIVITY;
import static com.site21.bittermelon.init.BitterAttachmentTypes.ATTACHMENT_TYPES;
import static com.site21.bittermelon.init.BitterBlockEntities.BLOCK_ENTITY_TYPES;
import static com.site21.bittermelon.init.BitterBlocks.BLOCKS;
import static com.site21.bittermelon.init.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.BitterDataComponents.DATA_COMPONENTS;
import static com.site21.bittermelon.init.BitterMemoryTypes.MEMORY_MODULE_TYPES;
import static com.site21.bittermelon.init.BitterItems.ITEMS;
import static com.site21.bittermelon.init.BitterMenus.MENUS;
import static com.site21.bittermelon.init.BitterMobEffects.MOB_EFFECTS;
import static com.site21.bittermelon.init.BitterReactions.REACTIONS;
import static com.site21.bittermelon.init.BitterSensors.SENSOR_TYPES;
import static com.site21.bittermelon.init.BitterSounds.LOW_IMPACT;
import static com.site21.bittermelon.init.BitterSounds.SOUND_EVENTS;
import static com.site21.bittermelon.init.Substances.SUBSTANCES;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Bittermelon.MOD_ID)
public class Bittermelon
{
    public static boolean shouldDisplayText = false;
    public static final String MOD_ID = "bittermelon";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
//    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
//    // Creates a new PlaceableItem with the id "examplemod:example_block", combining the namespace and path
//    public static final DeferredItem<PlaceableItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
//    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
//            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Bittermelon(IEventBus modEventBus, @NotNull ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);


        NeoForge.EVENT_BUS.register(this);


        BLOCKS.register(modEventBus);
        ACTIVITY.register(modEventBus);
        ITEMS.register(modEventBus);
        MEMORY_MODULE_TYPES.register(modEventBus);
        SENSOR_TYPES.register(modEventBus);
        BitterEntities.register(modEventBus);
        SUBSTANCES.register(modEventBus);
        REACTIONS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        MENUS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        MOB_EFFECTS.register(modEventBus);

        modEventBus.addListener(BitterRegistries::registerRegistries);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
        event.enqueueWork(Reactions::initReactions);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(@NotNull ServerStartingEvent event)
    {
        ServerUtil.setMinecraftServer(event.getServer());
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.@NotNull Post event) {
       Character character = CharacterManager.getInstance().getActiveCharacter(event.getEntity().getUUID());
       if (character != null) character.update();
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        shouldDisplayText = true;
        LoreOpeningOverlay.displayStartTime = System.currentTimeMillis();
        event.getEntity().playNotifySound(LOW_IMPACT.get(), SoundSource.MASTER, 1, 1);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void registerColorHandlers(RegisterColorHandlersEvent.@NotNull Block event) {
            event.register(new FluidBlockColor(), FLUID.get());
        }
    }
}
