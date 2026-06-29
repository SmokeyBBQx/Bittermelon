package com.site21.bittermelon.mixin;

import com.site21.bittermelon.common.systems.character.skin.SkinOverrideSystem;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {
    @Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
    private void overrideSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerInfo playerInfo = (PlayerInfo)(Object)this;
        ClientAsset.Texture override = SkinOverrideSystem.getOverriddenSkin(playerInfo.getProfile().id());
        PlayerModelType model = SkinOverrideSystem.getOverriddenModel(playerInfo.getProfile().id());
        if (override != null) {
            PlayerSkin customSkin = new PlayerSkin(override, null, null, model, true);
            cir.setReturnValue(customSkin);
        }
    }
}
