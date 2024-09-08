package net.smokeybbq.bittermelon.systems.containment;

import net.smokeybbq.bittermelon.character.Character;

import java.util.UUID;

public class SCP {
    private float containmentGrade;
    private float securityGrade;
    private float researchGrade;
    private float maintenanceGrade;
    private float caretakingGrade;
    private int researchTimer = 0;
    private final int RESEARCH_THRESHOLD = 5184000;
    private final float DECAY = 1.5F;
    private Character character;
    private UUID uuid;
    private float size;

    public SCP(Character character, float size) {
        this.character = character;
        uuid = character.getUUID();
        this.size = size;
    }

    public void update() {
        securityGrade -= DECAY;
        checkForResearchProgress();
    }

    public float getContainmentGrade() {
        containmentGrade = (securityGrade + researchGrade + maintenanceGrade + caretakingGrade) / 4;
        return containmentGrade;
    }

    public float getSecurityGrade() {
        return securityGrade;
    }

    public float getResearchGrade() {
        return researchGrade;
    }

    public float getMaintenanceGrade() {
        return maintenanceGrade;
    }

    public float getCaretakingGrade() {
        return caretakingGrade;
    }

    private void checkForResearchProgress() {
        researchTimer++;
        if (researchTimer >= RESEARCH_THRESHOLD) {

        }
    }
}

