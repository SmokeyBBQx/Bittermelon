package net.smokeybbq.bittermelon;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.commands.CommandAdministerDrugOral;
import net.smokeybbq.bittermelon.commands.CommandCondition;
import net.smokeybbq.bittermelon.commands.CommandStumble;
import net.smokeybbq.bittermelon.commands.channel.CommandChannel;
import net.smokeybbq.bittermelon.commands.character.*;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.smokeybbq.bittermelon.events.ChatEventHandler;
import net.smokeybbq.bittermelon.events.PlayerEventHandler;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Bittermelon.MODID)
public class Bittermelon
{
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

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        MinecraftForge.EVENT_BUS.register(new ChatEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        // MinecraftForge.EVENT_BUS.register(new SkinChangeHandler());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        CharacterManager.setMinecraftServer(event.getServer());
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) event.player;
            if (CharacterManager.getActiveCharacter(serverPlayer.getUUID()) != null) {
                CharacterManager.getActiveCharacter(serverPlayer.getUUID()).update();
            }
        }
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandChannel.register(event.getDispatcher());
        CommandCharacter.register(event.getDispatcher());
        CommandStumble.register(event.getDispatcher());
        CommandAdministerDrugOral.register(event.getDispatcher());
        CommandCondition.register(event.getDispatcher());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
