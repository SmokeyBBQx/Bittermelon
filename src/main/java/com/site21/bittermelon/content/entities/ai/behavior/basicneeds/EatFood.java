package com.site21.bittermelon.content.entities.ai.behavior.basicneeds;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EatFood<E extends Mob & HasBasicNeeds> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(BitterMemoryTypes.NEARBY_EDIBLE_ITEMS.get(), MemoryStatus.VALUE_PRESENT)
      );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(@NotNull E entity) {
        List<ItemEntity> nearbyFoodItems = entity.getBrain().getMemory(BitterMemoryTypes.NEARBY_EDIBLE_ITEMS.get()).orElse(null);
        if (nearbyFoodItems == null) return;

        List<ItemEntity> foodItems = entity.sortEdibleItems(nearbyFoodItems);

        ItemEntity itemEntity = foodItems.getFirst();
        ItemStack stack = itemEntity.getItem();

        if (entity.distanceToSqr(itemEntity) < 1) {
            entity.getNavigation().stop();
            stack.shrink(1);
            stack.finishUsingItem(entity.level(), entity);
            // TODO: Figure out how to get nutritional value of food
            entity.modifyHunger(-5);
        } else {
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(itemEntity.blockPosition(), 1.25f, 0));
        }
    }
}
