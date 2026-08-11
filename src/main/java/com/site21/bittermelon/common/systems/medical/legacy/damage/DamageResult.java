package com.site21.bittermelon.common.systems.medical.legacy.damage;

import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentInstance;

import java.util.List;

public record DamageResult(CompartmentInstance targetBodyPart, List<InjuryResult> injuryResults) {
}
