package com.site21.bittermelon.medical.factory;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.blood.BloodType;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;

public interface AnatomyFactory {
    MedicalStats build(BloodType bloodType, Character character);
}
