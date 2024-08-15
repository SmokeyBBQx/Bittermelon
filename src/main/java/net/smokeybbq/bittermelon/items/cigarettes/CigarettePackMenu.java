package net.smokeybbq.bittermelon.items.cigarettes;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.smokeybbq.bittermelon.items.itemcontainers.ContainerMenu;
import org.jetbrains.annotations.NotNull;

import static net.smokeybbq.bittermelon.init.MenuInit.CIGARETTE_PACK_MENU;

public class CigarettePackMenu extends ContainerMenu {
    public static final int ROWS = 1;
    public static final int COLUMNS = 10;
    public CigarettePackMenu(int containerID, Inventory playerInventory, ItemStack itemStack) {
        super(CIGARETTE_PACK_MENU.get(), containerID, playerInventory, ROWS, COLUMNS, itemStack);

    }

    @Override
    protected void handleCustomItemPlace(int slotID, Player player) {
        super.handleCustomItemPlace(slotID, player);
        player.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1F, 0.8F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    protected void handleCustomItemPick(int slotID, Player player) {
        super.handleCustomItemPick(slotID, player);
        player.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1F, 0.8F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);

        Level level = player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.NEUTRAL, 1F, 0.8F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

}
