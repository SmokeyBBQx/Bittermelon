package net.smokeybbq.bittermelon.medical.conditions;

import net.minecraft.world.entity.player.Player;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.symptoms.Rash;

import java.util.List;

public class Eczema extends Condition {
    private Rash rash;

    private String[] suitableTreatments = {""};

    public Eczema(float duration, boolean chronic, Character character, List<String> affectedAreas, float amplifier) {
        super(duration, chronic, character, affectedAreas, amplifier);
    }

    @Override
    protected void symptoms() {

    }

    @Override
    public void treat(Substance drug, float effectiveness, String area) {

    }

    @Override
    public void update() {

    }
}


