package com.site21.bittermelon.common.systems.roles;

import com.site21.bittermelon.common.systems.character.Character;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class Role {
    public final String name;
    public final int color;
    public final String quote;
    public final String description;
    public Identifier skinLocation;
    private boolean whitelisted = false;
    private boolean defaultRole = false;
    private Component addMessage;

    public Role(String name, int color, String quote, String description) {
        this.name = name;
        this.color = color;
        this.quote = quote;
        this.description = description;
    }

    public Role whitelisted() {
        whitelisted = true;
        return this;
    }

    public Role defaultRole() {
        defaultRole = true;
        return this;
    }

    public Role addMessage(Component addMessage) {
        this.addMessage = addMessage;
        return this;
    }

    public Role skinLocation(Identifier skinLocation) {
        this.skinLocation = skinLocation;
        return this;
    }

    public boolean isWhitelisted() {
        return whitelisted;
    }

    public boolean isDefaultRole() {
        return defaultRole;
    }

    public void onRoleAdded(Player player, Character character) {
        if (addMessage != null) {
            player.sendSystemMessage(addMessage);
        }
    }
}
