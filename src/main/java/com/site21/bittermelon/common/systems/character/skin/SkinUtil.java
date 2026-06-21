package com.site21.bittermelon.common.systems.character.skin;

import com.mojang.authlib.GameProfile;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.UUID;

public class SkinUtil {
    public static @Nullable @Unmodifiable AbstractClientPlayer getAbstractClientPlayer(@NotNull Character character) {
         if (character.getPlayerInfo().isPresent()) {
             return getAbstractClientPlayer(character.getEntityUUID(), character.getName(),
                     Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getId()),
                     character.getPlayerInfo().get().getModel().toMinecraftModel());
         }

         return null;
    }

    public static @Nullable @Unmodifiable AbstractClientPlayer getAbstractClientPlayer(@NotNull Character character, PlayerSkin.Model model) {
        if (character.getPlayerInfo().isPresent()) {
            return getAbstractClientPlayer(character.getEntityUUID(), character.getName(),
                    Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getId()),
                    model);
        }

        return null;
    }

    public static @NotNull AbstractClientPlayer getAbstractClientPlayer(UUID playerUUID, String name, Identifier skin, PlayerSkin.Model model) {
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

        fakePlayer.getEntityData().set(Player.DATA_PLAYER_MODE_CUSTOMISATION, (byte) 126);

        return fakePlayer;
    }
}
