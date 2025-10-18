package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.base.structuralblock.StructuralBlockRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.client.PhoneCordRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client.LargeSlidingDoorRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.slidingdoor.client.SlidingDoorRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.thermometer.client.ThermometerRenderer;
import com.site21.bittermelon.common.content.blocks.substance.fluid.client.FluidBlockColor;
import com.site21.bittermelon.common.content.blocks.wallwriting.client.WallWritingRenderer;
import com.site21.bittermelon.common.content.entities.chicken.client.ChickenRenderer;
import com.site21.bittermelon.common.content.entities.scp131.client.SCP131Renderer;
import com.site21.bittermelon.common.content.entities.scp1507.client.SCP1507Renderer;
import com.site21.bittermelon.common.content.entities.scp650.client.SCP650Renderer;
import com.site21.bittermelon.common.content.entities.scp939.client.SCP939Renderer;
import com.site21.bittermelon.common.content.items.taser.TaserProjectileRenderer;
import com.site21.bittermelon.common.systems.personnel.privilege.networking.*;
import com.site21.bittermelon.common.systems.personnel.registry.networking.AddPersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.networking.PersonnelClientPayloadHandler;
import com.site21.bittermelon.common.systems.personnel.registry.networking.RemovePersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.networking.UpdatePersonnelEntry;
import com.site21.bittermelon.datagen.property.BaseColor;
import com.site21.bittermelon.datagen.property.StackPillShape;
import com.site21.bittermelon.datagen.property.SubstanceColor;
import com.site21.bittermelon.datagen.property.SubstanceVolume;
import com.site21.bittermelon.init.neoforge.BitterBlockEntities;
import com.site21.bittermelon.networking.client.ClientPayloadHandler;
import com.site21.bittermelon.networking.server.SetLastTypingTime;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.neoforge.BitterEntities.*;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {

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
        event.registerBlockEntityRenderer(BitterBlockEntities.STRUCTURAL_BLOCK_ENTITY.get(), StructuralBlockRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.THERMOMETER_BLOCK_ENTITY.get(), ThermometerRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.INTERCOM_BLOCK_ENTITY.get(), PhoneCordRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.LARGE_SLIDING_DOOR_BLOCK_ENTITY.get(), LargeSlidingDoorRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.WALL_WRITING_BLOCK_ENTITY.get(), WallWritingRenderer::new);
    }

    @SubscribeEvent
    public static void registerColorHandlers(RegisterColorHandlersEvent.@NotNull Block event) {
        event.register(new FluidBlockColor(), FLUID.get());
    }

    @SubscribeEvent
    public static void registerRangeProperties(@NotNull RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "substance_volume"),
                SubstanceVolume.MAP_CODEC
        );
    }

    @SubscribeEvent
    public static void registerItemTintSources(RegisterColorHandlersEvent.@NotNull ItemTintSources event) {
        event.register(
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "substance_color"),
                SubstanceColor.MAP_CODEC
        );

        event.register(
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "base_color"),
                BaseColor.MAP_CODEC
        );
    }

    @SubscribeEvent
    public static void registerSelectProperties(@NotNull RegisterSelectItemModelPropertyEvent event) {
        event.register(
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "pill_shape"),
                StackPillShape.TYPE
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
    }

}
