package net.smokeybbq.bittermelon.medical.conditions;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;

import java.util.List;

public class Asthma extends Condition {
    public Asthma(float duration, boolean chronic, Character character, List<String> affectedAreas, float amplifier) {
        super(duration, chronic, character, affectedAreas, amplifier);
    }

    @Override
    public void update() {

    }

    @Override
    protected void symptoms() {

    }

    @Override
    public void treat(Substance drug, float effectiveness, String area) {

    }
}
