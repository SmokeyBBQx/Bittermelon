package com.site21.bittermelon.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.blocks.blockentities.ThermometerBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ThermometerRenderer implements BlockEntityRenderer<ThermometerBlockEntity> {
    private final Font font;

    public ThermometerRenderer(BlockEntityRendererProvider.@NotNull Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(@NotNull ThermometerBlockEntity thermometer, float v, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        poseStack.pushPose();

        float temperature = thermometer.getTemperature();
        String message = String.format("%.1f°C", temperature);

        poseStack.translate(0.61, 0.525, 0.875);

        poseStack.mulPose(Axis.ZN.rotationDegrees(180f));

        int length = message.length();
        float baseScale = 0.0075f;
        float scale = length > 6 ? baseScale * (1.0f - ((length - 6) * 0.2f)) : baseScale;

        poseStack.scale(scale, scale, scale);

        font.drawInBatch(message, 0, 0, 0xFF000000, false, poseStack.last().pose(), multiBufferSource, Font.DisplayMode.POLYGON_OFFSET, 0, 15728880);

        poseStack.popPose();
    }
}
