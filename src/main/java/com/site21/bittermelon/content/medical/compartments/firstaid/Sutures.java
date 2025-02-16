package com.site21.bittermelon.content.medical.compartments.firstaid;

import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.compartments.conditions.Cut;

import java.util.EnumSet;

public class Sutures extends FirstAid {
    public Sutures(String name, Cut owner, int maxHealth, float quality) {
        super(EnumSet.of(CompartmentType.STITCHES), name, owner, maxHealth, quality);
        attributes.put(FunctionType.FUNCTION, -owner.getMaxHealth());
    }


}

