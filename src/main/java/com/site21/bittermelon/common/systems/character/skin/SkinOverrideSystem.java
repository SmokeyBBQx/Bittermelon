package com.site21.bittermelon.common.systems.character.skin;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class SkinOverrideSystem {
    private static final ConcurrentHashMap<UUID, ResourceLocation> skinOverrides = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, String> pendingSkins = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, PlayerSkin.Model> modelOverrides = new ConcurrentHashMap<>();

    public static void setSkinOverride(UUID playerUUID, UUID characterUUID, String skinUrl, PlayerSkin.Model modelType) {
        pendingSkins.put(playerUUID, skinUrl);
        modelOverrides.put(playerUUID, modelType);

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
        modelOverrides.remove(playerUUID);
    }

    public static ResourceLocation getOverriddenSkin(UUID playerUUID) {
        return skinOverrides.get(playerUUID);
    }

    public static PlayerSkin.Model getOverriddenModel(UUID playerUUID) {
        return modelOverrides.getOrDefault(playerUUID, PlayerSkin.Model.WIDE);
    }
}
