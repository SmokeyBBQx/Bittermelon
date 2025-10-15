package com.site21.bittermelon.common.systems.medical.damage;


import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;

public record InjuryResult(CompartmentInstance injury, String message) {
}
