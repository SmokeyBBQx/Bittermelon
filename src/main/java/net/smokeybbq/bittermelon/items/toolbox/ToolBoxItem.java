package net.smokeybbq.bittermelon.items.toolbox;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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

import static net.smokeybbq.bittermelon.init.SoundInit.TOOLBOX_OPEN;

public class ToolBoxItem extends BaseItem {
    public ToolBoxItem(Properties pProperties) {
        super(pProperties, ItemSize.BULKY, ItemWeight.MEDIUM);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return Component.translatable("item.bittermelon.toolbox");
                }

                @Override
                public AbstractContainerMenu createMenu(int containerID, @NotNull Inventory playerInventory, @NotNull Player player) {
                    return new ToolBoxMenu(containerID, playerInventory, itemStack);
                }
            }, buf -> buf.writeItem(itemStack));
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), TOOLBOX_OPEN.get(), SoundSource.NEUTRAL, 1F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
