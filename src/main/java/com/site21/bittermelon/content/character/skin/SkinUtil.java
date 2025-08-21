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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkinUtil {
    public static @Nullable AbstractClientPlayer getAbstractClientPlayer(@NotNull Character character) {
        AbstractClientPlayer fakePlayer = null;

        if (character.getPlayerInfo().isPresent()) {
            PlayerInfo playerInfo = character.getPlayerInfo().get();

            GameProfile profile = new GameProfile(character.getEntityUUID(), character.getName());
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
                            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getUUID()),
                            null,
                            null,
                            null,
                            playerInfo.getModel(),
                            true
                    );
                }
            };
        }

        return fakePlayer;
    }
}
