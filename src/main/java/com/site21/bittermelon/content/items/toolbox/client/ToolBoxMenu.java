package com.site21.bittermelon.content.items.toolbox.client;

import com.site21.bittermelon.content.items.containers.item.client.ContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterMenus.TOOLBOX_MENU;
import static com.site21.bittermelon.init.BitterSounds.METAL_INVENTORY;
import static com.site21.bittermelon.init.BitterSounds.TOOLBOX_CLOSE;

public class ToolBoxMenu extends ContainerMenu {
    public static final int ROWS = 4;
    public static final int COLUMNS = 7;
    public ToolBoxMenu(int containerID, Inventory playerInventory, ItemStack toolboxItem) {
        super(TOOLBOX_MENU.get(), containerID, playerInventory, ROWS, COLUMNS, toolboxItem);

        init(playerInventory);
    }

    @Override
    public void init(Inventory playerInventory) {
        int i = (this.rows - 4) * 18;

        // Container inventory
        for (int row = 0; row < this.rows; ++row) {
            for (int col = 0; col < columns; ++col) {
                this.addSlot(new Slot(inventory, col + row * columns, 26 + col * 18, 18 + row * 18));
            }
        }

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + i));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 161 + i));
        }
    }

    @Override
    protected void handleCustomItemPlace(int slotID, Player player) {
        super.handleCustomItemPlace(slotID, player);
        player.playSound(METAL_INVENTORY.get(), 1F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    protected void handleCustomItemPick(int slotID, Player player) {
        super.handleCustomItemPick(slotID, player);
        player.playSound(METAL_INVENTORY.get(), 1F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);

        Level level = player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), TOOLBOX_CLOSE.get(), SoundSource.NEUTRAL, 1F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }
}
