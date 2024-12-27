package com.site21.bittermelon.medical.damage;

import com.site21.bittermelon.medical.compartments.Compartment;
import it.unimi.dsi.fastutil.Pair;

import java.util.List;

public record DamageResult(Compartment targetBodyPart, List<InjuryResult> injuryResults) {
}
