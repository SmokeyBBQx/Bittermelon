package com.site21.bittermelon.items.medical.tools;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.items.base.BaseItem;
import com.site21.bittermelon.items.base.ItemWeight;
import com.site21.bittermelon.items.medical.AbstractCautery;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.world.item.Item;

import java.util.List;

public class Cautery extends BaseItem implements AbstractCautery {
    public Cautery(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

}
