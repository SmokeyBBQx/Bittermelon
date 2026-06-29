package com.site21.bittermelon;

import com.mojang.logging.LogUtils;
import com.site21.bittermelon.common.systems.chemistry.ReactionLoader;
import com.site21.bittermelon.init.neoforge.BitterEntities;
import com.site21.bittermelon.init.neoforge.BitterRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import static com.site21.bittermelon.init.custom.Anatomies.ANATOMIES;
import static com.site21.bittermelon.init.custom.Compartments.COMPARTMENTS;
import static com.site21.bittermelon.init.custom.Drugs.DRUGS;
import static com.site21.bittermelon.init.custom.LogicalOperators.LOGICAL_OPERATORS;
import static com.site21.bittermelon.init.custom.Medias.MEDIA;
import static com.site21.bittermelon.init.custom.ReactionConditions.REACTION_CONDITION_TYPES;
import static com.site21.bittermelon.init.custom.ReactionEffects.REACTION_EFFECT_TYPES;
import static com.site21.bittermelon.init.custom.Roles.ROLES;
import static com.site21.bittermelon.init.custom.Substances.SUBSTANCES;
import static com.site21.bittermelon.init.custom.VerbSets.VERB_SETS;
import static com.site21.bittermelon.init.neoforge.BitterActivity.ACTIVITY;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ATTACHMENT_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.BLOCK_ENTITY_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.BLOCKS;
import static com.site21.bittermelon.init.neoforge.BitterCreativeTabs.CREATIVE_MODE_TABS;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.DATA_COMPONENTS;
import static com.site21.bittermelon.init.neoforge.BitterDataSerializers.ENTITY_DATA_SERIALIZERS;
import static com.site21.bittermelon.init.neoforge.BitterFluidTypes.FLUID_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterFluids.FLUIDS;
import static com.site21.bittermelon.init.neoforge.BitterGameRules.GAME_RULES;
import static com.site21.bittermelon.init.neoforge.BitterItems.ITEMS;
import static com.site21.bittermelon.init.neoforge.BitterMemoryTypes.MEMORY_MODULE_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterMenus.MENUS;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.MOB_EFFECTS;
import static com.site21.bittermelon.init.neoforge.BitterParticles.PARTICLES;
import static com.site21.bittermelon.init.neoforge.BitterSensors.SENSOR_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterSounds.SOUND_EVENTS;

@Mod(Bittermelon.MOD_ID)
public class Bittermelon {
    public static boolean shouldDisplayText = false;
    public static final String MOD_ID = "bittermelon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Bittermelon(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);

        BitterEntities.register(modEventBus);

        ITEMS.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ACTIVITY.register(modEventBus);
        SUBSTANCES.register(modEventBus);
        MEMORY_MODULE_TYPES.register(modEventBus);
        SENSOR_TYPES.register(modEventBus);
        VERB_SETS.register(modEventBus);
        LOGICAL_OPERATORS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENUS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        MOB_EFFECTS.register(modEventBus);
        COMPARTMENTS.register(modEventBus);
        DRUGS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ROLES.register(modEventBus);
        FLUIDS.register(modEventBus);
        FLUID_TYPES.register(modEventBus);
        MEDIA.register(modEventBus);
        PARTICLES.register(modEventBus);
        ANATOMIES.register(modEventBus);
        ENTITY_DATA_SERIALIZERS.register(modEventBus);
        REACTION_CONDITION_TYPES.register(modEventBus);
        REACTION_EFFECT_TYPES.register(modEventBus);
        GAME_RULES.register(modEventBus);

        modEventBus.addListener(BitterRegistries::registerRegistries);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final @NotNull FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(Bittermelon.identifier("reactions"), new ReactionLoader(event.getRegistryAccess()));
    }

    @Contract("_ -> new")
    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}