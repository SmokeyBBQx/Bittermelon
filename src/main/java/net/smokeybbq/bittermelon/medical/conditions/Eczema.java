package net.smokeybbq.bittermelon.medical.conditions;

import net.minecraft.world.entity.player.Player;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.symptoms.Rash;

public class Eczema extends Condition {
    private Rash rash;

    private String[] suitableTreatments = {"Penicillin"};

    public Eczema(float duration, boolean chronic, Character character, String affectedArea, float amplifier) {
        super(duration, chronic, character, affectedArea, amplifier);
    }

    @Override
    protected void symptoms() {

    }

    @Override
    public void treat(Substance drug, float effectiveness) {

    }

    @Override
    public void update() {

    }
}


