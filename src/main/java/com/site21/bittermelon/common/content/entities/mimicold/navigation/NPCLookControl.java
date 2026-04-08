package com.site21.bittermelon.common.content.entities.mimicold.navigation;

import com.site21.bittermelon.common.content.entities.mimicold.MimicPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.control.Control;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class NPCLookControl implements Control {
    protected final MimicPlayer mob;
    protected float yMaxRotSpeed;
    protected float xMaxRotAngle;
    protected int lookAtCooldown;
    protected double wantedX;
    protected double wantedY;
    protected double wantedZ;

    public NPCLookControl(MimicPlayer mob) {
        this.mob = mob;
    }

    /**
     * Sets the mob's look vector
     */
    public void setLookAt(Vec3 lookVector) {
        this.setLookAt(lookVector.x, lookVector.y, lookVector.z);
    }

    /**
     * Sets the controlling mob's look vector to the provided entity's location
     */
    public void setLookAt(Entity entity) {
        this.setLookAt(entity.getX(), entity.getEyeY(), entity.getZ());
    }

    /**
     * Sets position to look at using entity
     */
    public void setLookAt(Entity entity, float deltaYaw, float deltaPitch) {
        this.setLookAt(entity.getX(), entity.getEyeY(), entity.getZ(), deltaYaw, deltaPitch);
    }

    public void setLookAt(double x, double y, double z) {
        this.setLookAt(x, y, z, 10.0f, 40.0f);
    }

    /**
     * Sets position to look at
     */
    public void setLookAt(double x, double y, double z, float deltaYaw, float deltaPitch) {
        this.wantedX = x;
        this.wantedY = y;
        this.wantedZ = z;
        this.yMaxRotSpeed = deltaYaw;
        this.xMaxRotAngle = deltaPitch;
        this.lookAtCooldown = 2;
    }

    public void tick() {
        if (this.resetXRotOnTick()) {
            this.mob.setXRot(0.0F);
        }

        if (this.lookAtCooldown > 0) {
            this.lookAtCooldown--;
            this.getYRotD().ifPresent(p_370606_ -> this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, p_370606_, this.yMaxRotSpeed));
            this.getXRotD().ifPresent(p_427045_ -> this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), p_427045_, this.xMaxRotAngle)));
        } else {
            this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0F);
        }

        this.clampHeadRotationToBody();
    }

    protected void clampHeadRotationToBody() {
        if (!this.mob.getNavigation().isDone()) {
            this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, 75.0f);
        }
    }

    protected boolean resetXRotOnTick() {
        return true;
    }

    public boolean isLookingAtTarget() {
        return this.lookAtCooldown > 0;
    }

    public double getWantedX() {
        return this.wantedX;
    }

    public double getWantedY() {
        return this.wantedY;
    }

    public double getWantedZ() {
        return this.wantedZ;
    }

    protected Optional<Float> getXRotD() {
        double d0 = this.wantedX - this.mob.getX();
        double d1 = this.wantedY - this.mob.getEyeY();
        double d2 = this.wantedZ - this.mob.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return !(Math.abs(d1) > 1.0E-5F) && !(Math.abs(d3) > 1.0E-5F) ? Optional.empty() : Optional.of((float)(-(Mth.atan2(d1, d3) * 180.0F / (float)Math.PI)));
    }

    protected Optional<Float> getYRotD() {
        double d0 = this.wantedX - this.mob.getX();
        double d1 = this.wantedZ - this.mob.getZ();
        return !(Math.abs(d1) > 1.0E-5F) && !(Math.abs(d0) > 1.0E-5F)
                ? Optional.empty()
                : Optional.of((float)(Mth.atan2(d1, d0) * 180.0F / (float)Math.PI) - 90.0F);
    }
}
