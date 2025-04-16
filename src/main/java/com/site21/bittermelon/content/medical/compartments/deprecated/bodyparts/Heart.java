package com.site21.bittermelon.content.medical.compartments.deprecated.bodyparts;

import com.site21.bittermelon.content.medical.compartments.deprecated.CompartmentOld;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.deprecated.organs.HeartRhythm;

import java.util.EnumSet;

public class Heart extends BodyPart {
    private HeartRhythm heartRhythm = HeartRhythm.SINUS_RHYTHM;

    public Heart(EnumSet<CompartmentTag> types, String name, CompartmentOld owner, float maxHealth) {
        super(types, name, owner, maxHealth);
    }

    public HeartRhythm getHeartRhythm() {
        return heartRhythm;
    }

    public void setHeartRhythm(HeartRhythm heartRhythm) {
        this.heartRhythm = heartRhythm;
    }
}
