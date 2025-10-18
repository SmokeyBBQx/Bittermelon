package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ClientSetupOld {

//    @SubscribeEvent
//    public static void onClientSetup(FMLClientSetupEvent event) {
//        ItemProperties.register(
//                CIGARETTE.get(),
//                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "lit"),
//                (stack, level, entity, seed) -> Boolean.TRUE.equals(stack.get(LIT)) ? 1f : 0f
//        );
//
//        ItemProperties.register(
//                CIGARETTE.get(),
//                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "smoking"),
//                (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1f : 0f
//        );
//
//        ItemProperties.register(
//                POWDER.get(),
//                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "level"),
//                (stack, level, entity, seed) -> PowderedSubstanceItem.getTextureLevel(stack)
//        );
//
//        ItemProperties.register(
//                PILL.get(),
//                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "pill_shape"),
//                (stack, level, entity, seed) -> stack.getOrDefault(PILL_SHAPE, StackPillShape.ROUND).ordinal()
//        );
//
//        ItemProperties.register(
//                SCP_377.get(),
//                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "empty"),
//                (stack, level, entity, seed) -> {
//                    if (stack.getOrDefault(EMPTY_TIME, -1L) != -1L) return 1;
//                    return 0;
//                }
//        );
//
//        ItemColors itemColors = Minecraft.getInstance().getItemColors();
//
//        itemColors.register((stack, tintIndex) -> {
//                    if (stack.getItem() instanceof SubstanceContainerItem item) {
//                        return item.getColor(stack);
//                    }
//                    return 0xFFFFFF;
//                }, POWDER.get()
//        );
//
//        itemColors.register((stack, tintIndex) -> {
//                    if (tintIndex == 0) {
//                        return stack.getOrDefault(COLOR, 0xFFFFFFFF);
//                    }
//                    return 0xFFFFFFFF;
//                }, PILL.get()
//        );
//
//        itemColors.register((stack, tintIndex) -> stack.getOrDefault(BASE_COLOR, WHITE).getTextColor(), CHALK.get());
//
//        itemColors.register((stack, tintIndex) -> {
//                    if (tintIndex == 0) {
//                        return stack.getOrDefault(BASE_COLOR, WHITE).getTextColor();
//                    }
//                    return 0x00FFFFFF;
//                }, HIGHLIGHTER.get()
//        );
//    }

}
