package com.site21.bittermelon.common.systems.medical.factory;

import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;

public interface AnatomyFactory {
    MedicalStats build(BloodType bloodType);
}
