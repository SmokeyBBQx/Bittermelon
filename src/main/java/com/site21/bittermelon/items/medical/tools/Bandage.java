package com.site21.bittermelon.items.medical.tools;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.items.base.BaseItem;
import com.site21.bittermelon.items.base.ItemWeight;
import com.site21.bittermelon.items.medical.AbstractBandage;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.world.item.ItemStack;

public class Bandage extends BaseItem implements AbstractBandage {

    public Bandage(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }
}
