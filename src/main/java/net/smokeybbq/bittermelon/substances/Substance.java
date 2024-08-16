package net.smokeybbq.bittermelon.substances;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;
import org.checkerframework.checker.units.qual.C;

import java.util.Objects;

public class Substance implements INBTSerializable<CompoundTag> {
    protected String name;
    protected int color;
    protected String flavor;

    public Substance(String name, int color, String flavor) {
        this.name = name;
        this.color = color;
        this.flavor = flavor;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getFlavor() {return flavor;}

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Name", name);
        tag.putInt("Color", color);
        tag.putString("Flavor", flavor);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.name = nbt.getString("Name");
        this.color = nbt.getInt("Color");
        this.flavor = nbt.getString("Flavor");
    }

    public static Substance fromNBT(CompoundTag nbt) {
        Substance substance = new Substance("", 0, "");  // Temporary values
        substance.deserializeNBT(nbt);
        return substance;
    }

    public void getEffects(Entity entity, int amount) {

    }

}
