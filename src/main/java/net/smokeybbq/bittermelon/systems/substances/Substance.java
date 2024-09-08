package net.smokeybbq.bittermelon.systems.substances;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;

public class Substance implements INBTSerializable<CompoundTag> {
    protected String name;
    protected Properties properties;

    public static class Properties {
        int solidColor;
        int liquidColor;
        int gasColor;
        int plasmaColor;
        String flavor;
        float freezingTemperature;
        float boilingTemperature;
        float plasmaTemperature;

    }

    public Substance(String name, Properties properties) {
        this.name = name;
    }

    public int getColor(int temperature) {
        if (temperature < properties.freezingTemperature) {
            return properties.solidColor;
        } else if (temperature < properties.boilingTemperature) {
            return properties.liquidColor;
        } else if (temperature < properties.plasmaTemperature) {
            return properties.gasColor;
        } else {
            return properties.plasmaColor;
        }
    }

    public String getName() {
        return name;
    }

    public String getFlavor() {
        return properties.flavor;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Name", name);
//        tag.putInt("Color", color);
//        tag.putString("Flavor", flavor);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.name = nbt.getString("Name");
//        this.color = nbt.getInt("Color");
//        this.flavor = nbt.getString("Flavor");
    }

    public static Substance fromNBT(CompoundTag nbt) {
//        Substance substance = new Substance("", 0, "");  // Temporary values
//        substance.deserializeNBT(nbt);
//        return substance;
    }

    public void getEffects(Entity entity, int amount) {

    }

    public boolean compare(Substance substance) {
        return Objects.equals(substance.getName(), this.name);
    }
}
