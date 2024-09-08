package net.smokeybbq.bittermelon;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.client.colorhandlers.PuddleBlockColor;
import net.smokeybbq.bittermelon.client.renderer.PuddleFallingBlockRenderer;
import net.smokeybbq.bittermelon.commands.*;
import net.smokeybbq.bittermelon.commands.channel.CommandChannel;
import net.smokeybbq.bittermelon.commands.character.*;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.smokeybbq.bittermelon.chat.ChatEventHandler;
import net.smokeybbq.bittermelon.systems.atmospherics.AtmosEventHandler;
import net.smokeybbq.bittermelon.systems.atmospherics.AtmosManager;
import net.smokeybbq.bittermelon.systems.atmospherics.AtmosRenderer;
import net.smokeybbq.bittermelon.events.PlayerEventHandler;
import net.smokeybbq.bittermelon.systems.throwing.ThrowKeyHandler;
import net.smokeybbq.bittermelon.init.ModCapabilities;
import net.smokeybbq.bittermelon.init.ModKeyBindings;
import net.smokeybbq.bittermelon.init.ModRegistries;
import net.smokeybbq.bittermelon.init.ModScreens;
import net.smokeybbq.bittermelon.items.handlabeler.HandLabelerScreen;
import net.smokeybbq.bittermelon.items.radio.RadioKeyHandler;
import net.smokeybbq.bittermelon.networking.PacketHandler;
import org.slf4j.Logger;

import static net.smokeybbq.bittermelon.init.BlockEntityInit.BLOCK_ENTITIES;
import static net.smokeybbq.bittermelon.init.BlockInit.BLOCKS;
import static net.smokeybbq.bittermelon.init.BlockInit.PUDDLE;
import static net.smokeybbq.bittermelon.init.EntityInit.*;
import static net.smokeybbq.bittermelon.init.ItemInit.ITEMS;
import static net.smokeybbq.bittermelon.init.MenuInit.*;
import static net.smokeybbq.bittermelon.init.SoundInit.SOUND_EVENTS;
import static net.smokeybbq.bittermelon.init.SubstanceInit.SUBSTANCES;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Bittermelon.MODID)
public class Bittermelon {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "bittermelon";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Bittermelon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(ModRegistries::registerRegistries);
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        MinecraftForge.EVENT_BUS.register(new ChatEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new ThrowKeyHandler());
        MinecraftForge.EVENT_BUS.register(new RadioKeyHandler());
        MinecraftForge.EVENT_BUS.register(new AtmosEventHandler());
        // MinecraftForge.EVENT_BUS.register(new SkinChangeHandler());
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ENTITIES.register(modEventBus);
        SUBSTANCES.register(modEventBus);
        MENUS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        ModCapabilities.register(event);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        CharacterManager.setMinecraftServer(event.getServer());
        AtmosManager.init(event.getServer().getLevel(Level.OVERWORLD));
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer serverPlayer) {
            if (CharacterManager.getActiveCharacter(serverPlayer.getUUID()) != null) {
                CharacterManager.getActiveCharacter(serverPlayer.getUUID()).update();
            }
        }
//        DirtyBlocksHandler.onPlayerTick(event);
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            AtmosManager.getInstance().tick();
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandChannel.register(event.getDispatcher());
        CommandCharacter.register(event.getDispatcher());
        CommandStumble.register(event.getDispatcher());
        CommandAdministerDrugOral.register(event.getDispatcher());
        CommandCondition.register(event.getDispatcher());
        CommandAddTumor.register(event.getDispatcher());
        CommandAddSubstance.register(event.getDispatcher());
        CommandStartScreenshake.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ModKeyBindings.register(event);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ModScreens.register();
                EntityRenderers.register(PUDDLE_FALLING_BLOCK.get(), PuddleFallingBlockRenderer::new);
                EntityRenderers.register(THROWN_ITEM_PROJECTILE.get(), ThrownItemRenderer::new);
                MenuScreens.register(HAND_LABELER_MENU.get(), HandLabelerScreen::new);
                MinecraftForge.EVENT_BUS.register(AtmosRenderer.class);
            });
        }

        @SubscribeEvent
        public static void registerColorHandlers(RegisterColorHandlersEvent.Block event) {
            event.register(new PuddleBlockColor(), PUDDLE.get());
        }
    }

}
