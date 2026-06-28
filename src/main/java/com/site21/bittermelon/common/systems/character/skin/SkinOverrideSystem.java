package com.site21.bittermelon.common.systems.character.skin;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SkinOverrideSystem {
    private static final ConcurrentHashMap<UUID, Identifier> skinOverrides = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, String> pendingSkins = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, PlayerModelType> modelOverrides = new ConcurrentHashMap<>();

    public static void setSkinOverride(UUID playerUUID, UUID characterUUID, String skinUrl, PlayerModelType modelType) {
        pendingSkins.put(playerUUID, skinUrl);
        modelOverrides.put(playerUUID, modelType);

        SkinManager.loadSkin(skinUrl, String.valueOf(characterUUID), () -> {
            Identifier skinTexture = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + characterUUID);
            skinOverrides.put(playerUUID, skinTexture);
            pendingSkins.remove(playerUUID);
        });
    }

    public static void removeSkinOverride(UUID playerUUID) {
        skinOverrides.remove(playerUUID);
        pendingSkins.remove(playerUUID);
        modelOverrides.remove(playerUUID);
    }

    public static Identifier getOverriddenSkin(UUID playerUUID) {
        return skinOverrides.get(playerUUID);
    }

    public static PlayerModelType getOverriddenModel(UUID playerUUID) {
        return modelOverrides.getOrDefault(playerUUID, PlayerModelType.WIDE);
    }
}
