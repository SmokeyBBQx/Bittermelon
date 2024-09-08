package net.smokeybbq.bittermelon.items.pda;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.smokeybbq.bittermelon.items.base.ItemSize;
import net.smokeybbq.bittermelon.items.base.ItemWeight;
import net.smokeybbq.bittermelon.items.radio.RadioItem;
import net.smokeybbq.bittermelon.items.radio.RadioScreen;
import net.smokeybbq.bittermelon.systems.telecommunications.EncodedMessage;
import net.smokeybbq.bittermelon.systems.telecommunications.TelecommsManager;
import org.jetbrains.annotations.NotNull;

import static net.smokeybbq.bittermelon.init.SoundInit.TOOLBOX_OPEN;

public class PDA extends RadioItem {
    private static final String INVENTORY_KEY = "Inventory";
    private static final int PIM_SLOT = 0;

    public PDA(Properties pProperties, ItemSize itemSize, ItemWeight itemWeight, float minFrequency, float maxFrequency, int transmitRange, boolean twoWay) {
        super(pProperties, itemSize, itemWeight, minFrequency, maxFrequency, transmitRange, twoWay);
    }

    public String getOwnerID(ItemStack stack) {
        ItemStack PIM = getPIMCard(stack);
        if (!PIM.isEmpty()) {
            CompoundTag nbt = PIM.getTag();
            if (nbt != null && nbt.contains(PIMCard.OWNER_ID_KEY)) {
                return nbt.getString(PIMCard.OWNER_ID_KEY);
            }
        }
        return "";
    }

    public ItemStack getPIMCard(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains(INVENTORY_KEY)) {
            CompoundTag inventoryTag = nbt.getCompound(INVENTORY_KEY);
            ItemStackHandler inventory = new ItemStackHandler(1);
            inventory.deserializeNBT(inventoryTag);
            return inventory.getStackInSlot(PIM_SLOT);
        }
        return ItemStack.EMPTY;
    }

    public void scanNetwork(ServerPlayer player, ItemStack stack) {
        EncodedMessage message = new EncodedMessage("", "request_channels", "aa", getOwnerID(stack));
        TelecommsManager.broadcastReceivers(player.getOnPos(), player.level(), Float.parseFloat(getActiveChannel(stack)), getTransmitRange(), message);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (player.isShiftKeyDown()) {
                NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                    @Override
                    public @NotNull Component getDisplayName() {
                        return Component.translatable("item.bittermelon.toolbox");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int containerID, @NotNull Inventory playerInventory, @NotNull Player player) {
                        return null;
                    }
                }, buf -> buf.writeItem(itemStack));

                level.playSound(null, player.getX(), player.getY(), player.getZ(), TOOLBOX_OPEN.get(), SoundSource.NEUTRAL, 1F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            } else {
                Minecraft.getInstance().setScreen(new RadioScreen(player.getItemInHand(hand)));
            }
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
