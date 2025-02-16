package com.site21.bittermelon.content.medical.damage;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.Compartment;
import com.site21.bittermelon.content.medical.compartments.conditions.CardiacArrest;
import com.site21.bittermelon.content.medical.compartments.organs.HeartRhythm;
import org.jetbrains.annotations.NotNull;

public class HeartAttack {
    public static void causeHeartAttack(Character character, Compartment heart, @NotNull HeartRhythm heartRhythm) {
        CardiacArrest cardiacArrest = new CardiacArrest(heartRhythm.name(), heart, 100, character, null, heartRhythm);
        character.getMedicalStats().addCompartment(cardiacArrest);
    }
}
