package net.smokeybbq.bittermelon.items.substancecontainers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.smokeybbq.bittermelon.systems.substances.Substance;

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
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag substancesList = new ListTag();
        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            CompoundTag substanceTag = entry.getKey().serializeNBT();
            substanceTag.putInt("Amount", entry.getValue());
            substancesList.add(substanceTag);
        }
        nbt.put("Substances", substancesList);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        substances.clear();
        ListTag substancesList = nbt.getList("Substances", 10);
        for (int i = 0; i < substancesList.size(); i++) {
            CompoundTag substanceTag = substancesList.getCompound(i);
            Substance substance = Substance.fromNBT(substanceTag);
            int amount = substanceTag.getInt("Amount");
            substances.put(substance, amount);
        }
    }
}
