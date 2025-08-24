package com.site21.bittermelon.mixin;

import com.site21.bittermelon.networking.server.SetLastTypingTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.LAST_TYPING_TIME;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(method = "onEdited(Ljava/lang/String;)V", at = @At("HEAD"))
    private void onMessageBeingTyped(String value, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        long currentTime = System.currentTimeMillis();
        Long lastTypingTime = player.getExistingDataOrNull(LAST_TYPING_TIME);

        if (lastTypingTime == null || (currentTime - lastTypingTime) > 4000) {
            player.setData(LAST_TYPING_TIME, currentTime);
            PacketDistributor.sendToServer(new SetLastTypingTime(player.getUUID(), currentTime));
        }
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void onChatClosed(CallbackInfo ci) {
        PacketDistributor.sendToServer(new SetLastTypingTime(Minecraft.getInstance().player.getUUID(), -1));
    }
}
