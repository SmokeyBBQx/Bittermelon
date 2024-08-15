package net.smokeybbq.bittermelon.items.itemcontainers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.smokeybbq.bittermelon.items.base.BaseItem;

import java.util.Map;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ContainerScreen<T extends ContainerMenu> extends AbstractContainerScreenDuplicate<T> {
    private static ResourceLocation TEXTURE = new ResourceLocation("bittermelon:textures/gui/container/container_screen.png");
    private static final int SLOT_SIZE = 16;
    private static final int ITEM_RENDER_SIZE = 16;

    public ContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, ResourceLocation TEXTURE) {
        super(pMenu, pPlayerInventory, pTitle);
        ContainerScreen.TEXTURE = TEXTURE;
        this.imageHeight = 114 + menu.rows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderCustomItems(guiGraphics);

        if (hoveredSlot != null) {
            highlightMultiItemSlots(guiGraphics, hoveredSlot);
        }

        ItemStack carried = this.menu.getCarried();
        if (!carried.isEmpty() && carried.getItem() instanceof BaseItem baseItem) {
            int itemSize = baseItem.getItemSize().value;
            if (hoveredSlot != null) {
                highlightOccupiedSlots(guiGraphics, hoveredSlot, itemSize);
            }
            renderScaledFloatingItem(guiGraphics, carried, mouseX, mouseY, itemSize);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }


    private void highlightMultiItemSlots(GuiGraphics guiGraphics, Slot hoveredSlot) {
        if (hoveredSlot.container == menu.inventory) {
            int topLeftSlot = menu.findTopLeftSlot(hoveredSlot.getSlotIndex());
            ItemStack itemStack = menu.inventory.getItem(topLeftSlot);
            if (itemStack.getItem() instanceof BaseItem) {
                if (topLeftSlot != -1) {
                    for (Map.Entry<Integer, Set<Integer>> entry : menu.getTranslations().entrySet()) {
                        Set<Integer> occupiedSlots = entry.getValue();
                        if (occupiedSlots.contains(topLeftSlot)) {
                            for (int i : occupiedSlots) {
                                Slot slot = menu.getSlot(i);
                                int slotScreenX = this.leftPos + slot.x;
                                int slotScreenY = this.topPos + slot.y;
                                guiGraphics.fill(slotScreenX, slotScreenY, slotScreenX + SLOT_SIZE, slotScreenY + SLOT_SIZE, 0x80FFFFFF);
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        PoseStack poseStack = pGuiGraphics.pose();
        poseStack.pushPose();

        // Move the rendering to a higher z-index
        poseStack.translate(0, 0, 300.0F);

        if (hoveredSlot != null) {
            if (hoveredSlot.container == menu.inventory) {
                int topLeftSlot = menu.findTopLeftSlot(hoveredSlot.getSlotIndex());
                if (topLeftSlot != -1) {
                    ItemStack itemstack = menu.inventory.getItem(topLeftSlot);
                    pGuiGraphics.renderTooltip(font, getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, pX, pY);
                }
            } else {
                super.renderTooltip(pGuiGraphics, pX, pY);
            }
        }
        poseStack.popPose();
    }


    private void highlightOccupiedSlots(GuiGraphics guiGraphics, Slot hoveredSlot, int itemSize) {
        int startX = hoveredSlot.x;
        int startY = hoveredSlot.y;
        int endX = Math.min(startX + itemSize * SLOT_SIZE, this.leftPos + this.imageWidth);
        int endY = Math.min(startY + itemSize * SLOT_SIZE, this.topPos + this.imageHeight);

        ItemStack carriedItem = this.menu.getCarried();
        int color;

        if (menu.mayPlace(hoveredSlot.getSlotIndex(), carriedItem)) {
            color = 0x80FFFFFF;
        } else {
            color = 0x80FF0000;
        }

        for (Slot slot : this.menu.slots) {
            if (slot.x >= startX && slot.x < endX && slot.y >= startY && slot.y < endY) {
                if (slot.container == menu.inventory) {
                    int slotScreenX = this.leftPos + slot.x;
                    int slotScreenY = this.topPos + slot.y;
                    guiGraphics.fill(slotScreenX, slotScreenY, slotScreenX + SLOT_SIZE, slotScreenY + SLOT_SIZE, color);
                }
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, menu.rows * 18 + 17);
        guiGraphics.blit(TEXTURE, x, y + menu.rows * 18 + 17, 0, 126, imageWidth, 96);

        // Render slot backgrounds for BaseItems
        for (Slot slot : this.menu.slots) {
            if (slot.container == menu.inventory) {
                if (slot.hasItem() && slot.getItem().getItem() instanceof BaseItem item) {
                    int size = Math.min(item.getItemSize().value, Math.min(menu.rows, menu.columns));
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            int slotX = x + slot.x + j * (SLOT_SIZE + 2);
                            int slotY = y + slot.y + i * (SLOT_SIZE + 2);
                            guiGraphics.fill(slotX, slotY, slotX + ITEM_RENDER_SIZE, slotY + ITEM_RENDER_SIZE, 0x40FFFFFF);
                        }
                    }
                }
            }
        }
    }

    private void renderCustomItems(GuiGraphics guiGraphics) {
        int x = this.leftPos;
        int y = this.topPos;

        for (Slot slot : this.menu.slots) {
            if (slot.hasItem()) {
                ItemStack itemStack = slot.getItem();
                if (slot.container == menu.inventory && itemStack.getItem() instanceof BaseItem item) {
                    int size = Math.min(item.getItemSize().value, Math.min(menu.rows, menu.columns));
                    float totalSize = Math.max(1, size);
                    int itemX = x + slot.x + (size * SLOT_SIZE) / 2;
                    int itemY = y + slot.y + (size * SLOT_SIZE) / 2;
                    renderScaledItem(guiGraphics, itemStack, itemX, itemY, totalSize);
                } else {
                    // Render non-BaseItems or items not in the custom inventory normally
                    guiGraphics.renderItem(itemStack, x + slot.x, y + slot.y);
                    guiGraphics.renderItemDecorations(this.font, itemStack, x + slot.x, y + slot.y);
                }
            }
        }
    }

    private void renderScaledItem(GuiGraphics guiGraphics, ItemStack itemStack, int x, int y, float size) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 100.0F);
        poseStack.scale(size, size, size);
        poseStack.translate(-8, -8, 0);

        guiGraphics.renderItem(itemStack, 0, 0);
        guiGraphics.renderItemDecorations(this.font, itemStack, 0, 0);

        poseStack.popPose();
    }

    private void renderScaledFloatingItem(GuiGraphics guiGraphics, ItemStack stack, int mouseX, int mouseY, int itemSize) {
        if (hoveredSlot != null) {
            if (hoveredSlot.container == menu.inventory) {
                float scale = itemSize * 0.75f;
                String countString = stack.getCount() > 1 ? String.valueOf(stack.getCount()) : "";

                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();
                poseStack.translate(mouseX, mouseY, 200.0F);
                poseStack.scale(scale, scale, scale);
                poseStack.translate(-8, -8, 0); // Center the item

                guiGraphics.renderItem(stack, 0, 0);
                guiGraphics.renderItemDecorations(this.font, stack, 0, 0, countString);

                poseStack.popPose();
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
//        super.renderLabels(guiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderSlot(GuiGraphics guiGraphics, Slot slot) {
        int i = slot.x;
        int j = slot.y;
        ItemStack itemstack = slot.getItem();
        boolean flag = false;
        boolean flag1 = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isDragging();
        ItemStack itemstack1 = this.menu.getCarried();
        String s = null;

        if (slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isDragging() && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        } else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !itemstack1.isEmpty()) {
            if (this.quickCraftSlots.size() == 1) {
                return;
            }

            if (ContainerMenu.canItemQuickReplace(slot, itemstack1, true) && this.menu.canDragTo(slot)) {
                itemstack = itemstack1.copy();
                flag = true;
                int k = Math.min(itemstack.getMaxStackSize(), slot.getMaxStackSize(itemstack));
                if (itemstack.getCount() > k) {
                    s = ChatFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            } else {
                this.quickCraftSlots.remove(slot);
                this.recalculateQuickCraftRemaining();
            }
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

        if (itemstack.getItem() instanceof BaseItem baseItem) {

        } else {
            // Default rendering for non-BaseItems
            if (itemstack.isEmpty() && slot.isActive()) {
                Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
                if (pair != null) {
                    TextureAtlasSprite textureatlassprite = this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
                    guiGraphics.blit(i, j, 0, 16, 16, textureatlassprite);
                    flag1 = true;
                }
            }

            if (!flag1) {
                if (flag) {
                    guiGraphics.fill(i, j, i + 16, j + 16, -2130706433);
                }

                guiGraphics.renderItem(itemstack, i, j);
                guiGraphics.renderItemDecorations(this.font, itemstack, i, j, s);
            }
        }

        guiGraphics.pose().popPose();
    }
}