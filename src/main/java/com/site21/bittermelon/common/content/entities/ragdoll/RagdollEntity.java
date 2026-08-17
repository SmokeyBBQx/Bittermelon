package com.site21.bittermelon.common.content.entities.ragdoll;

import com.jme3.math.Vector3f;
import com.site21.bittermelon.common.content.entities.ragdoll.client.RagdollTransformation;
import com.site21.bittermelon.common.events.PhysicsManager;
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
    private Vector3f pushDirection = new Vector3f();

    public RagdollEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void addMotion(Vector3f motion) {
        this.pushDirection = pushDirection.add(motion);
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
            Vector3f startPos = new Vector3f((float) getX(), (float) getY(), (float) getZ());
            ragdoll = new Ragdoll(PhysicsManager.getPhysicsSpace(level().dimension()), startPos);
            ragdoll.setUserObject(this);
            ragdoll.addUniformVelocity(new Vector3f(0.5f, 0, 0.5f));
        }

        ragdoll.updateLocalWorldCollision(level(), blockPosition());

        ragdoll.addUniformVelocity(pushDirection);
        pushDirection = new Vector3f();

        Vector3f torsoPos = new Vector3f();
        ragdoll.getPart(1).getPhysicsLocation(torsoPos);
        setPosRaw(torsoPos.x, torsoPos.y, torsoPos.z);

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
            ragdoll = null;
        }
    }
}
