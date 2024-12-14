package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.site21.bittermelon.medical.compartments.CompartmentType;

import java.util.Set;

public record AttackSequence(String message, Set<CompartmentType> targets) {
}
