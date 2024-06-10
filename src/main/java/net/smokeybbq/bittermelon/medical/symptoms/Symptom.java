package net.smokeybbq.bittermelon.medical.symptoms;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.util.UUID;

public abstract class Symptom {
    protected String name;
    protected float amplifier;
    protected Character character;
    protected ServerPlayer player;
    protected String affectedArea;
    protected int tickTimer = 0;
    protected String description;

    public Symptom (float amplifier, Character character, String affectedArea) {
        this.amplifier = amplifier;
        this.character = character;
        this.affectedArea = affectedArea;

        MinecraftServer server = CharacterManager.getServer();
        UUID playerUUID = character.getPlayerUUID();
        this.player = server.getPlayerList().getPlayer(playerUUID);
    }

    public abstract void update ();

    public abstract void effects ();

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

    public String getDescription() {
        return description;
    }

}

