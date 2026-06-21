package com.site21.bittermelon.common.content.blocks.flamingo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.entities.scp1507.client.SCP1507Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FlamingoBlockRenderer implements BlockEntityRenderer<FlamingoBlockEntity> {
    private final SCP1507Model model;

    public FlamingoBlockRenderer(BlockEntityRendererProvider.@NotNull Context context) {
        this.model = new SCP1507Model(context.bakeLayer(LayerDefinitions.SCP_1507_LAYER));
    }

    @Override
    public void render(@NotNull FlamingoBlockEntity flamingoBlockEntity, float partialTick,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay, @NotNull Vec3 cameraPos) {
        int rotation = flamingoBlockEntity.getBlockState().getValue(BlockStateProperties.ROTATION_16);

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation * -22.5f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f));

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(
                Bittermelon.identifier("textures/entity/scp_1507.png")
        ));

        model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
