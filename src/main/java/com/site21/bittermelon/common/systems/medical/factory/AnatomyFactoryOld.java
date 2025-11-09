package com.site21.bittermelon.common.systems.medical.factory;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.medicalstats.deprecated.MedicalStatsOld;

public interface AnatomyFactoryOld {
    MedicalStatsOld build(BloodType bloodType, Character character);
}
