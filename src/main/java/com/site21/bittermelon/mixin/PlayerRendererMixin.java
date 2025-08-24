package com.site21.bittermelon.mixin;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @ModifyVariable(
            method = "renderNameTag(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V",
            at = @At("HEAD"),
            index = 2,
            argsOnly = true
    )
    private Component modifyDisplayName(Component displayName, @NotNull AbstractClientPlayer entity) {
        if (entity.getExistingDataOrNull(BitterAttachmentTypes.LAST_TYPING_TIME) != null) {
            return Component.empty()
                    .append(displayName)
                    .append(Component.literal(" \uE003").withStyle(style -> style.withFont(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "default"))));
        }
        return displayName;
    }
}
