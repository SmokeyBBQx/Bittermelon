package net.smokeybbq.bittermelon.medical.symptoms;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.util.UUID;

public abstract class Symptom {
    protected String name;
    protected double amplifier;
    protected Character character;
    protected ServerPlayer player;

    public Symptom (double amplifier, Character character) {
        this.amplifier = amplifier;
        this.character = character;

        MinecraftServer server = CharacterManager.getServer();
        UUID playerUUID = character.getPlayerUUID();
        this.player = server.getPlayerList().getPlayer(playerUUID);
    }

    public String getName() {
        return name;
    }

}

