package com.site21.bittermelon.content.entities.ai.sensors;

import com.site21.bittermelon.content.entities.ai.behavior.basicneeds.HasBasicNeeds;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import com.site21.bittermelon.init.neoforge.BitterSensors;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.List;

public class NearbyFoodSensor<E extends Mob & HasBasicNeeds> extends PredicateSensor<ItemEntity, E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(BitterMemoryTypes.NEARBY_EDIBLE_ITEMS.get());

    protected SquareRadius radius = new SquareRadius(32, 16);

    public NearbyFoodSensor() {
        super((item, entity) -> entity.wantsToEat(item.getItem()) && entity.hasLineOfSight(item));
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return BitterSensors.NEARBY_EDIBLE_ITEMS.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        List<ItemEntity> foodItems = EntityRetrievalUtil.getEntities(
                entity,
                this.radius.xzRadius(),
                this.radius.yRadius(),
                this.radius.xzRadius(),
                ItemEntity.class,
                item -> predicate().test(item, entity));

        BrainUtils.setMemory(entity, BitterMemoryTypes.NEARBY_EDIBLE_ITEMS.get(), foodItems);
    }
}
