package com.site21.bittermelon.common.content.blocks.flamingo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.entities.scp1507.client.SCP1507Model;
import com.site21.bittermelon.common.content.entities.scp1507.client.SCP1507RenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class FlamingoBlockRenderer implements BlockEntityRenderer<FlamingoBlockEntity, FlamingoRenderState> {
    private static final Identifier TEXTURE = Bittermelon.identifier("textures/entity/scp_1507.png");
    private final SCP1507Model model;

    public FlamingoBlockRenderer(BlockEntityRendererProvider.@NotNull Context context) {
        this.model = new SCP1507Model(context.bakeLayer(LayerDefinitions.SCP_1507_LAYER));
    }

    @Override
    public FlamingoRenderState createRenderState() {
        return new FlamingoRenderState();
    }

    @Override
    public void extractRenderState(FlamingoBlockEntity blockEntity, FlamingoRenderState state, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.rotation = blockEntity.getBlockState().getValue(BlockStateProperties.ROTATION_16);
        state.scp1507RenderState = new SCP1507RenderState();
    }

    @Override
    public void submit(FlamingoRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                       CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation * -22.5f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f));

        submitNodeCollector.submitModel(
                model,
                state.scp1507RenderState,
                poseStack,
                TEXTURE,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                state.breakProgress
        );

        poseStack.popPose();
    }
}
