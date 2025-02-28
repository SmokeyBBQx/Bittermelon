package com.site21.bittermelon.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayerDropMixin {
    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    private void onDrop(boolean fullStack, CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = (LocalPlayer)(Object)this;

        if (player.getMainHandItem().isEmpty() && !player.getOffhandItem().isEmpty()) {
            if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.OFF_HAND) {
                player.stopUsingItem();
            }

            ServerboundPlayerActionPacket.Action action = fullStack ?
                    ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS :
                    ServerboundPlayerActionPacket.Action.DROP_ITEM;
            player.connection.send(new ServerboundPlayerActionPacket(action, BlockPos.ZERO, Direction.DOWN));

            cir.setReturnValue(true);
        }
    }
}
