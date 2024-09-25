package com.site21.bittermelon.items.containers.substancecontainers;

import com.site21.bittermelon.substance.Substance;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Map;

public interface ISubstanceContainer extends INBTSerializable<CompoundTag> {
    void updateSubstance(Substance substance, int amount);
    void setSubstances(Map<Substance, Integer> substances);
    Map<Substance, Integer> getSubstances();
    int getTotalAmount();
    int getCapacity();
}
