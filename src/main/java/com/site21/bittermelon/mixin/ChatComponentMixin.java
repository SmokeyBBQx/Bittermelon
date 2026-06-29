package com.site21.bittermelon.mixin;

import com.site21.bittermelon.common.systems.chat.AlphaContainer;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
    @Redirect(
            method = "forEachLine",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/ChatComponent$AlphaCalculator;calculate(Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;)F"
            )
    )
    private float modifyAlpha(ChatComponent.AlphaCalculator calculator, GuiMessage.Line line) {
        float original = calculator.calculate(line);
        AlphaContainer alphaContainer = line.parent().content().getStyle();
        return original * alphaContainer.bittermelon$getAlpha();
    }
}
