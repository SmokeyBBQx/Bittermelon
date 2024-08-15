package net.smokeybbq.bittermelon.items.handlabeler;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.smokeybbq.bittermelon.networking.PacketHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.Util;

import static net.smokeybbq.bittermelon.init.MenuInit.HAND_LABELER_MENU;

public class HandLabelerMenu extends ItemCombinerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    public static final int MAX_NAME_LENGTH = 50;


    private String itemName = "";

    public HandLabelerMenu(@Nullable MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(pType, pContainerId, pPlayerInventory, pAccess);
    }

    public HandLabelerMenu(int containerID, Inventory playerInventory) {
        super(HAND_LABELER_MENU.get(), containerID, playerInventory, ContainerLevelAccess.NULL);

    }

    protected boolean mayPickup(@NotNull Player pPlayer, boolean pHasStack) {
        return true;
    }

    @Override
    protected void onTake(Player pPlayer, @NotNull ItemStack pStack) {
        this.inputSlots.setItem(INPUT_SLOT, ItemStack.EMPTY);
        Level level = pPlayer.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOOK_PAGE_TURN, SoundSource.NEUTRAL, 0.5F, 2);
    }

    @Override
    protected boolean isValidBlock(@NotNull BlockState pState) {
        return true;
    }

    public boolean setItemName(String name) {
        String s = validateName(name);
        if (s != null) {
            if (Util.isBlank(s)) {
                this.itemName = "";
            } else {
                this.itemName = s;
            }
            this.createResult();
            return true;
        } else {
            return false;
        }
    }

    public void setItemNameAndSync(String name) {
        if (this.setItemName(name)) {
            PacketHandler.INSTANCE.sendToServer(new SetItemNamePacket(this.itemName));
        }
    }

    @Override
    public void createResult() {
        ItemStack itemStack = this.inputSlots.getItem(INPUT_SLOT);
        if (!itemStack.isEmpty()) {
            ItemStack resultItemStack = itemStack.copy();
            if (!this.itemName.isEmpty()) {
                resultItemStack.setHoverName(Component.literal(itemName));
            } else {
                resultItemStack.resetHoverName();
            }
//            this.getSlot(RESULT_SLOT).set(resultItemStack);
            this.resultSlots.setItem(0, resultItemStack);
        } else {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    @Nullable
    private static String validateName(String pItemName) {
        String s = SharedConstants.filterText(pItemName);
        return s.length() <= MAX_NAME_LENGTH ? s : null;
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(INPUT_SLOT, 51, 47, (itemStack) -> true).withResultSlot(RESULT_SLOT, 109, 47).build();
    }
}
