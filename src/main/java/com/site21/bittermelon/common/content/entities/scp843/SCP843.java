package com.site21.bittermelon.common.content.entities.scp843;

import com.site21.bittermelon.common.content.entities.scp843.behavior.Photosynthesize;
import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.ai.behavior.social.Relationship;
import com.site21.bittermelon.common.systems.ai.behavior.social.Socializable;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyItemsSensor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SCP843 extends BitterMob<SCP843> implements Socializable, SmartBrainOwner<SCP843> {
    protected SCP843(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Character initializeCharacter() {
        return null;
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        HashMap<Need, NeedInstance> statConfigs = new HashMap<>();
        statConfigs.put(Need.SUNLIGHT, new NeedInstance(-0.001f, value -> Math.pow(100 - value, 1.5), BitterActivity.PHOTOSYNTHESIZE.get()));
        statConfigs.put(Need.NUTRIENTS, new NeedInstance(0.002f, value -> Math.pow(100 - value, 1.5), BitterActivity.EAT.get()));
        statConfigs.put(Need.THIRST, new NeedInstance(0.003f, value -> Math.pow(100 - value, 1.5), BitterActivity.DRINK.get()));
        statConfigs.put(Need.PROCREATION, new NeedInstance(0.0001f, value -> (double) value, BitterActivity.PROCREATE.get()));
        statConfigs.put(Need.SOCIALIZATION, new NeedInstance(0.001f, value -> Math.pow(value, 1.5), BitterActivity.SOCIALIZE.get()));
        statConfigs.put(Need.REST, new NeedInstance(0f, value -> Math.pow(value, 0.2), Activity.REST));
        statConfigs.put(Need.BLADDER, new NeedInstance(0.0015f, value -> (double) value, BitterActivity.URINATE.get()));
        statConfigs.put(Need.DEFECATION, new NeedInstance(0.001f, value -> (double) value, BitterActivity.DEFECATE.get()));
        statConfigs.put(Need.MOVEMENT, new NeedInstance(0.001f, value -> Math.pow(value, 1.2), BitterActivity.EXPLORE.get()));
        statConfigs.put(Need.HYGIENE, new NeedInstance(0.001f, value -> Math.pow(value, 1.3), BitterActivity.GROOM.get()));
        statConfigs.put(Need.RECREATION, new NeedInstance(0.001f, value -> Math.pow(value, 1.3), BitterActivity.PLAY.get()));
        return statConfigs;
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP843>> getSensors() {
        return ObjectArrayList.of(
                new NearbyBlocksSensor<>(),
                new NearbyItemsSensor<>()
        );
    }

    public BrainActivityGroup<? extends SCP843> getPhotosynthesizeTasks() {
        return new BrainActivityGroup<SCP843>(BitterActivity.PHOTOSYNTHESIZE.get()).behaviours(
                new Photosynthesize<>()
        );
    }

    public BrainActivityGroup<? extends SCP843> getEatTasks() {
        return new BrainActivityGroup<SCP843>(BitterActivity.EAT.get()).behaviours(

        );
    }

    public BrainActivityGroup<? extends SCP843> getDrinkTasks() {
        return new BrainActivityGroup<SCP843>(BitterActivity.DRINK.get()).behaviours(

        );
    }

    @Override
    public Map<Character, Relationship> getRelationships() {
        return null;
    }

}
