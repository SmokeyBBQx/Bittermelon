package com.site21.bittermelon.content.medical.compartments.bodyparts;

import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.compartments.organs.HeartRhythm;

import java.util.EnumSet;

public class Heart extends BodyPart {
    private HeartRhythm heartRhythm = HeartRhythm.SINUS_RHYTHM;

    public Heart(EnumSet<CompartmentType> types, String name, Compartment owner, float maxHealth) {
        super(types, name, owner, maxHealth);
    }

    public HeartRhythm getHeartRhythm() {
        return heartRhythm;
    }

    public void setHeartRhythm(HeartRhythm heartRhythm) {
        this.heartRhythm = heartRhythm;
    }
}
