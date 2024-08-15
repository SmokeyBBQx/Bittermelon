package net.smokeybbq.bittermelon.items.itemcontainers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.smokeybbq.bittermelon.items.base.BaseItem;
import net.smokeybbq.bittermelon.util.ModLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ContainerMenu extends AbstractContainerMenu {
    public SimpleContainer inventory;
    public int rows;
    public int columns;
    private Map<Integer, Set<Integer>> translations;
    private ItemStack containerItem;

    protected ContainerMenu(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    public ContainerMenu(@Nullable MenuType<?> pMenuType, int containerID, Inventory playerInventory, int rows, int columns, ItemStack itemStack) {
        super(pMenuType, containerID);
        this.inventory = new SimpleContainer(rows * columns);
        this.rows = rows;
        this.columns = columns;
        this.translations = new HashMap<>();
        this.containerItem = itemStack;
        if (containerItem.getItem() instanceof BaseItem) {
            load(BaseItem.getTag(containerItem, "Inventory"));
        }

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
                int size = item.getItemSize().value;
                Set<Integer> newOccupiedSlots = new HashSet<>();

                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        int slotIndex = slotID + i * columns + j;
                        newOccupiedSlots.add(slotIndex);
                        inventory.setItem(slotID, ItemStack.EMPTY);
                        ModLogger.info("Placed item at" + slotIndex);
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
                int size = item.getItemSize().value;
                Set<Integer> newOccupiedSlots = new HashSet<>();

                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        int slotIndex = slotID + i * columns + j;
                        newOccupiedSlots.add(slotIndex);
                        inventory.setItem(slotID, ItemStack.EMPTY);
                        ModLogger.info("Placed item at" + slotIndex);
                    }
                }

                inventory.setItem(slotID, itemStack.copy());
                translations.put(slotID, newOccupiedSlots);
            }
        }
    }

    public boolean mayPlace(int slotID, ItemStack itemStack) {
        if (ItemStack.matches(itemStack, this.containerItem)) {
            return false; // Prevent player from putting the container item into itself (Doesn't work for some reason)
        }

        if (!(itemStack.getItem() instanceof BaseItem baseItem)) {
            return false;
        }

        int size = baseItem.getItemSize().value;
        int row = slotID / columns;
        int col = slotID % columns;

        if (row + size > rows || col + size > columns) {
            return false;
        }

        Set<Integer> occupiedSlots = new HashSet<>();
        for (Set<Integer> translatedSlots : translations.values()) {
            occupiedSlots.addAll(translatedSlots);
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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
        return -1;
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slot) {
        return slot.mayPlace(stack);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int index) {
        // Needs to be implemented
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
        if (containerItem.getItem() instanceof BaseItem) {
            BaseItem.saveTag(containerItem, "Inventory", save());
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        // Save inventory items
        ListTag itemList = new ListTag();
        for (Map.Entry<Integer, Set<Integer>> entry : translations.entrySet()) {
            ItemStack stack = inventory.getItem(entry.getKey());
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", entry.getKey());
                stack.save(itemTag);
                itemList.add(itemTag);
            }
        }
        tag.put("Items", itemList);

        // Save translations
        ListTag translationsList = new ListTag();
        for (Map.Entry<Integer, Set<Integer>> entry : translations.entrySet()) {
            CompoundTag translationTag = new CompoundTag();
            translationTag.putInt("TopLeft", entry.getKey());
            translationTag.put("Slots", new IntArrayTag(entry.getValue().stream().mapToInt(Integer::intValue).toArray()));
            translationsList.add(translationTag);
        }
        tag.put("Translations", translationsList);

        return tag;
    }

    public void load(CompoundTag tag) {
        // Clear existing data
        inventory.clearContent();
        translations.clear();

        // Load inventory items
        ListTag itemList = tag.getList("Items", 10);
        for (int i = 0; i < itemList.size(); i++) {
            CompoundTag itemTag = itemList.getCompound(i);
            int slot = itemTag.getInt("Slot");
            ItemStack itemStack = ItemStack.of(itemTag);
            handleCustomItemPlace(slot, itemStack);
        }

        // Load translations
        ListTag translationsList = tag.getList("Translations", 10);
        for (int i = 0; i < translationsList.size(); i++) {
            CompoundTag translationTag = translationsList.getCompound(i);
            int topLeft = translationTag.getInt("TopLeft");
            int[] slotsArray = translationTag.getIntArray("Slots");
            Set<Integer> slots = Arrays.stream(slotsArray).boxed().collect(Collectors.toSet());
            translations.put(topLeft, slots);
        }
    }
}