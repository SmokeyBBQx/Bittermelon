package net.smokeybbq.bittermelon.medical.symptoms;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.medical.common.PathologyBase;
import net.smokeybbq.bittermelon.medical.common.Severity;

import java.util.AbstractMap;
import java.util.List;
import java.util.UUID;

public abstract class Symptom extends PathologyBase {
    protected String MILD_DESCRIPTION, MODERATE_DESCRIPTION, SEVERE_DESCRIPTION, CRITICAL_DESCRIPTION, TERMINAL_DESCRIPTION;

    public Symptom (Character character, List<String> affectedAreas, float amplifier) {
        super(character, affectedAreas, amplifier);
        initializeDescriptions();
    }

    public abstract void initializeDescriptions();

    public abstract void effects();

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
}

