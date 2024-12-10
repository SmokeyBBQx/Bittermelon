package com.site21.bittermelon.entities.scps.SCP843;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.entities.behavior.needs.Need;
import com.site21.bittermelon.entities.behavior.needs.NeedsUser;
import com.site21.bittermelon.entities.behavior.social.Relationship;
import com.site21.bittermelon.entities.behavior.social.Socializable;
import com.site21.bittermelon.entities.scps.SCP843.behavior.Photosynthesize;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import com.site21.bittermelon.entities.scps.SCP939.behavior.Procreate;
import com.site21.bittermelon.init.BitterActivity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyItemsSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SCP843 extends PathfinderMob implements NeedsUser<SCP843>, Socializable, SmartBrainOwner<SCP843> {
    private static final EntityDataAccessor<Float> SUNLIGHT = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> NUTRIENTS = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> THIRST = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PROCREATION = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SOCIALIZATION = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> REST = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> BLADDER = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DEFECATION = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MOVEMENT = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HYGIENE = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RECREATION = SynchedEntityData.defineId(SCP843.class, EntityDataSerializers.FLOAT);


    protected SCP843(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP843>> getSensors() {
        return ObjectArrayList.of(
                new NearbyBlocksSensor<>(),
                new NearbyItemsSensor<>()
        );
    }

    @Override
    public List<Need<SCP843>> getNeeds() {
        return List.of(
                new Need<>(
                        SUNLIGHT,
                        BitterActivity.PHOTOSYNTHESIZE.get(),
                        value -> (float) Math.pow(100 - value, 1.5),
                        value -> true
                ),
                new Need<>(
                        NUTRIENTS,
                        BitterActivity.EAT.get(),
                        value -> (float) Math.pow(100 - value, 1.5),
                        value -> true
                ),
                new Need<>(
                        THIRST,
                        BitterActivity.DRINK.get(),
                        value -> (float) Math.pow(100 - value, 1.5),
                        value -> true
                )
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
    public float getMood() {
        return 0;
    }

    @Override
    public float getStress() {
        return 0;
    }

    @Override
    public void modifyStress(float amount) {

    }

    public void modifySunlight(float amount) {

    }

    @Override
    public void setStress(float amount) {

    }

    @Override
    public Map<Character, Relationship> getRelationships() {
        return null;
    }

    @Override
    public void modifySocialization(float amount) {

    }

    @Override
    public void setSocialization(float amount) {

    }
}
