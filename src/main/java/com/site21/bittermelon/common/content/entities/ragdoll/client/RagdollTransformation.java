package com.site21.bittermelon.common.content.entities.ragdoll.client;

import com.github.stephengold.joltjni.Body;
import com.github.stephengold.joltjni.Quat;
import com.github.stephengold.joltjni.RVec3;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Quaternionf;

public class RagdollTransformation {
    public static final StreamCodec<ByteBuf, RagdollTransformation> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public RagdollTransformation decode(ByteBuf input) {
            RagdollTransformation transformation = new RagdollTransformation();
            transformation.pos.set(input.readDouble(), input.readDouble(), input.readDouble());
            transformation.rot.set(input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat());
            transformation.prevPos.set(transformation.pos);
            transformation.prevRot.set(transformation.rot);
            return transformation;
        }

        @Override
        public void encode(ByteBuf output, RagdollTransformation value) {
            output.writeDouble(value.pos.xx());
            output.writeDouble(value.pos.yy());
            output.writeDouble(value.pos.zz());
            output.writeFloat(value.rot.getX());
            output.writeFloat(value.rot.getY());
            output.writeFloat(value.rot.getZ());
            output.writeFloat(value.rot.getW());
        }
    };

    public final RVec3 prevPos = new RVec3();
    public final RVec3 pos = new RVec3();
    public final Quat prevRot = new Quat();
    public final Quat rot = new Quat();

    public void update(Body body) {
        prevPos.set(pos);
        prevRot.set(rot);
        pos.set(body.getPosition());
        rot.set(body.getRotation());
    }

    public RVec3 interpolatedPos(float partialTick, RVec3 store) {
        store.set(
                prevPos.xx() + (pos.xx() - prevPos.xx()) * partialTick,
                prevPos.yy() + (pos.yy() - prevPos.yy()) * partialTick,
                prevPos.zz() + (pos.zz() - prevPos.zz()) * partialTick
        );
        return store;
    }

    public Quat interpolatedRot(float partialTick, Quat store) {
        Quaternionf prev = new Quaternionf(prevRot.getX(), prevRot.getY(), prevRot.getZ(), prevRot.getW());
        Quaternionf current = new Quaternionf(rot.getX(), rot.getY(), rot.getZ(), rot.getW());
        prev.slerp(current, partialTick);
        store.set(prev.x(), prev.y(), prev.z(), prev.w());
        return store;
    }
}
