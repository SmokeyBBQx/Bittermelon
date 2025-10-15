package com.site21.bittermelon.systems.medical.factory;

import com.site21.bittermelon.systems.character.Character;
import com.site21.bittermelon.systems.medical.blood.BloodType;
import com.site21.bittermelon.systems.medical.medicalstats.MedicalStats;

public interface AnatomyFactory {
    MedicalStats build(BloodType bloodType, Character character);
}
