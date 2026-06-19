package com.site21.bittermelon.common.content.entities.scp025fr;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;

public class SCP025FRPart extends PartEntity<SCP025FR> {
    public SCP025FRPart(SCP025FR parent) {
        super(parent);
        setPos(parent.position());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }

    public void movePart(SCP025FRPart[] parts, int index) {
        SCP025FRPart self = parts[index];
        Vec3 facing = index == 0
                ? getParent().position()
                : parts[index - 1].position();
        Vec3 dir = self.position().subtract(facing).normalize();
        self.setPos(facing.add(dir.scale(0.5)));

        float yaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
        double horiz = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        float pitch = (float) Math.toDegrees(Math.atan2(dir.y, horiz));

        self.setYRot(yaw);
        self.setXRot(pitch);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return getParent().getDimensions(pose);
    }
}
