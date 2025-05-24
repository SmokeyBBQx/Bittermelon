package com.site21.bittermelon.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.base.structuralblock.client.StructuralBlockRenderer;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.client.PhoneCordRenderer;
import com.site21.bittermelon.content.blocks.devices.implementations.thermometer.client.ThermometerRenderer;
import com.site21.bittermelon.content.entities.implementations.chicken.client.ChickenRenderer;
import com.site21.bittermelon.content.entities.implementations.SCP939.client.SCP939Renderer;
import com.site21.bittermelon.init.neoforge.BitterBlockEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.LIT;
import static com.site21.bittermelon.init.neoforge.BitterEntities.*;
import static com.site21.bittermelon.init.neoforge.BitterItems.CIGARETTE;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ItemProperties.register(
                CIGARETTE.get(),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "lit"),
                (stack, level, entity, seed) -> Boolean.TRUE.equals(stack.get(LIT)) ? 1f : 0f
        );
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.@NotNull RegisterRenderers event) {
        event.registerEntityRenderer(SCP_939.get(), SCP939Renderer::new);
        event.registerEntityRenderer(CHICKEN.get(), ChickenRenderer::new);
        event.registerEntityRenderer(THROWN_ITEM_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(SCP_2398_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.STRUCTURAL_BLOCK_ENTITY.get(), StructuralBlockRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.THERMOMETER_BLOCK_ENTITY.get(), ThermometerRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.INTERCOM_BLOCK_ENTITY.get(), PhoneCordRenderer::new);
    }
}
