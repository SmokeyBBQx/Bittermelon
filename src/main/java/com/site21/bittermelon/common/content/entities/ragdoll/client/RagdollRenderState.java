package com.site21.bittermelon.common.content.entities.ragdoll.client;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class RagdollRenderState extends EntityRenderState {
    public final Vector3f[] partPositions = new Vector3f[6];
    public final Quaternion[] partRotations = new Quaternion[6];

    public RagdollRenderState() {
        for (int i = 0; i < 6; i++) {
            partPositions[i] = new Vector3f();
            partRotations[i] = new Quaternion();
        }
    }
}
