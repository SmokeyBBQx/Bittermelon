package com.site21.bittermelon.content.items.toolbox;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.toolbox.client.ToolBoxMenu;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterSounds.TOOLBOX_OPEN;

public class ToolBoxItem extends BaseItem {

    public ToolBoxItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getInventory().getSelected();

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new MenuProvider() {
                                      @Override
                                      public @NotNull Component getDisplayName() {
                                          return Component.literal("Toolbox");
                                      }

                                      @Nullable
                                      @Override
                                      public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
                                          return new ToolBoxMenu(containerId, playerInventory, itemStack);
                                      }
                                  }
            );
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), TOOLBOX_OPEN.get(), SoundSource.NEUTRAL, 1F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
