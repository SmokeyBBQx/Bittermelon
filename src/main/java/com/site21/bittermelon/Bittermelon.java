package com.site21.bittermelon;

import com.site21.bittermelon.content.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.client.gui.loreopening.LoreOpeningOverlay;
import com.site21.bittermelon.content.character.networking.SyncCharacters;
import com.site21.bittermelon.content.telecomms.intercom.IntercomManager;
import com.site21.bittermelon.content.telecomms.intercom.networking.SyncIntercomList;
import com.site21.bittermelon.content.substance.reactions.Reactions;
import com.site21.bittermelon.init.neoforge.BitterEntities;
import com.site21.bittermelon.init.neoforge.BitterRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static com.site21.bittermelon.init.custom.Compartments.COMPARTMENTS;
import static com.site21.bittermelon.init.custom.Drugs.DRUGS;
import static com.site21.bittermelon.init.custom.LogicalOperators.LOGICAL_OPERATORS;
import static com.site21.bittermelon.init.custom.Roles.ROLES;
import static com.site21.bittermelon.init.neoforge.BitterActivity.ACTIVITY;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ATTACHMENT_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.BLOCK_ENTITY_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.BLOCKS;
import static com.site21.bittermelon.init.neoforge.BitterCreativeTabs.CREATIVE_MODE_TABS;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.DATA_COMPONENTS;
import static com.site21.bittermelon.init.neoforge.BitterMemoryTypes.MEMORY_MODULE_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterItems.ITEMS;
import static com.site21.bittermelon.init.neoforge.BitterMenus.MENUS;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.MOB_EFFECTS;
import static com.site21.bittermelon.init.neoforge.BitterSensors.SENSOR_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterSounds.LOW_IMPACT;
import static com.site21.bittermelon.init.neoforge.BitterSounds.SOUND_EVENTS;
import static com.site21.bittermelon.init.custom.Substances.SUBSTANCES;
import static com.site21.bittermelon.init.custom.VerbSets.VERB_SETS;

@Mod(Bittermelon.MOD_ID)
public class Bittermelon {
    public static boolean shouldDisplayText = false;
    public static final String MOD_ID = "bittermelon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Bittermelon(IEventBus modEventBus, @NotNull ModContainer modContainer) {
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
        LOGICAL_OPERATORS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        MENUS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        MOB_EFFECTS.register(modEventBus);
        COMPARTMENTS.register(modEventBus);
        DRUGS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ROLES.register(modEventBus);

        modEventBus.addListener(BitterRegistries::registerRegistries);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final @NotNull FMLCommonSetupEvent event) {
        event.enqueueWork(Reactions::initReactions);
    }

    @SubscribeEvent
    public void onServerStarting(@NotNull ServerStartingEvent event) {
    }

    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.@NotNull Post event) {
        Level level = event.getEntity().level();
        if (level.isClientSide) return;
       Character character = CharacterManager.get(level).getActiveCharacter(event.getEntity());
       if (character != null) character.update(level);
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
}
