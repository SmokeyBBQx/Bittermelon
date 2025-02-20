package com.site21.bittermelon;

import com.site21.bittermelon.content.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.content.atmosphere.networking.SyncAtmosInstances;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.blocks.substance.fluid.client.FluidBlockColor;
import com.site21.bittermelon.client.gui.loreopening.LoreOpeningOverlay;
import com.site21.bittermelon.content.character.networking.SyncCharacters;
import com.site21.bittermelon.content.telecomms.intercom.IntercomManager;
import com.site21.bittermelon.content.telecomms.intercom.networking.SyncIntercomList;
import com.site21.bittermelon.init.*;
import com.site21.bittermelon.content.substance.reactions.Reactions;
import com.site21.bittermelon.util.ServerUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

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
import static com.site21.bittermelon.init.BitterSensors.SENSOR_TYPES;
import static com.site21.bittermelon.init.BitterSounds.LOW_IMPACT;
import static com.site21.bittermelon.init.BitterSounds.SOUND_EVENTS;
import static com.site21.bittermelon.init.Substances.SUBSTANCES;
import static com.site21.bittermelon.init.VerbSets.VERB_SETS;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Bittermelon.MOD_ID)
public class Bittermelon
{
    public static boolean shouldDisplayText = false;
    public static final String MOD_ID = "bittermelon";
    public static final Logger LOGGER = LogUtils.getLogger();

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
        VERB_SETS.register(modEventBus);
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
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
        event.enqueueWork(Reactions::initReactions);
    }

    @SubscribeEvent
    public void onServerStarting(@NotNull ServerStartingEvent event)
    {
        ServerUtil.setMinecraftServer(event.getServer());
    }

    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.@NotNull Post event) {
        Level level = event.getEntity().level();
        if (level.isClientSide) return;
       Character character = CharacterManager.get(level).getActiveCharacter(event.getEntity());
       if (character != null) character.update();

    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.@NotNull PlayerLoggedInEvent event) {
        shouldDisplayText = true;
        LoreOpeningOverlay.displayStartTime = System.currentTimeMillis();
        event.getEntity().playNotifySound(LOW_IMPACT.get(), SoundSource.MASTER, 1, 1);
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            AtmosLevelData.get(serverPlayer.level()).syncToClient();
            PacketDistributor.sendToPlayer(serverPlayer, new SyncIntercomList(IntercomManager.get(serverPlayer.level()).getIntercomIDs()));
            PacketDistributor.sendToPlayer(serverPlayer, new SyncCharacters(CharacterManager.get(serverPlayer.level()).getCharacters()));
        }
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }

        @SubscribeEvent
        public static void registerColorHandlers(RegisterColorHandlersEvent.@NotNull Block event) {
            event.register(new FluidBlockColor(), FLUID.get());
        }
    }
}
