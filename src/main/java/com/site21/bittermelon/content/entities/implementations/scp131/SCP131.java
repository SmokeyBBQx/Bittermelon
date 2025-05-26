package com.site21.bittermelon.content.entities.implementations.scp131;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.entities.ai.behavior.needs.Need;
import com.site21.bittermelon.content.entities.base.BitterMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SCP131 extends BitterMob<SCP131> {
    protected SCP131(EntityType<? extends PathfinderMob> entityType, Level level, int behaviorRandomness) {
        super(entityType, level, behaviorRandomness);
    }

    @Override
    protected Character initializeCharacter() {
        return null;
    }

    @Override
    public @NotNull Vec3 handleRelativeFrictionAndCalculateMovement(@NotNull Vec3 deltaMovement, float friction) {
        return super.handleRelativeFrictionAndCalculateMovement(deltaMovement, friction * 0.05f);
    }

    @Override
    public void tick() {
        super.tick();

        if (horizontalCollision) {
            if (getDeltaMovement().x > 1 || getDeltaMovement().z > 1) {

            }
        }
    }

    @Override
    public List<Need<SCP131>> getNeeds() {
        return List.of();
    }

    @Override
    public float getStress() {
        return 0;
    }

    @Override
    public void modifyStress(float amount) {

    }

    @Override
    public void setStress(float amount) {

    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP131>> getSensors() {
        return List.of();
    }
}
