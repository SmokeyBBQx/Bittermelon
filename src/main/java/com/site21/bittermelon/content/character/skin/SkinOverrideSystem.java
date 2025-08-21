package com.site21.bittermelon.content.character.skin;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SkinOverrideSystem {
    private static final ConcurrentHashMap<UUID, ResourceLocation> skinOverrides = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, String> pendingSkins = new ConcurrentHashMap<>();

    public static void setSkinOverride(UUID playerUUID, UUID characterUUID, String skinUrl) {
        pendingSkins.put(playerUUID, skinUrl);

        SkinManager.loadSkin(skinUrl, String.valueOf(characterUUID), () -> {
            ResourceLocation skinTexture = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID,
                    "skins/" + characterUUID);
            skinOverrides.put(playerUUID, skinTexture);
            pendingSkins.remove(playerUUID);
        });
    }

    public static void removeSkinOverride(UUID playerUUID) {
        skinOverrides.remove(playerUUID);
        pendingSkins.remove(playerUUID);
    }

    public static ResourceLocation getOverriddenSkin(UUID playerUUID) {
        return skinOverrides.get(playerUUID);
    }

    public static boolean hasSkinOverride(UUID playerUUID) {
        return skinOverrides.containsKey(playerUUID) || pendingSkins.containsKey(playerUUID);
    }
}
