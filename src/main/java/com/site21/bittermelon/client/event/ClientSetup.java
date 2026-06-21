package com.site21.bittermelon.client.event;

import com.google.common.reflect.TypeToken;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.particles.PlasticParticle;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.client.PhoneCordRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client.LargeSlidingDoorRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.slidingdoor.client.SlidingDoorRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.television.MediaSheets;
import com.site21.bittermelon.common.content.blocks.electronics.television.client.TelevisionRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.thermometer.client.ThermometerRenderer;
import com.site21.bittermelon.common.content.blocks.flamingo.FlamingoBlockRenderer;
import com.site21.bittermelon.common.content.blocks.wallwriting.client.WallWritingRenderer;
import com.site21.bittermelon.common.content.entities.cage.client.CageRenderer;
import com.site21.bittermelon.common.content.entities.chicken.client.ChickenRenderer;
import com.site21.bittermelon.common.content.entities.fluidprojectile.FluidProjectileRenderer;
import com.site21.bittermelon.common.content.entities.mimicplayer.Mimic;
import com.site21.bittermelon.common.content.entities.mimicplayer.client.MimicRenderer;
import com.site21.bittermelon.common.content.entities.scp025fr.client.SCP025FRRenderer;
import com.site21.bittermelon.common.content.entities.scp131.client.SCP131Renderer;
import com.site21.bittermelon.common.content.entities.scp1507.client.SCP1507Renderer;
import com.site21.bittermelon.common.content.entities.scp548.client.SCP548Renderer;
import com.site21.bittermelon.common.content.entities.scp650.client.SCP650Renderer;
import com.site21.bittermelon.common.content.entities.scp718.client.EyeballOnPlayerLayer;
import com.site21.bittermelon.common.content.entities.scp718.client.SCP718Renderer;
import com.site21.bittermelon.common.content.entities.scp815snake.client.SCP815SnakeRenderer;
import com.site21.bittermelon.common.content.entities.scp939.client.SCP939Renderer;
import com.site21.bittermelon.common.content.entities.seamonkey.client.SeaMonkeyRenderer;
import com.site21.bittermelon.common.content.items.keycard.KeycardDecorator;
import com.site21.bittermelon.common.content.items.repairtool.RepairToolUseAnimation;
import com.site21.bittermelon.common.content.items.taser.TaserProjectileRenderer;
import com.site21.bittermelon.common.systems.carry.CarryHandler;
import com.site21.bittermelon.common.systems.character.networking.UpdateCharacter;
import com.site21.bittermelon.common.systems.component.screwdriver.ScrewdriverUseAnimation;
import com.site21.bittermelon.common.systems.component.temperature.HeatDecorator;
import com.site21.bittermelon.common.systems.fluid.simple.ClientSimpleFluid;
import com.site21.bittermelon.common.systems.fluid.substance.ClientSubstanceFluid;
import com.site21.bittermelon.common.systems.medical.client.AnatomyPictureInPictureRenderer;
import com.site21.bittermelon.common.systems.medical.client.compartmentrenderers.CompartmentRenderers;
import com.site21.bittermelon.common.systems.medical.client.tool.InstrumentWidgets;
import com.site21.bittermelon.common.systems.medical.networking.AddAndInsertCompartment;
import com.site21.bittermelon.common.systems.medical.networking.InsertCompartment;
import com.site21.bittermelon.common.systems.medical.networking.RemoveCompartment;
import com.site21.bittermelon.common.systems.medical.networking.UpdateCompartments;
import com.site21.bittermelon.common.systems.personnel.privilege.networking.*;
import com.site21.bittermelon.common.systems.personnel.registry.networking.AddPersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.networking.PersonnelClientPayloadHandler;
import com.site21.bittermelon.common.systems.personnel.registry.networking.RemovePersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.networking.UpdatePersonnelEntry;
import com.site21.bittermelon.common.systems.rage.client.RageRenderer;
import com.site21.bittermelon.datagen.property.*;
import com.site21.bittermelon.init.neoforge.BitterBlockEntities;
import com.site21.bittermelon.init.neoforge.BitterItems;
import com.site21.bittermelon.init.neoforge.BitterMobEffects;
import com.site21.bittermelon.init.neoforge.BitterParticles;
import com.site21.bittermelon.networking.client.ClientPayloadHandler;
import com.site21.bittermelon.networking.server.SetLastTypingTime;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;
import static com.site21.bittermelon.init.neoforge.BitterEntities.*;
import static com.site21.bittermelon.init.neoforge.BitterFluidTypes.SIMPLE_FLUID_TYPE;
import static com.site21.bittermelon.init.neoforge.BitterFluidTypes.SUBSTANCE_FLUID_TYPE;
import static com.site21.bittermelon.init.neoforge.BitterFluids.SUBSTANCE_FLUID;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {
    private static final Map<PlayerSkin.Model, EntityRendererProvider<Mimic>> MIMIC_PROVIDERS = Map.of(
            PlayerSkin.Model.WIDE, p_174098_ -> new MimicRenderer(p_174098_, false), PlayerSkin.Model.SLIM, p_174096_ -> new MimicRenderer(p_174096_, true)
    );

    public static final ContextKey<Float> ENTITY_WIDTH = new ContextKey<>(
            Bittermelon.identifier("entity_width")
    );

    public static final ContextKey<Map<String, Boolean>> LIMB_VISIBILITY = new ContextKey<>(
            Bittermelon.identifier("limb_visibility")
    );

    public static final ContextKey<Boolean> EYEBALL_GROWTH = new ContextKey<>(
            Bittermelon.identifier("eyeball_growth")
    );

    @SubscribeEvent
    public static void fmlSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(SUBSTANCE_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);
        CompartmentRenderers.register();
        InstrumentWidgets.register();
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.@NotNull RegisterRenderers event) {
        event.registerEntityRenderer(SCP_939.get(), SCP939Renderer::new);
        event.registerEntityRenderer(CHICKEN.get(), ChickenRenderer::new);
        event.registerEntityRenderer(THROWN_ITEM_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(SCP_2398_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(SCP_650.get(), SCP650Renderer::new);
        event.registerEntityRenderer(SCP_131.get(), SCP131Renderer::new);
        event.registerEntityRenderer(SCP_1507.get(), SCP1507Renderer::new);
        event.registerEntityRenderer(TASER_PROJECTILE.get(), TaserProjectileRenderer::new);
        event.registerEntityRenderer(SCP_548.get(), SCP548Renderer::new);
        event.registerEntityRenderer(SCP_815_SNAKE.get(), SCP815SnakeRenderer::new);
        event.registerEntityRenderer(CAGE.get(), CageRenderer::new);
        event.registerEntityRenderer(SEA_MONKEY.get(), SeaMonkeyRenderer::new);
        event.registerEntityRenderer(SCP_718.get(), SCP718Renderer::new);
        event.registerEntityRenderer(FLUID_PROJECTILE.get(), FluidProjectileRenderer::new);
        MIMIC_PROVIDERS.forEach(
                (model, provider) -> event.registerEntityRenderer(MIMIC.get(), provider)
        );
        event.registerEntityRenderer(SCP_025_FR.get(), SCP025FRRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.THERMOMETER_BLOCK_ENTITY.get(), ThermometerRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.INTERCOM_BLOCK_ENTITY.get(), PhoneCordRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.LARGE_SLIDING_DOOR_BLOCK_ENTITY.get(), LargeSlidingDoorRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.WALL_WRITING_BLOCK_ENTITY.get(), WallWritingRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.TELEVISION_BLOCK_ENTITY.get(), TelevisionRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.PLASTIC_FLAMINGO_BLOCK_ENTITY.get(), FlamingoBlockRenderer::new);

    }

    @SubscribeEvent
    public static void registerRenderStateModifiers(@NotNull RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(
                PlayerRenderer.class,
                (entity, state) -> {
                    Entity carriedEntity = CarryHandler.getCarried(entity);
                    if (carriedEntity == null) {
                        state.setRenderData(ENTITY_WIDTH, 0f);
                        return;
                    }

                    float entityWidth = carriedEntity.getBbWidth();
                    state.setRenderData(ENTITY_WIDTH, entityWidth);
                }
        );

        event.registerEntityModifier(
                new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {},
                (entity, state) -> {
                    Map<String, Boolean> limbVisibility = null;
                    if (entity.hasData(MEDICAL_STATS)) {
                        limbVisibility = entity.getData(MEDICAL_STATS).getAnatomyModel().getBodyPartVisibility();
                    }

                    state.setRenderData(LIMB_VISIBILITY, limbVisibility);
                }
        );

        event.registerEntityModifier(
                PlayerRenderer.class,
                (entity, state) -> {
                    MobEffectInstance eyeballEffect = entity.getEffect(BitterMobEffects.EYEBALL_GROWTH);
                    boolean render = eyeballEffect != null && eyeballEffect.getAmplifier() > 0;
                    state.setRenderData(EYEBALL_GROWTH, render);
                }
        );
    }

    @SubscribeEvent
    public static void registerRangeProperties(@NotNull RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "substance_volume"),
                SubstanceVolume.MAP_CODEC
        );
    }

    @SubscribeEvent
    public static void registerItemTintSources(RegisterColorHandlersEvent.@NotNull ItemTintSources event) {
        event.register(
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "substance_color"),
                SubstanceColor.MAP_CODEC
        );

        event.register(
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "base_color"),
                BaseColor.MAP_CODEC
        );
    }

    @SubscribeEvent
    public static void registerSelectProperties(@NotNull RegisterSelectItemModelPropertyEvent event) {
        event.register(
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "pill_shape"),
                StackPillShape.TYPE
        );
    }

    @SubscribeEvent
    public static void registerConditionalProperties(@NotNull RegisterConditionalItemModelPropertyEvent event) {
        event.register(
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "smokable_lit"),
                SmokableLit.MAP_CODEC
        );

        event.register(
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "empty_377"),
                Empty377.MAP_CODEC
        );

        event.register(
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_1079"),
                Open1079.MAP_CODEC
        );

        event.register(
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_815"),
                Open815.MAP_CODEC
        );

    }

    @SubscribeEvent
    public static void registerClientExtensions(@NotNull RegisterClientExtensionsEvent event) {
        event.registerItem(
                new ScrewdriverUseAnimation(),
                BitterItems.SCREWDRIVER
        );

        event.registerItem(
                new RepairToolUseAnimation(),
                BitterItems.REPAIR_TOOL
        );
    }

    @SubscribeEvent
    public static void onRenderInventorySlot(@NotNull RegisterItemDecorationsEvent event) {
        HeatDecorator heatDecorator = new HeatDecorator();
        for (Item item : BuiltInRegistries.ITEM.stream().toList()) {
            event.register(item, heatDecorator);
        }

        event.register(BitterItems.KEYCARD, new KeycardDecorator());
    }

    @SubscribeEvent
    public static void onRegisterFluidTypeExtensions(@NotNull RegisterClientExtensionsEvent event) {
        event.registerFluidType(new ClientSubstanceFluid(), SUBSTANCE_FLUID_TYPE.get());
        event.registerFluidType(new ClientSimpleFluid(), SIMPLE_FLUID_TYPE.get());
    }

    @SubscribeEvent
    public static void registerAtlases(@NotNull RegisterMaterialAtlasesEvent event) {
        event.register(MediaSheets.ATLAS_LOCATION, MediaSheets.ATLAS_INFO_LOCATION);
    }

    @SubscribeEvent
    public static void registerParticleProviders(@NotNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BitterParticles.PLASTIC.get(), PlasticParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerGuiLayers(@NotNull RegisterGuiLayersEvent event) {
        event.registerAbove(Identifier.withDefaultNamespace("camera_overlays"),
                Bittermelon.identifier("rage_overlay"), new RageRenderer());
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new EyeballOnPlayerLayer(playerRenderer, event.getEntityModels()));
            }
        }
    }

    @SubscribeEvent
    public static void registerPictureInPictureRenderers(@NotNull RegisterPictureInPictureRenderersEvent event) {
        event.register(
                AnatomyPictureInPictureRenderer.RenderState.class,
                AnatomyPictureInPictureRenderer::new
        );
    }

    @SubscribeEvent
    public static void registerClientPayloadHandlers(@NotNull RegisterClientPayloadHandlersEvent event) {
        event.register(
                SetLastTypingTime.TYPE,
                ClientPayloadHandler::setLastTypingTime
        );

        event.register(
                AddPrivilege.TYPE,
                PrivilegeClientPayloadHandler::handleAddPrivilege
        );

        event.register(
                RemovePrivilege.TYPE,
                PrivilegeClientPayloadHandler::handleRemovePrivilege
        );

        event.register(
                AddPrivilegeGroup.TYPE,
                PrivilegeClientPayloadHandler::handleAddPrivilegeGroup
        );

        event.register(
                RemovePrivilegeGroup.TYPE,
                PrivilegeClientPayloadHandler::handleRemovePrivilegeGroup
        );

        event.register(
                SetPrivilegeForEntry.TYPE,
                PrivilegeClientPayloadHandler::setPrivilegeForEntry
        );

        event.register(
                RemovePrivilegeForEntry.TYPE,
                PrivilegeClientPayloadHandler::removePrivilegeForEntry
        );

        event.register(
                SetPrivilegeForGroup.TYPE,
                PrivilegeClientPayloadHandler::setPrivilegeForGroup
        );

        event.register(
                RemovePrivilegeForGroup.TYPE,
                PrivilegeClientPayloadHandler::removePrivilegeForGroup
        );

        event.register(
                AddPersonnelEntry.TYPE,
                PersonnelClientPayloadHandler::addPersonnelEntry
        );

        event.register(
                RemovePersonnelEntry.TYPE,
                PersonnelClientPayloadHandler::removePersonnelEntry
        );

        event.register(
                UpdatePersonnelEntry.TYPE,
                PersonnelClientPayloadHandler::updatePersonnelEntry
        );

        event.register(
                UpdateCompartments.TYPE,
                UpdateCompartments::handle
        );

        event.register(
                InsertCompartment.TYPE,
                InsertCompartment::handle
        );

        event.register(
                AddAndInsertCompartment.TYPE,
                AddAndInsertCompartment::handle
        );

        event.register(
                RemoveCompartment.TYPE,
                RemoveCompartment::handle
        );

        event.register(
                UpdateCharacter.TYPE,
                UpdateCharacter::handle
        );
    }

    @SubscribeEvent
    public static void onConfigureRenderTarget(@NotNull ConfigureMainRenderTargetEvent event) {
        event.enableStencil();
    }
}
