package com.site21.bittermelon.common.content.entities.mimicplayer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.common.content.entities.mimicplayer.Mimic;
import com.site21.bittermelon.common.systems.medical.bodypart.LimbLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public class MimicRenderer extends LivingEntityRenderer<Mimic, AvatarRenderState, PlayerModel> {
    public MimicRenderer(EntityRendererProvider.Context context, boolean slimSteve) {
        super(context, new PlayerModel(context.bakeLayer(slimSteve ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slimSteve), 0.5F);
        this.addLayer(
                new HumanoidArmorLayer<>(
                        this,
                        ArmorModelSet.bake(
                                slimSteve ? ModelLayers.PLAYER_SLIM_ARMOR : ModelLayers.PLAYER_ARMOR, context.getModelSet(), part -> new PlayerModel(part, slimSteve)
                        ),
                        context.getEquipmentRenderer()
                )
        );
        this.addLayer(new PlayerItemInHandLayer<>(this));
        this.addLayer(new ArrowLayer<>(this, context));
        this.addLayer(new Deadmau5EarsLayer(this, context.getModelSet()));
        this.addLayer(new CapeLayer(this, context.getModelSet(), context.getEquipmentAssets()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
        this.addLayer(new WingsLayer<>(this, context.getModelSet(), context.getEquipmentRenderer()));
        this.addLayer(new ParrotOnShoulderLayer(this, context.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this, context));
        this.addLayer(new LimbLayer<>(this));
    }

    protected boolean shouldRenderLayers(AvatarRenderState state) {
        return !state.isSpectator;
    }

    public Vec3 getRenderOffset(AvatarRenderState state) {
        Vec3 offset = super.getRenderOffset(state);
        return state.isCrouching ? offset.add(0.0, state.scale * -2.0F / 16.0, 0.0) : offset;
    }

    private static HumanoidModel.ArmPose getArmPose(Mimic avatar, HumanoidArm arm) {
        ItemStack mainHandItem = avatar.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandItem = avatar.getItemInHand(InteractionHand.OFF_HAND);
        HumanoidModel.ArmPose mainHandPose = getArmPose(avatar, mainHandItem, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose offHandPose = getArmPose(avatar, offHandItem, InteractionHand.OFF_HAND);
        if (mainHandPose.isTwoHanded()) {
            offHandPose = offHandItem.isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        }

        return avatar.getMainArm() == arm ? mainHandPose : offHandPose;
    }

    private static HumanoidModel.ArmPose getArmPose(Mimic avatar, ItemStack itemInHand, InteractionHand hand) {
        var extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(itemInHand);
        var armPose = extensions.getArmPose(avatar, hand, itemInHand);
        if (armPose != null) {
            return armPose;
        }
        if (itemInHand.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else if (!avatar.swinging && itemInHand.is(Items.CROSSBOW) && CrossbowItem.isCharged(itemInHand)) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        } else {
            if (avatar.getUsedItemHand() == hand && avatar.getUseItemRemainingTicks() > 0) {
                ItemUseAnimation anim = itemInHand.getUseAnimation();
                if (anim == ItemUseAnimation.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (anim == ItemUseAnimation.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (anim == ItemUseAnimation.TRIDENT) {
                    return HumanoidModel.ArmPose.THROW_TRIDENT;
                }

                if (anim == ItemUseAnimation.CROSSBOW) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (anim == ItemUseAnimation.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if (anim == ItemUseAnimation.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if (anim == ItemUseAnimation.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }

                if (anim == ItemUseAnimation.SPEAR) {
                    return HumanoidModel.ArmPose.SPEAR;
                }
            }

            SwingAnimation attack = itemInHand.get(DataComponents.SWING_ANIMATION);
            if (attack != null && attack.type() == SwingAnimationType.STAB && avatar.swinging) {
                return HumanoidModel.ArmPose.SPEAR;
            } else {
                return itemInHand.is(ItemTags.SPEARS) ? HumanoidModel.ArmPose.SPEAR : HumanoidModel.ArmPose.ITEM;
            }
        }
    }

    public Identifier getTextureLocation(AvatarRenderState state) {
        return state.skin.body().texturePath();
    }

    protected void scale(AvatarRenderState state, PoseStack poseStack) {
        float s = 0.9375F;
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    protected void submitNameDisplay(AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        int offset = state.showExtraEars ? -10 : 0;
        this.submitNameDisplay(state, poseStack, submitNodeCollector, camera, offset);
        poseStack.popPose();
    }

    public AvatarRenderState createRenderState() {
        return new AvatarRenderState();
    }

    public void extractRenderState(Mimic entity, AvatarRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, itemModelResolver);
        state.leftArmPose = getArmPose(entity, HumanoidArm.LEFT);
        state.rightArmPose = getArmPose(entity, HumanoidArm.RIGHT);
        state.skin = getSkin(entity);
        state.arrowCount = entity.getArrowCount();
        state.stingerCount = entity.getStingerCount();
        state.isSpectator = entity.isSpectator();
        state.showHat = entity.isModelPartShown(PlayerModelPart.HAT);
        state.showJacket = entity.isModelPartShown(PlayerModelPart.JACKET);
        state.showLeftPants = entity.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
        state.showRightPants = entity.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
        state.showLeftSleeve = entity.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
        state.showRightSleeve = entity.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
        state.showCape = entity.isModelPartShown(PlayerModelPart.CAPE);

        Player player = entity.getPlayer();
        state.parrotOnLeftShoulder = getParrotOnShoulder(player, true);
        state.parrotOnRightShoulder = getParrotOnShoulder(player, false);
        state.id = entity.getId();
        state.heldOnHead.clear();
        if (state.isUsingItem) {
            ItemStack itemstack = entity.getItemInHand(state.useItemHand);
            if (itemstack.canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SPYGLASS_SCOPE)) {
                this.itemModelResolver.updateForLiving(state.heldOnHead, itemstack, ItemDisplayContext.HEAD, entity);
            }
        }
    }

    public PlayerSkin getSkin(Mimic mimic) {
        // TODO: Cache this in the local mimic
        Player player = mimic.getPlayer();
        if (player == null) return DefaultPlayerSkin.getDefaultSkin();

        PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(player.getUUID());;
        return info == null ? DefaultPlayerSkin.get(player.getUUID()) : info.getSkin();
    }

    @Nullable
    private static Parrot.Variant getParrotOnShoulder(Player player, boolean leftShoulder) {
        if (player == null) return null;

        Optional<Parrot.Variant> variant = leftShoulder ? player.getShoulderParrotLeft() : player.getShoulderParrotRight();
        return variant.orElse(null);
    }

    public void renderRightHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier p_445487_, boolean hasSleeve, net.minecraft.client.player.AbstractClientPlayer player) {
        if(!net.neoforged.neoforge.client.ClientHooks.renderSpecificFirstPersonArm(poseStack, submitNodeCollector, lightCoords, player, HumanoidArm.RIGHT))
            this.renderHand(poseStack, submitNodeCollector, lightCoords, p_445487_, this.model.rightArm, hasSleeve);
    }

    public void renderLeftHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier p_446675_, boolean hasSleeve, net.minecraft.client.player.AbstractClientPlayer player) {
        if(!net.neoforged.neoforge.client.ClientHooks.renderSpecificFirstPersonArm(poseStack, submitNodeCollector, lightCoords, player, HumanoidArm.LEFT))
            this.renderHand(poseStack, submitNodeCollector, lightCoords, p_446675_, this.model.leftArm, hasSleeve);
    }

    private void renderHand(
            PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, ModelPart arm, boolean hasSleeve
    ) {
        PlayerModel model = this.getModel();
        arm.resetPose();
        arm.visible = true;
        model.leftSleeve.visible = hasSleeve;
        model.rightSleeve.visible = hasSleeve;
        model.leftArm.zRot = -0.1F;
        model.rightArm.zRot = 0.1F;
        submitNodeCollector.submitModelPart(arm, poseStack, RenderTypes.entityTranslucent(skinTexture), lightCoords, OverlayTexture.NO_OVERLAY, null);
    }

    protected void setupRotations(AvatarRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
        float swimAmount = state.swimAmount;
        float xRot = state.xRot;
        if (state.isFallFlying) {
            super.setupRotations(state, poseStack, bodyRot, entityScale);
            float scale = state.fallFlyingScale();
            if (!state.isAutoSpinAttack) {
                poseStack.mulPose(Axis.XP.rotationDegrees(scale * (-90.0F - xRot)));
            }

            if (state.shouldApplyFlyingYRot) {
                poseStack.mulPose(Axis.YP.rotation(state.flyingYRot));
            }
        } else if (swimAmount > 0.0F) {
            super.setupRotations(state, poseStack, bodyRot, entityScale);
            float targetXRot = state.isInWater ? -90.0F - xRot : -90.0F;
            float xAngle = Mth.lerp(swimAmount, 0.0F, targetXRot);
            poseStack.mulPose(Axis.XP.rotationDegrees(xAngle));
            if (state.isVisuallySwimming) {
                poseStack.translate(0.0F, -1.0F, 0.3F);
            }
        } else {
            super.setupRotations(state, poseStack, bodyRot, entityScale);
        }
    }
}
