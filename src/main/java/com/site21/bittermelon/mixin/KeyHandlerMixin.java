package com.site21.bittermelon.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.vehicle.Minecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class KeyHandlerMixin {
    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void handleCustomHotbarKeys(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft)(Object)this;

        if (minecraft.player == null) return;

        for (int i = 0; i < 9; i++) {
            minecraft.options.keyHotbarSlots[i].consumeClick();
        }
    }
}
