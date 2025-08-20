package com.site21.bittermelon.content.roles;

import com.site21.bittermelon.content.character.Character;
import net.minecraft.world.entity.LivingEntity;

public class Role {
    public final String name;
    public final int color;
    private boolean whitelisted = false;
    private boolean defaultRole = false;

    public Role(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public Role whitelisted() {
        whitelisted = true;
        return this;
    }

    public Role defaultRole() {
        defaultRole = true;
        return this;
    }

    public boolean isWhitelisted() {
        return whitelisted;
    }

    public boolean isDefaultRole() {
        return defaultRole;
    }

    public void onRoleAdded(LivingEntity entity, Character character) {

    }
}
