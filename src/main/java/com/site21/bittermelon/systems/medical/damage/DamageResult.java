package com.site21.bittermelon.systems.medical.damage;

import com.site21.bittermelon.systems.medical.compartment.CompartmentInstance;

import java.util.List;

public record DamageResult(CompartmentInstance targetBodyPart, List<InjuryResult> injuryResults) {
}
