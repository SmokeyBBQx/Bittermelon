package com.site21.bittermelon.common.content.entities.scp025fr.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SCP025FRRenderState extends LivingEntityRenderState {
    public final List<PartPose> partPoses = new ArrayList<>();
    public record PartPose(Vec3 offset, float yRot, float xRot) {}
}
