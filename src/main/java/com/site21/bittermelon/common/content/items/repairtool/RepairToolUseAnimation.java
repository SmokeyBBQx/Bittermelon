package com.site21.bittermelon.common.content.items.repairtool;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class RepairToolUseAnimation implements IClientItemExtensions {
    private static final float HAND_OFFSET_X = 0.56f;
    private static final float HAND_OFFSET_Y = -0.52f;
    private static final float HAND_OFFSET_Z = -0.72f;
    private static final float EQUIP_Y_OFFSET = -0.6f;

    private static final float ANIMATION_SPEED = 0.3f;
    private static final float PUSH_AMPLITUDE = 0.3f;
    private static final float PUSH_OFFSET = 0.2f;

    private static final float BASE_ROTATION_X = -80.0f;

    @Override
    public boolean applyForgeHandTransform(@NotNull PoseStack poseStack, @NotNull LocalPlayer player, @NotNull HumanoidArm arm,
                                           @NotNull ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        if (itemInHand.getUseAnimation() != RepairToolItem.REPAIR_TOOL_ANIMATION || !player.isUsingItem()) return false;
        int armDirection = arm == HumanoidArm.RIGHT ? 1 : -1;

        float startZ = -0.75f;
        float endZ = -1.0f;
        float lerpOffset = Mth.lerp(swingProcess, startZ, endZ);

        float shakeX = equipProcess == 0 ? (player.getRandom().nextFloat() - 0.5f) * 0.025f + 1.0f : 1;
        poseStack.translate(shakeX * armDirection * HAND_OFFSET_X, HAND_OFFSET_Y, swingProcess == 0 ? endZ : lerpOffset);


        return true;
    }
}
