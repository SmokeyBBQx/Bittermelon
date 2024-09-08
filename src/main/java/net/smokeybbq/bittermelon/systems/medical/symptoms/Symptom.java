package net.smokeybbq.bittermelon.systems.medical.symptoms;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.systems.medical.common.PathologyBase;

public abstract class Symptom extends PathologyBase {
    protected String MILD_DESCRIPTION, MODERATE_DESCRIPTION, SEVERE_DESCRIPTION, CRITICAL_DESCRIPTION, TERMINAL_DESCRIPTION;
    protected String MILD_REMINDER, MODERATE_REMINDER, SEVERE_REMINDER, CRITICAL_REMINDER, TERMINAL_REMINDER;
    protected String affectedArea;

    public Symptom (Character character, String affectedArea, float amplifier) {
        super(character, amplifier);
        this.affectedArea = affectedArea;
        initializeDescriptions();
    }

    public abstract void initializeDescriptions();

    public abstract void effects();

    public String getAffectedArea() {
        return affectedArea;
    }

    public String getDescription() {
        switch(severity) {
            case MILD -> {
                return MILD_DESCRIPTION;
            }
            case MODERATE -> {
                return MODERATE_DESCRIPTION;
            }
            case SEVERE -> {
                return SEVERE_DESCRIPTION;
            }
            case CRITICAL -> {
                return CRITICAL_DESCRIPTION;
            }
            case TERMINAL -> {
                return TERMINAL_DESCRIPTION;
            }
            default -> {
                return null;
            }
        }
    }

    public String getReminder() {
        switch(severity) {
            case MILD -> {
                return MILD_REMINDER;
            }
            case MODERATE -> {
                return MODERATE_REMINDER;
            }
            case SEVERE -> {
                return SEVERE_REMINDER;
            }
            case CRITICAL -> {
                return CRITICAL_REMINDER;
            }
            case TERMINAL -> {
                return TERMINAL_REMINDER;
            }
            default -> {
                return null;
            }
        }
    }
}

