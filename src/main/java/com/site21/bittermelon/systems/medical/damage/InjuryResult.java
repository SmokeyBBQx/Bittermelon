package com.site21.bittermelon.systems.medical.damage;


import com.site21.bittermelon.systems.medical.compartment.CompartmentInstance;

public record InjuryResult(CompartmentInstance injury, String message) {
}
