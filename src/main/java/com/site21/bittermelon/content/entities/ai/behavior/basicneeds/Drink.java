package com.site21.bittermelon.content.entities.ai.behavior.basicneeds;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlockEntity;
import com.site21.bittermelon.content.entities.base.NeedsUser;
import com.site21.bittermelon.content.entities.base.Need;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Drink<E extends Mob & HasBasicNeeds & NeedsUser> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(BitterMemoryTypes.NEARBY_DRINKABLE_FLUIDS.get(), MemoryStatus.VALUE_PRESENT)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected void start(@NotNull E entity) {
        List<FluidBlockEntity> nearbyDrinkableFluids = entity.getBrain().getMemory(BitterMemoryTypes.NEARBY_DRINKABLE_FLUIDS.get()).orElse(null);
        if (nearbyDrinkableFluids == null) return;

        List<FluidBlockEntity> drinkableFluids = entity.sortFluids(nearbyDrinkableFluids);

        FluidBlockEntity fluid = drinkableFluids.getFirst();

        if (entity.distanceToSqr(fluid.getBlockPos().getX(), fluid.getBlockPos().getY(), fluid.getBlockPos().getZ()) < 3) {
            entity.getNavigation().stop();
            fluid.transferSubstancesVolume(1);
            // TODO: Figure out how to get nutritional value of food
            entity.modifyNeed(Need.THIRST, -5);
        } else {
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(fluid.getBlockPos(), 1.25f, 0));
        }
    }
}
