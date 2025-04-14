package com.site21.bittermelon.content.medical.factory;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;

public interface AnatomyFactoryOld {
    MedicalStatsOld build(BloodType bloodType, Character character);
}
