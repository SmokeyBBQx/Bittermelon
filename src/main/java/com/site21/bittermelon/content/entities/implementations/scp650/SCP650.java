package com.site21.bittermelon.content.entities.implementations.scp650;

import com.site21.bittermelon.content.entities.ai.sensors.ObserversSensor;
import com.site21.bittermelon.content.entities.implementations.scp650.behavior.InvalidateFoundTarget;
import com.site21.bittermelon.content.entities.implementations.scp650.behavior.TeleportBehindRandomTarget;
import com.site21.bittermelon.content.entities.implementations.scp650.client.SCP650Animation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SCP650 extends Mob implements SmartBrainOwner<SCP650> {
    private static final EntityDataAccessor<String> POSE = SynchedEntityData.defineId(SCP650.class, EntityDataSerializers.STRING);
    public static final String[] SCARE_POSES = {
            "JUMPSCARE", "JUMPSCARE_2", "JUMPSCARE_3", "JUMPSCARE_4", "JUMPSCARE_5", "MENACING", "BOWLING",
            "SPOOKY", "RAPTURE", "KARATE"
    };

    public static final String[] GUILT_POSES = {
            "FRUSTRATED", "FRUSTRATED_2", "CHOKING", "SCARED", "SCARED_2"
    };

    public static final String[] EASTER_EGG_POSES = {
            "DROP_DEAD_GORGEOUS", "FAMILY_GUY_DEAD_POSE", "POSE_28", "DEATH_NOTE"
    };

    public final AnimationState pose = new AnimationState();

    public SCP650(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100).add(Attributes.MOVEMENT_SPEED, 0);
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP650>> getSensors() {
        return ObjectArrayList.of(new ObserversSensor<SCP650>().setScanRate(entity -> 10));
    }

    @Override
    public BrainActivityGroup<? extends SCP650> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new TeleportBehindRandomTarget<>(),
                new InvalidateFoundTarget<>()
        );
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(POSE, "pose");
    }

    public void setPose(String poseName) {
        this.entityData.set(POSE, poseName);
    }

    public String getRandomPose(boolean guilt) {
        if (random.nextFloat() > 0.99f) {
            return EASTER_EGG_POSES[this.random.nextInt(EASTER_EGG_POSES.length)];
        }

        if (guilt) {
            return GUILT_POSES[this.random.nextInt(GUILT_POSES.length)];
        } else {
            return SCARE_POSES[this.random.nextInt(SCARE_POSES.length)];
        }
    }

    public String getCurrentPose() {
        return this.entityData.get(POSE);
    }

    public AnimationDefinition getCurrentPoseAnimation() {
        return switch (getCurrentPose()) {
            case "JUMPSCARE" -> SCP650Animation.JUMPSCARE;
            case "JUMPSCARE_2" -> SCP650Animation.JUMPSCARE_2;
            case "JUMPSCARE_3" -> SCP650Animation.JUMPSCARE_3;
            case "JUMPSCARE_4" -> SCP650Animation.JUMPSCARE_4;
            case "JUMPSCARE_5" -> SCP650Animation.JUMPSCARE_5;
            case "FRUSTRATED" -> SCP650Animation.FRUSTRATED;
            case "CHOKING" -> SCP650Animation.CHOKING;
            case "MENACING" -> SCP650Animation.MENACING;
            case "DROP_DEAD_GORGEOUS" -> SCP650Animation.DROP_DEAD_GORGEOUS;
            case "SCARED" -> SCP650Animation.SCARED;
            case "FRUSTRATED_2" -> SCP650Animation.FRUSTRATED_2;
            case "FAMILY_GUY_DEAD_POSE" -> SCP650Animation.FAMILY_GUY_DEAD_POSE;
            case "SCARED_2" -> SCP650Animation.SCARED_2;
            case "BOWLING" -> SCP650Animation.BOWLING;
            case "SPOOKY" -> SCP650Animation.SPOOKY;
            case "RAPTURE" -> SCP650Animation.RAPTURE;
            case "POSE_28" -> SCP650Animation.POSE_28;
            case "KARATE" -> SCP650Animation.KARATE;
            case "DEATH_NOTE" -> SCP650Animation.DEATH_NOTE;
            default -> SCP650Animation.DEFAULT;
        };
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    protected @NotNull SmartBrainProvider<SCP650> brainProvider() {
        return new SmartBrainProvider<>(this);
    }
}
