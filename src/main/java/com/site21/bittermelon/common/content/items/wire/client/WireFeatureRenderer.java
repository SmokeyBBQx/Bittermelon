package com.site21.bittermelon.common.content.items.wire.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.common.content.items.wire.WireItem;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import static com.site21.bittermelon.client.event.ClientSetup.WIRE_STATE;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

public class WireFeatureRenderer {

    public static WireState extractWireState(Avatar player, float partialTicks) {
        WireState state = new WireState();
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof WireItem)) return state;

        BlockPos devicePos = heldItem.get(CORD_CONNECTION.get());
        String port = heldItem.get(PORT_ID.get());

        if (devicePos == null || port == null) return state;

        state.playerPos = player.getRopeHoldPosition(partialTicks);
        state.blockPos = Vec3.atCenterOf(devicePos);
        state.offset = state.playerPos.subtract(player.getPosition(partialTicks));
        BlockPos eyePos = BlockPos.containing(player.getEyePosition(partialTicks));
        state.startBlockLight = player.isOnFire()
                ? 15
                : player.level().getBrightness(LightLayer.BLOCK, eyePos);
        state.endBlockLight = player.level().getBrightness(LightLayer.BLOCK, devicePos);
        state.startSkyLight = player.level().getBrightness(LightLayer.SKY, eyePos);
        state.endSkyLight = player.level().getBrightness(LightLayer.SKY, devicePos);

        return state;
    }

    public static void submitWire(AvatarRenderState state, SubmitNodeCollector collector, PoseStack poseStack) {
        WireState wireState = state.getRenderData(WIRE_STATE);
        if (wireState == null) return;

        collector.submitCustomGeometry(poseStack, RenderTypes.leash(), (pose, buffer) ->
                renderWire(pose, buffer, wireState));
    }

    private static void renderWire(PoseStack.Pose pose, VertexConsumer buffer, WireState state) {
        float dx = (float) (state.blockPos.x - state.playerPos.x);
        float dy = (float) (state.blockPos.y - state.playerPos.y);
        float dz = (float) (state.blockPos.z - state.playerPos.z);
        float offsetFactor = Mth.invSqrt(dx * dx + dz * dz) * state.thickness / 2.0F;
        float dxOff = dz * offsetFactor;
        float dzOff = dx * offsetFactor;
        pose.translate((float) state.offset.x, (float) state.offset.y, (float) state.offset.z);

        for (int k = 0; k <= 24; k++) {
            addVertexPair(buffer, pose, dx, dy, dz, state.thickness, dxOff, dzOff, k, false, state);
        }

        for (int k = 24; k >= 0; k--) {
            addVertexPair(buffer, pose, dx, dy, dz, 0.0F, dxOff, dzOff, k, true, state);
        }
    }

    private static void addVertexPair(
            VertexConsumer buffer,
            PoseStack.Pose pose,
            float dx,
            float dy,
            float dz,
            float fudge,
            float dxOff,
            float dzOff,
            int k,
            boolean backwards,
            WireState state
    ) {
        float progress = k / 24.0F;
        int block = (int) Mth.lerp(progress, (float) state.startBlockLight, (float) state.endBlockLight);
        int sky = (int) Mth.lerp(progress, (float) state.startSkyLight, (float) state.endSkyLight);
        int lightCoords = LightCoordsUtil.pack(block, sky);
        float colorModifier = k % 2 == (backwards ? 1 : 0) ? 0.7F : 1.0F;
        float r = state.r * colorModifier;
        float g = state.g * colorModifier;
        float b = state.b * colorModifier;
        float x = dx * progress;
        float y;
        if (state.slack) {
            y = dy > 0.0F ? dy * progress * progress : dy - dy * (1.0F - progress) * (1.0F - progress);
        } else {
            y = dy * progress;
        }

        float z = dz * progress;

        buffer.addVertex(pose, x - dxOff, y + fudge, z + dzOff)
                .setColor(r, g, b, 1.0F)
                .setLight(lightCoords);
        buffer.addVertex(pose, x + dxOff, y + state.thickness - fudge, z - dzOff)
                .setColor(r, g, b, 1.0F)
                .setLight(lightCoords);
    }
}
