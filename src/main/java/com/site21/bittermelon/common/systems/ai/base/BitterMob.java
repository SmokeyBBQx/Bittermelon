package com.site21.bittermelon.common.systems.ai.base;

import com.site21.bittermelon.common.systems.character.Character;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BitterMob<T extends BitterMob<T>> extends PathfinderMob implements SmartBrainOwner<T>, NeedsUser {
    private Map<Need, NeedInstance> needs;
    private final int behaviorRandomness;
    private Activity[] cachedActivityPriorities = null;

    protected BitterMob(EntityType<? extends PathfinderMob> entityType, Level level, int behaviorRandomness) {
        super(entityType, level);

        this.behaviorRandomness = behaviorRandomness;
//
//        if (!level.isClientSide) {
//            Character character = initializeCharacter();
//            CharacterManager.get(level).addCharacter(character);
//            CharacterManager.get(level).setActiveCharacter(this, character.getUUID());
//        }
    }

    protected BitterMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        this(entityType, level, 1);
    }

    protected abstract Character initializeCharacter();

    protected abstract Map<Need, NeedInstance> initializeNeeds();

    @Override
    public Activity[] getActivityActivationPriority() {
        if (cachedActivityPriorities != null) return cachedActivityPriorities;

        List<NeedInstance> needs = new ArrayList<>(getNeeds().values());

        if (needs.isEmpty()) {
            cachedActivityPriorities = new Activity[] {Activity.FIGHT, Activity.IDLE};
            return cachedActivityPriorities;
        }

        needs.sort((n1, n2) -> Double.compare(n2.evaluatePriority(), n1.evaluatePriority()));

        if (needs.size() > behaviorRandomness) {
            List<NeedInstance> randomizedBehaviour = needs.subList(0, behaviorRandomness);
            Collections.shuffle(randomizedBehaviour);
        }

        Activity[] result = new Activity[needs.size() + 2];
        result[0] = Activity.FIGHT;
        for (int i = 0; i < needs.size(); i++) {
            result[i + 1] = needs.get(i).getActivity();
        }
        result[result.length - 1] = Activity.IDLE;

        cachedActivityPriorities = result;
        return cachedActivityPriorities;
    }

    @Override
    public float getMood() {
        float mood = 0;

        for (NeedInstance needInstance : getNeeds().values()) {
            mood += needInstance.getValue();
        }

        return mood / getNeeds().size();
    }

    public Map<Need, NeedInstance> getNeeds() {
        if (needs == null) {
            needs = initializeNeeds();
        }
        return needs;
    }

    @Override
    public float getNeed(@NotNull Need stat) {
        return getNeeds().get(stat).getValue();
    }

    @Override
    public void setNeed(@NotNull Need stat, float value) {
        getNeeds().get(stat).setValue(Mth.clamp(value, 0, 100));
        invalidateActivityCache();
    }

    private void invalidateActivityCache() {
        cachedActivityPriorities = null;
    }

    @Override
    public void tick() {
        super.tick();

//        if (level().getGameTime() % 0 != 0) return;
//        getNeeds().values().forEach(NeedInstance::decay);
//        updateStress();
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);

        for (Need need : getNeeds().keySet()) {
            output.putFloat(need.name(), getNeed(need));
        }
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);

        for (Need need : getNeeds().keySet()) {
            setNeed(need, input.getFloatOr(need.name(), 0.0f));
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
