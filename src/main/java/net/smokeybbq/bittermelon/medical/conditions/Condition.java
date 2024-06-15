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
    protected List<String> affectedAreas;

    public Condition(float duration, boolean chronic, Character character, List<String> affectedAreas, float amplifier) {
        super(character, amplifier);
        this.duration = duration;
        this.chronic = chronic;
        this.affectedAreas = affectedAreas;
        symptoms();
    }

    protected abstract void symptoms();

    public void addAffectedArea(String area) {
        affectedAreas.add(area);
    }

    public List<String> getAffectedAreas() {
        return affectedAreas;
    }

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

    public abstract void treat(Substance drug, float effectiveness, String area);
}
