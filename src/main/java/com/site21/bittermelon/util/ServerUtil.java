package com.site21.bittermelon.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ServerUtil {
    private static MinecraftServer minecraftServer;

    public static void setMinecraftServer(MinecraftServer server) {
        minecraftServer = server;
    }

    public static MinecraftServer getServer() {
        return minecraftServer;
    }

    public static @Nullable Entity getEntity(UUID uuid) {
        for (ServerLevel level : getServer().getAllLevels()) {
            Entity entity = level.getEntities().get(uuid);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

    public static @Nullable LivingEntity getLivingEntity(UUID uuid) {
        if (getEntity(uuid) instanceof LivingEntity livingEntity) {
            return livingEntity;
        }
        return null;
    }
}
