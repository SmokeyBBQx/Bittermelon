package com.site21.bittermelon.content.medical.damage;

import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentOld;

import java.util.List;

public record DamageResult(CompartmentInstance targetBodyPart, List<InjuryResult> injuryResults) {
}
