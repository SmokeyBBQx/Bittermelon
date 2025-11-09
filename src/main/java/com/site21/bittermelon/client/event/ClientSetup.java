package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.base.structuralblock.client.StructuralBlockRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.client.PhoneCordRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client.LargeSlidingDoorRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.slidingdoor.client.SlidingDoorRenderer;
import com.site21.bittermelon.common.content.blocks.electronics.thermometer.client.ThermometerRenderer;
import com.site21.bittermelon.common.content.blocks.substance.fluid.client.FluidBlockColor;
import com.site21.bittermelon.common.content.blocks.wallwriting.client.WallWritingRenderer;
import com.site21.bittermelon.common.content.entities.implementations.chicken.client.ChickenRenderer;
import com.site21.bittermelon.common.content.entities.implementations.scp131.client.SCP131Renderer;
import com.site21.bittermelon.common.content.entities.implementations.scp1507.client.SCP1507Renderer;
import com.site21.bittermelon.common.content.entities.implementations.scp650.client.SCP650Renderer;
import com.site21.bittermelon.common.content.entities.implementations.scp939.client.SCP939Renderer;
import com.site21.bittermelon.common.content.items.substance.PowderedSubstanceItem;
import com.site21.bittermelon.common.content.items.substance.SubstanceContainerItem;
import com.site21.bittermelon.common.content.items.substance.pill.PillShape;
import com.site21.bittermelon.common.content.items.taser.TaserProjectileRenderer;
import com.site21.bittermelon.init.neoforge.BitterBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client.LargeSlidingDoorRenderer.*;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.COLOR;
import static com.site21.bittermelon.init.neoforge.BitterEntities.*;
import static com.site21.bittermelon.init.neoforge.BitterEntities.TASER_PROJECTILE;
import static com.site21.bittermelon.init.neoforge.BitterItems.*;
import static com.site21.bittermelon.init.neoforge.BitterItems.HIGHLIGHTER;
import static net.minecraft.core.component.DataComponents.BASE_COLOR;
import static net.minecraft.world.item.DyeColor.WHITE;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ItemProperties.register(
                CIGARETTE.get(),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "lit"),
                (stack, level, entity, seed) -> Boolean.TRUE.equals(stack.get(LIT)) ? 1f : 0f
        );

        ItemProperties.register(
                CIGARETTE.get(),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "smoking"),
                (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1f : 0f
        );

        ItemProperties.register(
                POWDER.get(),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "level"),
                (stack, level, entity, seed) -> PowderedSubstanceItem.getTextureLevel(stack)
        );

        ItemProperties.register(
                PILL.get(),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "pill_shape"),
                (stack, level, entity, seed) -> stack.getOrDefault(PILL_SHAPE, PillShape.ROUND).ordinal()
        );

        ItemProperties.register(
                SCP_377.get(),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "empty"),
                (stack, level, entity, seed) -> {
                    if (stack.getOrDefault(EMPTY_TIME, -1L) != -1L) return 1;
                    return 0;
                }
        );

        ItemColors itemColors = Minecraft.getInstance().getItemColors();

        itemColors.register((stack, tintIndex) -> {
                    if (stack.getItem() instanceof SubstanceContainerItem item) {
                        return item.getColor(stack);
                    }
                    return 0xFFFFFF;
                }, POWDER.get()
        );

        itemColors.register((stack, tintIndex) -> {
                    if (tintIndex == 0) {
                        return stack.getOrDefault(COLOR, 0xFFFFFFFF);
                    }
                    return 0xFFFFFFFF;
                }, PILL.get()
        );

        itemColors.register((stack, tintIndex) -> stack.getOrDefault(BASE_COLOR, WHITE).getTextColor(), CHALK.get());

        itemColors.register((stack, tintIndex) -> {
                    if (tintIndex == 0) {
                        return stack.getOrDefault(BASE_COLOR, WHITE).getTextColor();
                    }
                    return 0x00FFFFFF;
                }, HIGHLIGHTER.get()
        );
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
        event.registerBlockEntityRenderer(BitterBlockEntities.STRUCTURAL_BLOCK_ENTITY.get(), StructuralBlockRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.THERMOMETER_BLOCK_ENTITY.get(), ThermometerRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.INTERCOM_BLOCK_ENTITY.get(), PhoneCordRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.LARGE_SLIDING_DOOR_BLOCK_ENTITY.get(), LargeSlidingDoorRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
        event.registerBlockEntityRenderer(BitterBlockEntities.WALL_WRITING_BLOCK_ENTITY.get(), WallWritingRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.@NotNull RegisterAdditional event) {
        event.register(LEFT_DOOR_MODEL);
        event.register(RIGHT_DOOR_MODEL);
        event.register(FRAME_MODEL);
    }

    @SubscribeEvent
    public static void registerColorHandlers(RegisterColorHandlersEvent.@NotNull Block event) {
        event.register(new FluidBlockColor(), FLUID.get());
    }
}
