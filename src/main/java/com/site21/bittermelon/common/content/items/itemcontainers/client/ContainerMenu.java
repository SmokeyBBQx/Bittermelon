package com.site21.bittermelon.common.content.items.itemcontainers.client;

import com.site21.bittermelon.common.content.items.base.BaseItem;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.ITEM_CONTAINER_CONTENTS;

public class ContainerMenu extends AbstractContainerMenu {
    public SimpleContainer inventory;
    public int rows;
    public int columns;
    private Map<Integer, Set<Integer>> translations;
    private ItemStack containerItem;

    protected ContainerMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    public ContainerMenu(@Nullable MenuType<?> pMenuType, int containerID, Inventory playerInventory, int rows, int columns, ItemStack itemStack) {
        super(pMenuType, containerID);
        this.inventory = new SimpleContainer(rows * columns);
        this.rows = rows;
        this.columns = columns;
        this.translations = new HashMap<>();
        this.containerItem = itemStack;

        loadData(containerItem);
        init(playerInventory);
    }

    protected void init(Inventory playerInventory) {
        int i = (this.rows - 4) * 18;

        for (int j = 0; j < this.rows; ++j) {
            for (int k = 0; k < columns; ++k) {
                this.addSlot(new Slot(inventory, k + j * columns, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    public ItemStack getContainerItem() {
        return containerItem;
    }

    @Override
    public void clicked(int slotID, int button, @NotNull ClickType clickType, @NotNull Player player) {
        if (slotID < (rows * columns)) {
            if (slotID >= 0 && slotID < this.slots.size()) {
                Slot clickedSlot = this.slots.get(slotID);

                if (player.containerMenu.getCarried().isEmpty()) {
                    handleCustomItemPick(clickedSlot.getSlotIndex(), player);
                } else if (player.containerMenu.getCarried().getItem() instanceof BaseItem) {
                    handleCustomItemPlace(clickedSlot.getSlotIndex(), player);
                } else {
                    super.clicked(slotID, button, clickType, player);
                }
            }
        } else {
            super.clicked(slotID, button, clickType, player);
        }
    }

    protected void handleCustomItemPick(int slotID, Player player) {
        Map<Integer, Set<Integer>> translationsCopy = new HashMap<>(translations);
        for (Map.Entry<Integer, Set<Integer>> entry : translationsCopy.entrySet()) {
            int topLeftSlot = entry.getKey();
            Set<Integer> occupiedSlots = entry.getValue();

            if (occupiedSlots.contains(slotID)) {
                ItemStack itemStack = inventory.getItem(topLeftSlot);
                if (!itemStack.isEmpty()) {
                    player.containerMenu.setCarried(itemStack.copy());

                    inventory.setItem(topLeftSlot, ItemStack.EMPTY);

                    translations.remove(topLeftSlot);

                    for (int slot : occupiedSlots) {
                        this.setRemoteSlot(slot, ItemStack.EMPTY);
                    }
                }
                break;
            }
        }
    }

    protected void handleCustomItemPlace(int slotID, Player player) {
        ItemStack carriedItem = player.containerMenu.getCarried();
        if (mayPlace(slotID, carriedItem)) {
            if (carriedItem.getItem() instanceof BaseItem item) {
                int width = item.getItemWidth();
                int height = item.getItemHeight();
                Set<Integer> newOccupiedSlots = new HashSet<>();

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int slotIndex = slotID + i * columns + j;
                        newOccupiedSlots.add(slotIndex);
                        inventory.setItem(slotID, ItemStack.EMPTY);
                    }
                }

                inventory.setItem(slotID, carriedItem.copy());
                translations.put(slotID, newOccupiedSlots);
                player.containerMenu.setCarried(ItemStack.EMPTY);
            }
        }
    }

    protected void handleCustomItemPlace(int slotID, ItemStack itemStack) {
        if (mayPlace(slotID, itemStack)) {
            if (itemStack.getItem() instanceof BaseItem item) {
                int width = item.getItemWidth();
                int height = item.getItemHeight();
                Set<Integer> newOccupiedSlots = new HashSet<>();

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int slotIndex = slotID + i * columns + j;
                        newOccupiedSlots.add(slotIndex);
                        inventory.setItem(slotID, ItemStack.EMPTY);
                    }
                }

                inventory.setItem(slotID, itemStack.copy());
                translations.put(slotID, newOccupiedSlots);
            }
        }
    }

    public boolean mayPlace(int slotID, ItemStack itemStack) {
        if (ItemStack.matches(itemStack, this.containerItem)) {
            return false;
        }

        if (!(itemStack.getItem() instanceof BaseItem baseItem)) {
            return false;
        }

        int width = baseItem.getItemWidth();
        int height = baseItem.getItemHeight();
        int row = slotID / columns;
        int col = slotID % columns;

        // Check if item fits within container boundaries
        if (row + height > rows || col + width > columns) {
            return false;
        }

        // Check if any slots are already occupied
        Set<Integer> occupiedSlots = new HashSet<>();
        for (Set<Integer> translatedSlots : translations.values()) {
            occupiedSlots.addAll(translatedSlots);
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int slotIndex = slotID + i * columns + j;
                if (!inventory.getItem(slotIndex).isEmpty() || occupiedSlots.contains(slotIndex)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int findTopLeftSlot(int slotID) {
        for (Map.Entry<Integer, Set<Integer>> entry : translations.entrySet()) {
            if (entry.getValue().contains(slotID)) {
                return entry.getKey();
            }
        }
        System.out.println(slotID + " does not contain top left slot");
        return -1;
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slot) {
        return slot.mayPlace(stack);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        if (pPlayer.getMainHandItem() == this.containerItem || pPlayer.getOffhandItem() == this.containerItem) {
            return true;
        }

        return pPlayer.getInventory().contains(this.containerItem);
    }

    public Map<Integer, Set<Integer>> getTranslations() {
        return translations;
    }

    @Override
    public void removed(@NotNull Player pPlayer) {
        super.removed(pPlayer);
        saveData(containerItem);
    }

    private void saveData(ItemStack stack) {
        List<SlotData> data = new ArrayList<>();

        for (Map.Entry<Integer, Set<Integer>> entry : translations.entrySet()) {
            int topLeftSlotIndex = entry.getKey();
            List<Integer> slots = entry.getValue().stream().toList();
            data.add(new SlotData(topLeftSlotIndex, slots, inventory.getItem(topLeftSlotIndex)));
        }

        stack.set(ITEM_CONTAINER_CONTENTS.get(), new ItemContainerContents(data));
    }

    public void loadData(ItemStack stack) {
        inventory.clearContent();
        translations.clear();

        ItemContainerContents contents = stack.getOrDefault(ITEM_CONTAINER_CONTENTS.get(), ItemContainerContents.EMPTY);
        for (SlotData slot : contents.items()) {
            System.out.println("Loading slot: " + slot.toString());
            handleCustomItemPlace(slot.topLeftSlot(), slot.stack());
            translations.put(slot.topLeftSlot(), new HashSet<>(slot.slots()));
        }

        broadcastChanges();
    }
}
