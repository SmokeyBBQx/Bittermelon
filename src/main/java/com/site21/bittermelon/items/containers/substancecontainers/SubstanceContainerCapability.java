package com.site21.bittermelon.items.containers.substancecontainers;

import com.site21.bittermelon.substance.Substance;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.Map;

public class SubstanceContainerCapability implements ISubstanceContainer {
    private Map<Substance, Integer> substances = new HashMap<>();
    private final int capacity;

    public SubstanceContainerCapability(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void updateSubstance(Substance substance, int delta) {
        int currentAmount = substances.getOrDefault(substance, 0);
        int newAmount = Math.min(capacity, currentAmount + delta);

        if (newAmount > 0) {
            substances.put(substance, newAmount);
        } else {
            substances.remove(substance);
        }
    }

    @Override
    public void setSubstances(Map<Substance, Integer> substances) {
        this.substances = substances;
    }

    @Override
    public Map<Substance, Integer> getSubstances() {
        return new HashMap<>(substances);
    }

    @Override
    public int getTotalAmount() {
        return substances.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

    }
}
