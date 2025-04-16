package com.site21.bittermelon.content.medical.damage;


import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;

public record InjuryResult(CompartmentInstance injury, String message) {
}
