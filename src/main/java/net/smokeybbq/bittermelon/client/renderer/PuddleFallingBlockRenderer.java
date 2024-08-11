package net.smokeybbq.bittermelon.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.entities.PuddleFallingBlockEntity;
import net.smokeybbq.bittermelon.util.ModLogger;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;

@OnlyIn(Dist.CLIENT)
public class PuddleFallingBlockRenderer extends EntityRenderer<PuddleFallingBlockEntity> {
    private final BlockRenderDispatcher dispatcher;
    public PuddleFallingBlockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(PuddleFallingBlockEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        BlockState blockstate = entity.getBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = entity.level();
            if (blockstate != level.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                matrixStack.pushPose();
                BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
                matrixStack.translate(-0.5D, 0.0D, -0.5D);
                var model = this.dispatcher.getBlockModel(blockstate);

                int color = 0xFFAAD5DB; // Default color
                CompoundTag nbt = entity.blockData;
                if (nbt != null) {
                    PuddleBlockEntity tempEntity = new PuddleBlockEntity(blockpos, blockstate);
                    tempEntity.load(nbt);
                    color = tempEntity.getColor();
                } else {
                    ModLogger.warn("PuddleFallingBlockEntity has null blockData");
                }


                float r = ((color >> 16) & 0xFF) / 255f;
                float g = ((color >> 8) & 0xFF) / 255f;
                float b = (color & 0xFF) / 255f;
                float a = ((color >> 24) & 0xFF) / 255f;

                RenderSystem.setShaderColor(r, g, b, a);

                for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(entity.getStartPos())), ModelData.EMPTY)) {
                    this.dispatcher.getModelRenderer().tesselateBlock(
                            level, model, blockstate, blockpos, matrixStack, buffer.getBuffer(renderType), false,
                            RandomSource.create(), blockstate.getSeed(entity.getStartPos()),
                            OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType
                    );
                }
                matrixStack.popPose();
                super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            }
        }
    }


    @Override
    public ResourceLocation getTextureLocation(PuddleFallingBlockEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}