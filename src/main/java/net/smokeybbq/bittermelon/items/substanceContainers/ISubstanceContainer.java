package net.smokeybbq.bittermelon.items.substanceContainers;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.smokeybbq.bittermelon.substances.Substance;

import java.util.Map;

public interface ISubstanceContainer extends INBTSerializable<CompoundTag> {
    void updateSubstance(Substance substance, int amount);
    void setSubstances(Map<Substance, Integer> substances);
    Map<Substance, Integer> getSubstances();
    int getTotalAmount();

    int getCapacity();
}
