package com.site21.bittermelon.common.content.entities.mimicplayer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.content.entities.mimicplayer.Mimic;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class MimicRenderer extends LivingEntityRenderer<Mimic, PlayerRenderState, PlayerModel> {
    public MimicRenderer(EntityRendererProvider.Context context, boolean useSlimModel) {
        super(context, new PlayerModel(context.bakeLayer(useSlimModel ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER),
                useSlimModel), 0.5F);
        this.addLayer(
                new HumanoidArmorLayer<>(
                        this,
                        new HumanoidArmorModel<>(context.bakeLayer(useSlimModel ?
                                ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)),
                        new HumanoidArmorModel<>(context.bakeLayer(useSlimModel ?
                                ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)),
                        context.getEquipmentRenderer()
                )
        );
        this.addLayer(new PlayerItemInHandLayer<>(this));
        this.addLayer(new ArrowLayer<>(this, context));
        this.addLayer(new Deadmau5EarsLayer(this, context.getModelSet()));
        this.addLayer(new CapeLayer(this, context.getModelSet(), context.getEquipmentAssets()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
        this.addLayer(new WingsLayer<>(this, context.getModelSet(), context.getEquipmentRenderer()));
        this.addLayer(new ParrotOnShoulderLayer(this, context.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this, context));
    }

    protected boolean shouldRenderLayers(PlayerRenderState renderState) {
        return !renderState.isSpectator;
    }

    public Vec3 getRenderOffset(PlayerRenderState renderState) {
        Vec3 vec3 = super.getRenderOffset(renderState);
        return renderState.isCrouching ? vec3.add(0.0, renderState.scale * -2.0F / 16.0, 0.0) : vec3;
    }

    private static HumanoidModel.ArmPose getArmPose(Mimic mimic, HumanoidArm arm) {
        Player player = mimic.getPlayer();
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack itemstack1 = player.getItemInHand(InteractionHand.OFF_HAND);
        HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(player, itemstack, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(player, itemstack1, InteractionHand.OFF_HAND);
        if (humanoidmodel$armpose.isTwoHanded()) {
            humanoidmodel$armpose1 = itemstack1.isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        }

        return player.getMainArm() == arm ? humanoidmodel$armpose : humanoidmodel$armpose1;
    }

    private static HumanoidModel.ArmPose getArmPose(Player player, ItemStack stack, InteractionHand hand) {
        var extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(stack);
        var armPose = extensions.getArmPose(player, hand, stack);
        if (armPose != null) {
            return armPose;
        }
        if (stack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else if (!player.swinging && stack.is(Items.CROSSBOW) && CrossbowItem.isCharged(stack)) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        } else {
            if (player.getUsedItemHand() == hand && player.getUseItemRemainingTicks() > 0) {
                ItemUseAnimation itemuseanimation = stack.getUseAnimation();
                if (itemuseanimation == ItemUseAnimation.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (itemuseanimation == ItemUseAnimation.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (itemuseanimation == ItemUseAnimation.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if (itemuseanimation == ItemUseAnimation.CROSSBOW) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (itemuseanimation == ItemUseAnimation.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if (itemuseanimation == ItemUseAnimation.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if (itemuseanimation == ItemUseAnimation.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }
            }

            return HumanoidModel.ArmPose.ITEM;
        }
    }

    public ResourceLocation getTextureLocation(PlayerRenderState renderState) {
        return renderState.skin.texture();
    }

    protected void scale(PlayerRenderState renderState, PoseStack poseStack) {
        float f = 0.9375F;
        poseStack.scale(f, f, f);
    }

    protected void renderNameTag(PlayerRenderState renderState, Component displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        if (renderState.scoreText != null) {
            super.renderNameTag(renderState, renderState.scoreText, poseStack, bufferSource, packedLight);
            poseStack.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
        }

        super.renderNameTag(renderState, displayName, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    public PlayerRenderState createRenderState() {
        return new PlayerRenderState();
    }

    public void extractRenderState(Mimic entity, PlayerRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, reusedState, partialTick, this.itemModelResolver);
        reusedState.leftArmPose = getArmPose(entity, HumanoidArm.LEFT);
        reusedState.rightArmPose = getArmPose(entity, HumanoidArm.RIGHT);

        reusedState.skin = entity.getSkin();
        reusedState.arrowCount = entity.getArrowCount();
        reusedState.stingerCount = entity.getStingerCount();
        reusedState.useItemRemainingTicks = entity.getUseItemRemainingTicks();
        reusedState.swinging = entity.swinging;
        reusedState.isSpectator = entity.isSpectator();

        reusedState.showHat = entity.isModelPartShown(PlayerModelPart.HAT);
        reusedState.showJacket = entity.isModelPartShown(PlayerModelPart.JACKET);
        reusedState.showLeftPants = entity.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
        reusedState.showRightPants = entity.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
        reusedState.showLeftSleeve = entity.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
        reusedState.showRightSleeve = entity.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
        reusedState.showCape = entity.isModelPartShown(PlayerModelPart.CAPE);

        Player player = entity.getPlayer();
        reusedState.parrotOnLeftShoulder = getParrotOnShoulder(player, true);
        reusedState.parrotOnRightShoulder = getParrotOnShoulder(player, false);
        reusedState.id = entity.getId();
        reusedState.name = player == null ? "None" : player.getGameProfile().getName();
        reusedState.heldOnHead.clear();
        if (reusedState.isUsingItem) {
            ItemStack itemstack = entity.getItemInHand(reusedState.useItemHand);
            if (itemstack.canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SPYGLASS_SCOPE)) {
                this.itemModelResolver.updateForLiving(reusedState.heldOnHead, itemstack, ItemDisplayContext.HEAD, entity);
            }
        }
    }

    @Nullable
    private static Parrot.Variant getParrotOnShoulder(Player player, boolean leftShoulder) {
        if (player == null) return null;

        CompoundTag compoundtag = leftShoulder ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
        if (compoundtag.isEmpty()) {
            return null;
        } else {
            EntityType<?> entitytype = compoundtag.read("id", EntityType.CODEC).orElse(null);
            return entitytype == EntityType.PARROT ? compoundtag.read("Variant",
                    Parrot.Variant.LEGACY_CODEC).orElse(Parrot.Variant.RED_BLUE) : null;
        }
    }

    public void renderRightHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation skinTexture, boolean isSleeveVisible, AbstractClientPlayer player) {
        if(!net.neoforged.neoforge.client.ClientHooks.renderSpecificFirstPersonArm(poseStack, bufferSource, packedLight, player, HumanoidArm.RIGHT))
            this.renderHand(poseStack, bufferSource, packedLight, skinTexture, this.model.rightArm, isSleeveVisible);
    }

    public void renderLeftHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation skinTexture, boolean isSleeveVisible, AbstractClientPlayer player) {
        if(!net.neoforged.neoforge.client.ClientHooks.renderSpecificFirstPersonArm(poseStack, bufferSource, packedLight, player, HumanoidArm.LEFT))
            this.renderHand(poseStack, bufferSource, packedLight, skinTexture, this.model.leftArm, isSleeveVisible);
    }

    private void renderHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation skinTexture, ModelPart arm, boolean isSleeveVisible) {
        PlayerModel playermodel = this.getModel();
        arm.resetPose();
        arm.visible = true;
        playermodel.leftSleeve.visible = isSleeveVisible;
        playermodel.rightSleeve.visible = isSleeveVisible;
        playermodel.leftArm.zRot = -0.1F;
        playermodel.rightArm.zRot = 0.1F;
        arm.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(skinTexture)), packedLight, OverlayTexture.NO_OVERLAY);
    }

    protected void setupRotations(PlayerRenderState renderState, PoseStack poseStack, float bodyRot, float scale) {
        float f = renderState.swimAmount;
        float f1 = renderState.xRot;
        if (renderState.isFallFlying) {
            super.setupRotations(renderState, poseStack, bodyRot, scale);
            float f2 = renderState.fallFlyingScale();
            if (!renderState.isAutoSpinAttack) {
                poseStack.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - f1)));
            }

            if (renderState.shouldApplyFlyingYRot) {
                poseStack.mulPose(Axis.YP.rotation(renderState.flyingYRot));
            }
        } else if (f > 0.0F) {
            super.setupRotations(renderState, poseStack, bodyRot, scale);
            float f4 = renderState.isInWater ? -90.0F - f1 : -90.0F;
            float f3 = Mth.lerp(f, 0.0F, f4);
            poseStack.mulPose(Axis.XP.rotationDegrees(f3));
            if (renderState.isVisuallySwimming) {
                poseStack.translate(0.0F, -1.0F, 0.3F);
            }
        } else {
            super.setupRotations(renderState, poseStack, bodyRot, scale);
        }
    }
}
