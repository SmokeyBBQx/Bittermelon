package com.site21.bittermelon.content.items.containers.item.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.base.BaseItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ContainerScreen<T extends ContainerMenu> extends AbstractContainerScreen<T> {
    private static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/container/container_screen.png");
    private static final int SLOT_SIZE = 16;
    private static final int ITEM_RENDER_SIZE = 16;

    public ContainerScreen(T menu, Inventory playerInventory, Component title, ResourceLocation TEXTURE) {
        super(menu, playerInventory, title);
        ContainerScreen.TEXTURE = TEXTURE;
        this.imageHeight = 114 + this.menu.rows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderCustomItems(guiGraphics);

        if (hoveredSlot != null) {
            highlightMultiItemSlots(guiGraphics, hoveredSlot);
        }

        ItemStack carried = this.menu.getCarried();
        if (!carried.isEmpty() && carried.getItem() instanceof BaseItem baseItem) {
            if (hoveredSlot != null) {
                highlightOccupiedSlots(guiGraphics, hoveredSlot, baseItem);
            }
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void highlightMultiItemSlots(GuiGraphics guiGraphics, Slot hoveredSlot) {
        if (!(hoveredSlot.container == menu.inventory)) return;

        int topLeftSlot = menu.findTopLeftSlot(hoveredSlot.getSlotIndex());
        ItemStack itemStack = menu.inventory.getItem(topLeftSlot);

        if (!(itemStack.getItem() instanceof BaseItem)) return;
        if (topLeftSlot == -1) return;

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

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        PoseStack poseStack = pGuiGraphics.pose();
        poseStack.pushPose();

        // Move the rendering to a higher z-index
        poseStack.translate(0, 0, 300.0F);

        if (hoveredSlot == null) return;

        if (hoveredSlot.container == menu.inventory) {
            System.out.println("Hovered slot is menu inventory");
            int topLeftSlot = menu.findTopLeftSlot(hoveredSlot.getSlotIndex());
            if (topLeftSlot != -1) {
                System.out.println("It has a top left slot");
                ItemStack itemstack = menu.inventory.getItem(topLeftSlot);
                System.out.println("ItemStack is " + itemstack.getHoverName());
                pGuiGraphics.renderTooltip(font, getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, pX, pY);
            }
        } else {
            super.renderTooltip(pGuiGraphics, pX, pY);
        }
        poseStack.popPose();
    }

    private void highlightOccupiedSlots(GuiGraphics guiGraphics, Slot hoveredSlot, BaseItem item) {
        int startX = hoveredSlot.x;
        int startY = hoveredSlot.y;
        int endX = startX + (item.getItemWidth() * SLOT_SIZE);
        int endY = startY + (item.getItemHeight() * SLOT_SIZE);

        ItemStack carriedItem = this.menu.getCarried();
        int color = menu.mayPlace(hoveredSlot.getSlotIndex(), carriedItem) ? 0x80FFFFFF : 0x80FF0000;

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

    private void renderCustomItems(GuiGraphics guiGraphics) {
        int x = this.leftPos;
        int y = this.topPos;

        for (Slot slot : this.menu.slots) {
            if (slot.hasItem()) {
                ItemStack itemStack = slot.getItem();
                if (slot.container == menu.inventory && itemStack.getItem() instanceof BaseItem item) {
                    int width = item.getItemWidth();
                    int height = item.getItemHeight();
                    float scale = (float) (width * height) / 4;

                    int itemX = 4 + x + slot.x + (width * SLOT_SIZE) / 2;
                    int itemY = y + slot.y + (height * SLOT_SIZE) / 2;

                    renderScaledItem(guiGraphics, itemStack, itemX, itemY, scale);
                } else {
                    guiGraphics.renderItem(itemStack, x + slot.x, y + slot.y);
                    guiGraphics.renderItemDecorations(this.font, itemStack, x + slot.x, y + slot.y);
                }
            }
        }
    }

    private void renderScaledItem(GuiGraphics guiGraphics, ItemStack itemStack, int x, int y, float scale) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        poseStack.translate(x, y, 100.0F);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-8, -8, 0);

        guiGraphics.renderItem(itemStack, 0, 0);
        guiGraphics.renderItemDecorations(this.font, itemStack, 0, 0);

        poseStack.popPose();
    }

    @Override
    protected void renderFloatingItem(@NotNull GuiGraphics guiGraphics, @NotNull ItemStack itemStack, int mouseX, int mouseY, @NotNull String text) {
        if (hoveredSlot == null) return;

        if (hoveredSlot.container == menu.inventory && itemStack.getItem() instanceof BaseItem item) {
            float scale = (float) (item.getItemWidth() * item.getItemHeight()) / 4;
            String countString = itemStack.getCount() > 1 ? String.valueOf(itemStack.getCount()) : "";

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(mouseX, mouseY, 200.0F);
            poseStack.scale(scale, scale, scale);
            poseStack.translate(-8, -8, 0);

            guiGraphics.renderItem(itemStack, 0, 0);
            guiGraphics.renderItemDecorations(this.font, itemStack, 0, 0, countString);

            poseStack.popPose();
        } else {
            super.renderFloatingItem(guiGraphics, itemStack, mouseX, mouseY, text);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, menu.rows * 18 + 17);
        guiGraphics.blit(TEXTURE, x, y + menu.rows * 18 + 17, 0, 126, imageWidth, 96);

        for (Slot slot : this.menu.slots) {
            if (slot.container == menu.inventory && slot.hasItem() && slot.getItem().getItem() instanceof BaseItem item) {
                int width = item.getItemWidth();
                int height = item.getItemHeight();

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int slotX = x + slot.x + j * (SLOT_SIZE + 2);
                        int slotY = y + slot.y + i * (SLOT_SIZE + 2);
                        guiGraphics.fill(slotX, slotY, slotX + ITEM_RENDER_SIZE, slotY + ITEM_RENDER_SIZE, 0x40FFFFFF);
                    }
                }
            }
        }
    }

    @Override
    protected void renderSlot(@NotNull GuiGraphics guiGraphics, Slot slot) {
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

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
    }
}
