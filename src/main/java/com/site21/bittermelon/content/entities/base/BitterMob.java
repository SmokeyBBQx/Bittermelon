package com.site21.bittermelon.content.entities.base;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.entities.ai.behavior.needs.Need;
import com.site21.bittermelon.content.entities.ai.behavior.needs.NeedsUser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class BitterMob<T extends BitterMob<T>> extends PathfinderMob implements SmartBrainOwner<T>, NeedsUser<T> {
    private final Map<NeedsStat, StatConfig> stats;
    private final int behaviorRandomness;

    protected BitterMob(EntityType<? extends PathfinderMob> entityType, Level level, int behaviorRandomness) {
        super(entityType, level);
        this.stats = initializeStats();

        this.behaviorRandomness = behaviorRandomness;

        if (!level.isClientSide) {
            Character character = initializeCharacter();
            CharacterManager.get(level).addCharacter(character);
            CharacterManager.get(level).setActiveCharacter(this, character.getUUID());
        }
    }

    protected BitterMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        this(entityType, level, 1);
    }

    protected abstract Character initializeCharacter();

    protected abstract Map<NeedsStat, StatConfig> initializeStats();

    @Contract("_ -> new")
    protected @NotNull StatConfig createStatConfig(float decayRate) {
        return StatConfig.of(decayRate, this);
    }

    @Override
    public List<Activity> getActivityPriorities() {
        List<Need<T>> needs = new ArrayList<>(this.getNeeds());

        needs.removeIf(need ->
                !need.canBeFulfilled().test((T) this) ||
                        need.priorityFunction().apply(this.entityData.get(need.data())) < 5);

        if (!needs.isEmpty()) {
            needs.sort((n1, n2) -> {
                float priority1 = n1.priorityFunction().apply(this.getEntityData().get(n1.data()));
                float priority2 = n2.priorityFunction().apply(this.getEntityData().get(n2.data()));
                return Float.compare(priority2, priority1);
            });

            if (needs.size() > behaviorRandomness) {
                List<Need<T>> randomizedBehaviour = needs.subList(0, behaviorRandomness);
                Collections.shuffle(randomizedBehaviour);
            }

            return needs.stream()
                    .map(Need::activity)
                    .collect(Collectors.toList());
        }

        return ObjectArrayList.of(Activity.FIGHT, Activity.IDLE);
    }

    @Override
    public float getMood() {
        float mood = 0;

        for (Need<T> need : getNeeds()) {
            mood += this.entityData.get(need.data());
        }

        return mood / getNeeds().size();
    }

    @Override
    public float getStat(@NotNull NeedsStat stat) {
        StatConfig config = stats.get(stat);
        return this.entityData.get(config.accessor());
    }

    @Override
    public void setStat(@NotNull NeedsStat stat, float value) {
        StatConfig config = stats.get(stat);
        this.entityData.set(config.accessor(), Math.min(100, Math.max(0, value)));
    }

    public EntityDataAccessor<Float> getDataAccessor(NeedsStat stat) {
        return stats.get(stat).accessor();
    }

    @Override
    public void tick() {
        super.tick();

        for (Map.Entry<NeedsStat, StatConfig> entry : stats.entrySet()) {
            StatConfig config = entry.getValue();
            if (config.decayRate() != 0) {
                modifyStat(entry.getKey(), config.decayRate());
            }
        }
        updateStress();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        if (stats == null) {
            initializeStats();
        } else {
            for (StatConfig stat : stats.values()) {
                builder.define(stat.accessor(), 0f);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        for (NeedsStat stat : stats.keySet()) {
            compound.putFloat(stat.saveKey(), getStat(stat));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        for (NeedsStat stat : stats.keySet()) {
            setStat(stat, compound.getFloat(stat.saveKey()));
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        tickBrain((T) this);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    protected @NotNull SmartBrainProvider<T> brainProvider() {
        return new SmartBrainProvider<>((T) this);
    }
}
