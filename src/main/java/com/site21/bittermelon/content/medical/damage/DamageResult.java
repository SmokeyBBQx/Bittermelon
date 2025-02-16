package com.site21.bittermelon.content.medical.damage;

import com.site21.bittermelon.content.medical.compartments.Compartment;

import java.util.List;

public record DamageResult(Compartment targetBodyPart, List<InjuryResult> injuryResults) {
}
