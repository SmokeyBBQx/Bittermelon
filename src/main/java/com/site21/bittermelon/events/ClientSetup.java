package com.site21.bittermelon.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.base.structuralblock.client.StructuralBlockRenderer;
import com.site21.bittermelon.content.blocks.devices.thermometer.client.ThermometerRenderer;
import com.site21.bittermelon.content.entities.implementations.chicken.client.ChickenRenderer;
import com.site21.bittermelon.content.entities.implementations.SCP939.client.SCP939Renderer;
import com.site21.bittermelon.init.BitterBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterEntities.CHICKEN;
import static com.site21.bittermelon.init.BitterEntities.SCP_939;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.@NotNull RegisterRenderers event) {
        event.registerEntityRenderer(SCP_939.get(), SCP939Renderer::new);
        event.registerEntityRenderer(CHICKEN.get(), ChickenRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.STRUCTURAL_BLOCK_ENTITY.get(), StructuralBlockRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.THERMOMETER_BLOCK_ENTITY.get(), ThermometerRenderer::new);
    }
}
