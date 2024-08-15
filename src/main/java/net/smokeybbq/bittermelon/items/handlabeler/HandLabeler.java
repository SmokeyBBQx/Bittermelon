package net.smokeybbq.bittermelon.items.handlabeler;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.smokeybbq.bittermelon.items.base.BaseItem;
import net.smokeybbq.bittermelon.items.base.ItemSize;
import net.smokeybbq.bittermelon.items.base.ItemWeight;
import org.jetbrains.annotations.NotNull;

public class HandLabeler extends BaseItem {
    public HandLabeler(Properties pProperties) {
        super(pProperties);
        itemSize = ItemSize.NORMAL;
        itemWeight = ItemWeight.MEDIUM;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return Component.translatable("container.hand_labeler");
                }

                @Override
                public @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
                    return new HandLabelerMenu(pContainerId, pPlayerInventory);
                }
            });
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
