package com.site21.bittermelon.content.medical.factory;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;

public interface AnatomyFactory {
    MedicalStats build(BloodType bloodType, Character character);
}
