package net.smokeybbq.bittermelon.medical.conditions;

import net.minecraft.world.entity.player.Player;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.symptoms.Rash;

public class Eczema extends Condition {
    private Rash rash;

    private String[] suitableTreatments = {"Penicillin"};

    public Eczema(double duration, boolean chronic, double severity, String affectedArea, Character character) {
        super(duration, chronic, severity, affectedArea, character);

        symptoms();
    }

    @Override
    public void update() {

    }

    @Override
    protected void symptoms() {
        this.rash = new Rash(severity, character, affectedArea);
    }

    @Override
    public void treat(Substance drug, double effectiveness) {
        setSeverity(severity - effectiveness);
    }

}
