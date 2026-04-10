package com.site21.bittermelon.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ENRAGED;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(
            method = "turnPlayer",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onTurnPlayer(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (mc.player.getData(ENRAGED)) {
            ci.cancel();
        }
    }
}
