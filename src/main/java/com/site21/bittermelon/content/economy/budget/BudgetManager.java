package com.site21.bittermelon.content.economy.budget;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.EnumMap;

public class BudgetManager extends SavedData {
    private float totalBudget = 0;
    private final EnumMap<BudgetCategory, Float> allocations = new EnumMap<>(BudgetCategory.class);

    public void allocate(BudgetCategory category, float percentage) {
        allocations.put(category, percentage);
    }

    public float getBudget(BudgetCategory category) {
        return allocations.get(category) * totalBudget;
    }

    public float getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(float budget) {
        totalBudget = budget;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return null;
    }
}
