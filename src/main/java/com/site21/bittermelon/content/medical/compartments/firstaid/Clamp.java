package com.site21.bittermelon.content.medical.compartments.firstaid;

import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.compartments.conditionsold.Bleed;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class Clamp extends FirstAid {
    public Clamp(String name, Bleed owner, int maxHealth, float quality, ItemStack item) {
        super(EnumSet.of(CompartmentTag.CLAMP), name, owner, maxHealth, quality);
        this.item = item;

        attributes.put(FunctionType.FUNCTION, -owner.getMaxHealth());
    }


}
