package net.smokeybbq.bittermelon.medical.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class PathologyBase {

    protected String name;
    protected int tickTimer = 0;
    protected Character character;
    protected ServerPlayer player;
    protected List<String> affectedAreas;
    protected int progressionMildToModerate, progressionModerateToSevere, progressionSevereToCritical, progressionCriticalToTerminal;
    protected float amplifier;
    protected Severity severity;
    protected List<AbstractMap.SimpleEntry<Integer, Severity>> severityThresholds;

    public PathologyBase (Character character, List<String> affectedArea, float amplifier) {
        this.character = character;
        this.affectedAreas = affectedArea;
        this.amplifier = amplifier;

        MinecraftServer server = CharacterManager.getServer();
        UUID playerUUID = character.getPlayerUUID();
        player = server.getPlayerList().getPlayer(playerUUID);

        severityThresholds = List.of(
                new AbstractMap.SimpleEntry<>(progressionCriticalToTerminal, Severity.TERMINAL),
                new AbstractMap.SimpleEntry<>(progressionSevereToCritical, Severity.CRITICAL),
                new AbstractMap.SimpleEntry<>(progressionModerateToSevere, Severity.SEVERE),
                new AbstractMap.SimpleEntry<>(progressionMildToModerate, Severity.MODERATE)
        );
    }

    public abstract void update ();

    protected void tick() {
        tickTimer++;
    }

    public String getName() {
        return name;
    }

    public void increaseAmplifier(float input) {
        amplifier += input;
    }

    public void decreaseAmplifier(float input) {
        amplifier = Math.max(amplifier - input, 0 );
    }

    public float getAmplifier() {
        return amplifier;
    }

    public List<String> getAffectedAreas() {
        return affectedAreas;
    }

    public void updateProgression() {
        severity = Severity.MILD;
        for (AbstractMap.SimpleEntry<Integer, Severity> threshold : severityThresholds) {
            if (amplifier >= threshold.getKey()) {
                severity = threshold.getValue();
                break;
            }
        }
    }
}
