package net.smokeybbq.bittermelon.events;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinChangeHandler {
    private static final Map<UUID, String> skinUrlMap = new HashMap<>();
    private static final String DEFAULT_SKIN_URL = "https://i.imgur.com/GjeaREP.png";

    public static void setSkinUrl(UUID playerUUID, String skinUrl) {
        skinUrlMap.put(playerUUID, skinUrl);
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        event.setCanceled(true);

        if (event.getEntity() instanceof AbstractClientPlayer clientPlayer) {
            updatePlayerSkin(event, clientPlayer);
        }
    }

    private void updatePlayerSkin(RenderPlayerEvent.Pre event, AbstractClientPlayer player) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        String skinUrl = skinUrlMap.getOrDefault(player.getUUID(), DEFAULT_SKIN_URL);
        //ResourceLocation skinLocation = new ResourceLocation("bittermelon", "skins/" + skinUrl.hashCode() + ".png");
        ResourceLocation skinLocation = new ResourceLocation("bittermelon", "textures/entity/default.png");

        File skinFile = new File("assets/bittermelon/textures/entity/default.png");

        // Register and bind the texture
        //AbstractTexture httpTexture = new HttpTexture(skinFile, null, skinLocation, true, null);
        AbstractTexture httpTexture = new SimpleTexture(skinLocation);

        textureManager.register(skinLocation, httpTexture);

        // Logging the current skin texture location
        System.out.println("Current skin texture location: " + player.getSkinTextureLocation());

        // Update the player's skin texture if it's different from the current one
        ResourceLocation currentSkin = player.getSkinTextureLocation();
        // if (!skinLocation.equals(currentSkin)) {
            player.registerSkinTexture(skinLocation, "texture");
            System.out.println("Updated skin texture to: " + skinLocation);
        //} else {
          //  System.out.println("Skin texture is already set to: " + skinLocation);
        //}
    }
}
