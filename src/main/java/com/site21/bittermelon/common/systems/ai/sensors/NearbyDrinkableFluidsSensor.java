package com.site21.bittermelon.common.systems.ai.sensors;

import com.site21.bittermelon.common.content.blocks.substance.fluid.FluidBlockEntity;
import com.site21.bittermelon.common.systems.ai.behavior.basicneeds.HasBasicNeeds;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import com.site21.bittermelon.init.neoforge.BitterSensors;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.base.PredicateSensor;
import net.tslat.smartbrainlib.library.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NearbyDrinkableFluidsSensor<E extends Mob & HasBasicNeeds> extends PredicateSensor<E, FluidBlockEntity> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(BitterMemoryTypes.NEARBY_DRINKABLE_FLUIDS.get());

    protected SquareRadius radius = new SquareRadius(32, 16);

    public NearbyDrinkableFluidsSensor() {
        super(HasBasicNeeds::wantsToDrink);
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return BitterSensors.NEARBY_DRINKABLE_FLUIDS.get();
    }

    @Override
    protected void doTick(ServerLevel level, @NotNull E entity) {
        List<FluidBlockEntity> fluids = new ArrayList<>();

        for (BlockPos pos : BlockPos.betweenClosed(
                entity.blockPosition().subtract(this.radius.toVec3i()),
                entity.blockPosition().offset(this.radius.toVec3i())
        )
        ) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (!(blockEntity instanceof FluidBlockEntity fluid)) continue;

            if (this.predicate().test(entity, fluid)) {
                fluids.add(fluid);
            }
        }

        if (fluids.isEmpty()) {
            BrainUtil.clearMemory(entity, BitterMemoryTypes.NEARBY_DRINKABLE_FLUIDS.get());
        } else {
            BrainUtil.setMemory(entity, BitterMemoryTypes.NEARBY_DRINKABLE_FLUIDS.get(), fluids);
        }
    }
}
