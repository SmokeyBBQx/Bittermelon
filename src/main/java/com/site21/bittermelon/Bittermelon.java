package com.site21.bittermelon;

import com.site21.bittermelon.client.colorhandlers.FluidBlockColor;
import com.site21.bittermelon.client.renderer.entity.SCP939Renderer;
import com.site21.bittermelon.commands.CommandSubstance;
import com.site21.bittermelon.init.*;
import com.site21.bittermelon.substance.reactions.Reactions;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
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

import static com.site21.bittermelon.init.ActivityInit.ACTIVITY;
import static com.site21.bittermelon.init.BlockEntityInit.BLOCK_ENTITY_TYPES;
import static com.site21.bittermelon.init.BlockInit.BLOCKS;
import static com.site21.bittermelon.init.BlockInit.FLUID;
import static com.site21.bittermelon.init.DataComponentsInit.DATA_COMPONENTS;
import static com.site21.bittermelon.init.EntityInit.ENTITY_TYPES;
import static com.site21.bittermelon.init.EntityInit.SCP_939;
import static com.site21.bittermelon.init.MemoryModuleTypeInit.MEMORY_MODULE_TYPES;
import static com.site21.bittermelon.init.ReactionInit.REACTIONS;
import static com.site21.bittermelon.init.SubstanceInit.SUBSTANCES;
import static net.minecraft.core.registries.Registries.MEMORY_MODULE_TYPE;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Bittermelon.MOD_ID)
public class Bittermelon
{
    public static final String MOD_ID = "bittermelon";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
//    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
//    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
//    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
//    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
//            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Bittermelon(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);


        NeoForge.EVENT_BUS.register(this);


        BLOCKS.register(modEventBus);
        ACTIVITY.register(modEventBus);
        MEMORY_MODULE_TYPES.register(modEventBus);
        EntityInit.register(modEventBus);
        SUBSTANCES.register(modEventBus);
        REACTIONS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);

        modEventBus.addListener(ModRegistries::registerRegistries);
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
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandSubstance.register(event.getDispatcher());
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
        public static void registerColorHandlers(RegisterColorHandlersEvent.Block event) {
            event.register(new FluidBlockColor(), FLUID.get());
        }
    }
}
