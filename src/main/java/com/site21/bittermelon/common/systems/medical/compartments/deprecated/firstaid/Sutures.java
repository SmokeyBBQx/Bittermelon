package com.site21.bittermelon.common.systems.medical.compartments.deprecated.firstaid;

import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.compartments.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.compartments.deprecated.conditionsold.Cut;

import java.util.EnumSet;

public class Sutures extends FirstAid {
    public Sutures(String name, Cut owner, int maxHealth, float quality) {
        super(EnumSet.of(CompartmentTag.STITCHES), name, owner, maxHealth, quality);
        attributes.put(MedicalAttribute.FUNCTION, -owner.getMaxHealth());
    }


}

