package com.site21.bittermelon;

import com.mojang.logging.LogUtils;
import com.site21.bittermelon.common.systems.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.character.networking.SyncActiveCharacter;
import com.site21.bittermelon.common.systems.character.networking.SyncCharacters;
import com.site21.bittermelon.common.systems.chemistry.Reaction;
import com.site21.bittermelon.common.systems.chemistry.ReactionManager;
import com.site21.bittermelon.common.systems.chemistry.Reagent;
import com.site21.bittermelon.common.systems.chemistry.effects.ExplosionEffect;
import com.site21.bittermelon.common.systems.chemistry.effects.ProductionEffect;
import com.site21.bittermelon.common.systems.substance.Nature;
import com.site21.bittermelon.common.systems.telecomms.intercom.IntercomManager;
import com.site21.bittermelon.common.systems.telecomms.intercom.networking.SyncIntercomList;
import com.site21.bittermelon.init.custom.Substances;
import com.site21.bittermelon.init.neoforge.BitterEntities;
import com.site21.bittermelon.init.neoforge.BitterRegistries;
import com.site21.bittermelon.networking.server.SetLastTypingTime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;

import static com.site21.bittermelon.init.custom.Anatomies.ANATOMIES;
import static com.site21.bittermelon.init.custom.Compartments.COMPARTMENTS;
import static com.site21.bittermelon.init.custom.Drugs.DRUGS;
import static com.site21.bittermelon.init.custom.LogicalOperators.LOGICAL_OPERATORS;
import static com.site21.bittermelon.init.custom.Medias.MEDIA;
import static com.site21.bittermelon.init.custom.Roles.ROLES;
import static com.site21.bittermelon.init.custom.Substances.SUBSTANCES;
import static com.site21.bittermelon.init.custom.Substances.WATER;
import static com.site21.bittermelon.init.custom.VerbSets.VERB_SETS;
import static com.site21.bittermelon.init.neoforge.BitterActivity.ACTIVITY;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ATTACHMENT_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.LAST_TYPING_TIME;
import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.BLOCK_ENTITY_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.BLOCKS;
import static com.site21.bittermelon.init.neoforge.BitterCreativeTabs.CREATIVE_MODE_TABS;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.DATA_COMPONENTS;
import static com.site21.bittermelon.init.neoforge.BitterDataSerializers.ENTITY_DATA_SERIALIZERS;
import static com.site21.bittermelon.init.neoforge.BitterFluidTypes.FLUID_TYPES;
import static com.site21.bittermelon.init.neoforge.BitterFluids.FLUIDS;
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

        modEventBus.addListener(BitterRegistries::registerRegistries);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final @NotNull FMLCommonSetupEvent event) {
        ReactionManager reactionManager = ReactionManager.getInstance();
        Reaction testReaction = new Reaction(
                List.of(
                        new Reagent.Builder().addNatureRequirement(Nature.STRONG_ACID, 0.5f).build(),
                        new Reagent.Builder().addNatureRequirement(Nature.BASE, 0.5f).build()),
                List.of(),
                0,
                0,
                List.of(new ProductionEffect()
                        .addProduct(Substances.KOOL_AID.get(), 2)
                        .addProduct(Substances.HYDROGEN_CYANIDE.get(), 1)
                )
        );

        Reaction explosionReaction = new Reaction(
                List.of(
                        new Reagent.Builder().addNatureRequirement(Nature.WEAK_ACID, 0.5f).build(),
                        new Reagent.Builder().addSubstanceRequirement(WATER.get()).build()),
                List.of(),
                0,
                0,
                List.of(new ExplosionEffect())
        );

        Reaction redIceConversion = new Reaction(
                List.of(
                        new Reagent.Builder().proportion(0).addSubstanceRequirement(Substances.RED_ICE.get()).build(),
                        new Reagent.Builder().addNatureRequirement(Nature.WATER_BASED, 0f).build()
                ),
                List.of(),
                Integer.MIN_VALUE,
                0,
                List.of(new ProductionEffect().addProduct(Substances.RED_ICE.get(), 1))
        );

        reactionManager.register(testReaction);
        reactionManager.register(explosionReaction);
        reactionManager.register(redIceConversion);
        reactionManager.build();
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

        Entity entity = event.getEntity();

        if (entity.getExistingDataOrNull(LAST_TYPING_TIME) != null) {
            long lastTypingTime = entity.getData(LAST_TYPING_TIME);
            long timeSinceTyping = System.currentTimeMillis() - lastTypingTime;

            if (timeSinceTyping > 5000) {
                entity.removeData(LAST_TYPING_TIME);
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new SetLastTypingTime(entity.getUUID(), -1));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.@NotNull PlayerLoggedInEvent event) {
//        shouldDisplayText = true;
//        LoreOpeningOverlay.displayStartTime = System.currentTimeMillis();
//        event.getEntity().playNotifySound(LOW_IMPACT.get(), SoundSource.MASTER, 1, 1);
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            Level level = serverPlayer.level();

            AtmosLevelData.get(level).syncToClient();
            PacketDistributor.sendToPlayer(serverPlayer, new SyncIntercomList(IntercomManager.get(level).getIntercomIDs()));

            CharacterManager characterManager = CharacterManager.get(level);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncCharacters(characterManager.getCharacters()));

            Character activeCharacter = characterManager.getActiveCharacter(serverPlayer);
            if (activeCharacter != null) {
                PacketDistributor.sendToPlayer(serverPlayer, new SyncActiveCharacter(activeCharacter.getId()));
            }
        }
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}