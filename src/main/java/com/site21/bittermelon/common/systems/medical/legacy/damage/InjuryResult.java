package com.site21.bittermelon.common.systems.medical.legacy.damage;


import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentInstance;

public record InjuryResult(CompartmentInstance injury, String message) {
}
