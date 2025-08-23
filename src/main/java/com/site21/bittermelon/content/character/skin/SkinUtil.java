package com.site21.bittermelon.content.character.skin;

import com.mojang.authlib.GameProfile;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.character.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class SkinUtil {
    public static @Nullable @Unmodifiable AbstractClientPlayer getAbstractClientPlayer(@NotNull Character character) {
         if (character.getPlayerInfo().isPresent()) {
             return getAbstractClientPlayer(character.getEntityUUID(), character.getName(),
                     ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getUUID()),
                     character.getPlayerInfo().get().getModel().toMinecraftModel());
         }

         return null;
    }

    public static @Nullable @Unmodifiable AbstractClientPlayer getAbstractClientPlayer(@NotNull Character character, PlayerSkin.Model model) {
        if (character.getPlayerInfo().isPresent()) {
            return getAbstractClientPlayer(character.getEntityUUID(), character.getName(),
                    ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getUUID()),
                    model);
        }

        return null;
    }

    public static @Nullable AbstractClientPlayer getAbstractClientPlayer(UUID playerUUID, String name, ResourceLocation skin, PlayerSkin.Model model) {
        AbstractClientPlayer fakePlayer;

        GameProfile profile = new GameProfile(playerUUID, name);
        fakePlayer = new AbstractClientPlayer(Minecraft.getInstance().level, profile) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }

            @Contract(" -> new")
            @Override
            public @NotNull PlayerSkin getSkin() {
                return new PlayerSkin(
                        skin,
                        null,
                        null,
                        null,
                        model,
                        true
                );
            }
        };

        return fakePlayer;
    }
}
