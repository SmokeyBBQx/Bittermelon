package net.smokeybbq.bittermelon.medical.conditions;

import net.minecraft.world.entity.player.Player;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.common.PathologyBase;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

import java.util.ArrayList;
import java.util.List;

public abstract class Condition extends PathologyBase {
    protected float duration;
    protected boolean chronic;
    protected List<Symptom> symptoms = new ArrayList<>();
    protected String[] suitableTreatments;

    public Condition(float duration, boolean chronic, Character character, String affectedArea, float amplifier) {
        super(character, affectedArea, amplifier);
        this.duration = duration;
        this.chronic = chronic;
        symptoms();
    }

    protected abstract void symptoms();

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setChronic(boolean chronic) {
        this.chronic = chronic;
    }

    public String[] getSuitableTreatments() {
        return suitableTreatments;
    }

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public abstract void treat(Substance drug, float effectiveness);
}
