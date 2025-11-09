package com.site21.bittermelon.mixin;

import com.site21.bittermelon.common.systems.character.skin.SkinOverrideSystem;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {
    @Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
    private void overrideSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerInfo playerInfo = (PlayerInfo)(Object)this;
        ResourceLocation override = SkinOverrideSystem.getOverriddenSkin(playerInfo.getProfile().getId());
        PlayerSkin.Model model = SkinOverrideSystem.getOverriddenModel(playerInfo.getProfile().getId());
        if (override != null) {
            PlayerSkin customSkin = new PlayerSkin(override, null, null, null, model, true);
            cir.setReturnValue(customSkin);
        }
    }
}
