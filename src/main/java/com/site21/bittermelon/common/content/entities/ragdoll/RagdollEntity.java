package com.site21.bittermelon.common.content.entities.ragdoll;

import com.github.stephengold.joltjni.RVec3;
import com.github.stephengold.joltjni.Vec3;
import com.site21.bittermelon.common.content.entities.ragdoll.client.RagdollTransformation;
import com.site21.bittermelon.common.physics.PhysicsManager;
import com.site21.bittermelon.init.neoforge.BitterDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

public class RagdollEntity extends Entity {
    public Ragdoll ragdoll;
    public static final EntityDataAccessor<List<RagdollTransformation>> PART_TRANSFORMATIONS =
            SynchedEntityData.defineId(RagdollEntity.class, BitterDataSerializers.RAGDOLL_TRANSFORMATIONS.get());
    private Vec3 pushDirection = new Vec3();

    public RagdollEntity(EntityType<?> type, Level level) {
        super(type, level);
        if (!level.isClientSide()) {
            PhysicsManager.getPhysicsLevel(level.dimension()).addRagdoll(this);
        }
    }

    public void addMotion(net.minecraft.world.phys.Vec3 motion) {
        this.pushDirection = new Vec3(motion.x + pushDirection.getX(), motion.y + pushDirection.getY(), motion.z + pushDirection.getZ());
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            List<RagdollTransformation> currentData = getPartTransformations();
            if (currentData != null) {
                for (RagdollTransformation transform : currentData) {
                    transform.prevPos.set(transform.pos);
                    transform.prevRot.set(transform.rot);
                }
            }
            return;
        }

        if (ragdoll == null) {
            Vec3 startPos = new Vec3((float) getX(), (float) getY(), (float) getZ());
            ragdoll = new Ragdoll(PhysicsManager.getPhysicsLevel(level().dimension()).system(), startPos);
        }

        ragdoll.addUniformVelocity(pushDirection);
        pushDirection = new Vec3();

        RVec3 torsoPos =  ragdoll.getPart(1).getPosition();
        setPosRaw(torsoPos.xx(), torsoPos.yy(), torsoPos.zz());

        List<RagdollTransformation> updated = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            RagdollTransformation t = new RagdollTransformation();
            t.update(ragdoll.getPart(i));
            t.prevPos.set(t.pos);
            t.prevRot.set(t.rot);
            updated.add(t);
        }

        entityData.set(PART_TRANSFORMATIONS, updated);
    }

    public List<RagdollTransformation> getPartTransformations() {
        return entityData.get(PART_TRANSFORMATIONS);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        List<RagdollTransformation> initial = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            initial.add(new RagdollTransformation());
        }
        entityData.define(PART_TRANSFORMATIONS, initial);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (ragdoll != null && !level().isClientSide()) {
            ragdoll.destroy();
            PhysicsManager.getPhysicsLevel(level()).removeRagdoll(this);
            ragdoll = null;
        }
    }
}
