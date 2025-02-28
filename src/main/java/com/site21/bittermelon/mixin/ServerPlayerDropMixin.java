package com.site21.bittermelon.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerDropMixin {
    @Inject(method = "drop*", at = @At("HEAD"), cancellable = true)
    private void onServerPlayerDrop(boolean dropStack, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        Inventory inventory = player.getInventory();

        if (inventory.getSelected().isEmpty() && !player.getOffhandItem().isEmpty()) {
            ItemStack offhandItem = player.getOffhandItem();

            if (offhandItem.onDroppedByPlayer(player)) {
                if (player.isUsingItem() && player.getUsedItemHand() == InteractionHand.OFF_HAND &&
                        (dropStack || offhandItem.getCount() == 1)) {
                    player.stopUsingItem();
                }

                ItemStack itemToDrop;
                if (dropStack) {
                    itemToDrop = offhandItem;
                    inventory.removeItem(offhandItem);
                } else {
                    itemToDrop = inventory.removeItem(40, 1);
                }

                player.containerMenu.broadcastChanges();

                ItemEntity droppedItem = CommonHooks.onPlayerTossEvent(player, itemToDrop, true);
                cir.setReturnValue(droppedItem != null);
            } else {
                cir.setReturnValue(false);
            }
        }
    }
}
