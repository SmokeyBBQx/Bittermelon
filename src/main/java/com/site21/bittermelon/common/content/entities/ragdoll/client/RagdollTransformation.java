package com.site21.bittermelon.common.content.entities.ragdoll.client;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class RagdollTransformation {
    public static final StreamCodec<ByteBuf, RagdollTransformation> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public RagdollTransformation decode(ByteBuf input) {
            RagdollTransformation transformation = new RagdollTransformation();
            transformation.pos.set(input.readFloat(), input.readFloat(), input.readFloat());
            transformation.rot.set(input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat());
            transformation.prevPos.set(transformation.pos);
            transformation.prevRot.set(transformation.rot);
            return transformation;
        }

        @Override
        public void encode(ByteBuf output, RagdollTransformation value) {
            output.writeFloat(value.pos.x);
            output.writeFloat(value.pos.y);
            output.writeFloat(value.pos.z);
            output.writeFloat(value.rot.getX());
            output.writeFloat(value.rot.getY());
            output.writeFloat(value.rot.getZ());
            output.writeFloat(value.rot.getW());
        }
    };

    public final Vector3f prevPos = new Vector3f();
    public final Vector3f pos = new Vector3f();
    public final Quaternion prevRot = new Quaternion();
    public final Quaternion rot = new Quaternion();

    public void update(PhysicsRigidBody body) {
        prevPos.set(pos);
        prevRot.set(rot);
        body.getPhysicsLocation(pos);
        body.getPhysicsRotation(rot);
    }

    public Vector3f interpolatedPos(float partialTick, Vector3f store) {
        store.set(
                prevPos.x + (pos.x - prevPos.x) * partialTick,
                prevPos.y + (pos.y - prevPos.y) * partialTick,
                prevPos.z + (pos.z - prevPos.z) * partialTick
        );
        return store;
    }

    public Quaternion interpolatedRot(float partialTick, Quaternion store) {
        org.joml.Quaternionf prev = new org.joml.Quaternionf(prevRot.getX(), prevRot.getY(), prevRot.getZ(), prevRot.getW());
        org.joml.Quaternionf current = new org.joml.Quaternionf(rot.getX(), rot.getY(), rot.getZ(), rot.getW());
        prev.slerp(current, partialTick);
        store.set(prev.x(), prev.y(), prev.z(), prev.w());
        return store;
    }
}
