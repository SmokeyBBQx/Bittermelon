package com.site21.bittermelon.mixin;

import com.site21.bittermelon.common.systems.stress.client.StressBarRenderer;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Mutable
    @Shadow
    @Final
    private Map<Gui.ContextualInfo, Supplier<ContextualBarRenderer>> contextualInfoBarRenderers;

    @Unique
    private static final Gui.ContextualInfo bittermelon$STRESS = Enum.valueOf(Gui.ContextualInfo.class, "STRESS");

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Gui;contextualInfoBarRenderers:Ljava/util/Map;", shift = At.Shift.AFTER))
    private void onInit(Minecraft minecraft, CallbackInfo ci) {
        contextualInfoBarRenderers = new HashMap<>(contextualInfoBarRenderers);
        contextualInfoBarRenderers.put(bittermelon$STRESS, () -> new StressBarRenderer(minecraft));
    }

    @Inject(method = "nextContextualInfoState", at = @At("HEAD"), cancellable = true)
    private void onNextContextualInfoState(CallbackInfoReturnable<Gui.ContextualInfo> cir) {
        if (minecraft.player != null && bittermelon$shouldShowStress()) {
            cir.setReturnValue(bittermelon$STRESS);
        }
    }

    @Unique
    private boolean bittermelon$shouldShowStress() {
        if (minecraft.player == null) return false;

        int stress = minecraft.player.getData(BitterAttachmentTypes.STRESS);
        return stress > 0;
    }
}
