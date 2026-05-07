package com.site21.bittermelon.common.content.entities.mimicplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class LocalMimic extends Mimic {
    @Nullable
    private PlayerInfo playerInfo;

    public LocalMimic(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    protected PlayerInfo getPlayerInfo() {
        if (playerInfo == null) {
            Player player = getPlayer();
            if (player == null) return null;

            playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getUUID());
        }

        return playerInfo;
    }

    public PlayerSkin getSkin() {
        PlayerInfo info = getPlayerInfo();
        return info == null ? DefaultPlayerSkin.get(getUUID()) : info.getSkin();
    }
}
