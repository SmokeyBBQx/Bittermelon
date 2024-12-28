package com.site21.bittermelon.entities.base;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.entities.behavior.needs.Need;
import com.site21.bittermelon.entities.behavior.needs.NeedsUser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class BitterMob<T extends BitterMob<T>> extends PathfinderMob implements SmartBrainOwner<T>, NeedsUser<T> {

    protected BitterMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        Character character = initializeCharacter();
        CharacterManager.getInstance().addCharacter(character);
        CharacterManager.getInstance().setActiveCharacter(this.uuid, character);
    }

    protected abstract Character initializeCharacter();

    @Override
    public List<Activity> getActivityPriorities() {
        List<Need<T>> needs = new ArrayList<>(this.getNeeds());

        needs.removeIf(need -> !need.canBeFulfilled().test((T) this));

        if (!needs.isEmpty()) {
            needs.sort((n1, n2) -> {
                float priority1 = n1.priorityFunction().apply(this.getEntityData().get(n1.data()));
                float priority2 = n2.priorityFunction().apply(this.getEntityData().get(n2.data()));
                return Float.compare(priority2, priority1);
            });

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
