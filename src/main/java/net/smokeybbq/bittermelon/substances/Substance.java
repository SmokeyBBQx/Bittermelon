package net.smokeybbq.bittermelon.substances;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.checkerframework.checker.units.qual.C;

public class Substance implements INBTSerializable<CompoundTag> {
    protected String name;
    protected int color;

    public Substance(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Name", name);
        tag.putInt("Color", color);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.name = nbt.getString("Name");
        this.color = nbt.getInt("Color");
    }

    public static Substance fromNBT(CompoundTag nbt) {
        Substance substance = new Substance("", 0);  // Temporary values
        substance.deserializeNBT(nbt);
        return substance;
    }
}
