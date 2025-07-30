package com.site21.bittermelon.mixin;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
    @Shadow @Final private List<GuiMessage> allMessages;

    @Shadow @Final public Minecraft minecraft;

    @Shadow public abstract void refreshTrimmedMessages();

    @Unique
    private int bittermelon$lastMessageCount = 1;

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V", at = @At("HEAD"), cancellable = true)
    private void onAddMessage(Component chatComponent, @javax.annotation.Nullable MessageSignature headerSignature, @Nullable GuiMessageTag tag, CallbackInfo info) {
        if (allMessages.isEmpty()) return;

        String newMessage = chatComponent.getString() + (bittermelon$lastMessageCount > 1 ? " (x" + bittermelon$lastMessageCount + ")" : "");

        if (allMessages.getFirst().content().getString().equals(newMessage)) {
            bittermelon$lastMessageCount++;

            GuiMessage guiMessage = new GuiMessage(
                    minecraft.gui.getGuiTicks(),
                    chatComponent.copy().append(" (x" + bittermelon$lastMessageCount + ")"),
                    headerSignature,
                    tag
            );
            allMessages.set(0, guiMessage);
            refreshTrimmedMessages();

            info.cancel();
        } else {
            bittermelon$lastMessageCount = 1;
        }
    }
}
