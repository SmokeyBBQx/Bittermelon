package net.smokeybbq.bittermelon.systems.medical.conditions;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.systems.medical.common.PathologyBase;
import net.smokeybbq.bittermelon.systems.medical.symptoms.Symptom;

import java.util.ArrayList;
import java.util.List;

public abstract class Condition extends PathologyBase {
    protected float duration;
    protected boolean chronic;
    protected List<Symptom> symptoms = new ArrayList<>();
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

    public List<Symptom> getSymptoms() {
        return symptoms;
    }
}
