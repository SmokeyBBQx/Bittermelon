package com.site21.bittermelon.common.content.entities.scp025fr;

import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.character.Character;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class SCP025FR extends BitterMob<SCP025FR> {
    private final SCP025FRPart[] parts;

    public SCP025FR(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level, 2);
        parts = new SCP025FRPart[9];
        for (int i = 0; i < parts.length; i++) {
            parts[i] = new SCP025FRPart(this);
        }
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    public BrainActivityGroup<? extends SCP025FR> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP025FR> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new SetRandomWalkTarget<>()
        );
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(getUUID(), "SCP-025");
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of();
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP025FR>> getSensors() {
        return List.of();
    }

    @Override
    public void tick() {
        super.tick();

        for (SCP025FRPart part : getParts()) {
            part.xOld = part.getX();
            part.yOld = part.getY();
            part.zOld = part.getZ();
            part.yRotO = part.getYRot();
            part.xRotO = part.getXRot();
        }

        for (int i = 0; i < parts.length; i++) {
            parts[i].movePart(parts, i);
        }
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public @Nullable SCP025FRPart[] getParts() {
        return parts;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.SPIDER_STEP, 0.05f, 0.8f);
    }

    public int getHeadRotSpeed() {
        return 1;
    }

    @Override
    protected float getMaxHeadRotationRelativeToBody() {
        return 0;
    }

    @Override
    public int getMaxHeadYRot() {
        return 40;
    }
}
