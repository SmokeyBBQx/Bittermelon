package com.site21.bittermelon.content.medical.damage;


import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.Injury;

public record InjuryResult(CompartmentInstance injury, String message) {
}
