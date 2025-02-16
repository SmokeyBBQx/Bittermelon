package com.site21.bittermelon.content.entities.ai.behavior.attack;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.phys.Vec3;

public class LeapAtTarget<E extends Mob> extends net.tslat.smartbrainlib.api.core.behaviour.custom.attack.LeapAtTarget<E> {

    public LeapAtTarget(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected void start(E entity) {
        if (target == null) return;

        entity.swing(InteractionHand.MAIN_HAND);
        BehaviorUtils.lookAtEntity(entity, this.target);

        Vec3 velocity = new Vec3(this.target.getX() - entity.getX(), 0, this.target.getZ() - entity.getZ());

        if (velocity.lengthSqr() > 1.0E-7)
            velocity = velocity.normalize().scale(this.jumpStrength.apply(entity, this.target)).add(entity.getDeltaMovement().scale(this.moveSpeedContribution.apply(entity, this.target)));

        entity.setDeltaMovement(velocity.x, this.verticalJumpStrength.apply(entity, this.target), velocity.z);
    }
}
