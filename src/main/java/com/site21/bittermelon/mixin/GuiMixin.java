package com.site21.bittermelon.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    protected abstract void renderSlot(GuiGraphics guiGraphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack stack, int seed);

    @Shadow
    @Final
    private static ResourceLocation HOTBAR_SPRITE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/hotbar.png");

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void renderCustomHotbar(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        net.minecraft.client.player.LocalPlayer player = minecraft.player;
        if (player == null) return;

        Inventory inventory = player.getInventory();
        ItemStack mainHandItem = inventory.items.get(inventory.selected);
        ItemStack offHandItem = player.getOffhandItem();

        int centerX = graphics.guiWidth() / 2;
        int y = graphics.guiHeight() - 22;

        RenderSystem.enableBlend();
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, -90.0F);

//        ResourceLocation SLOT_SPRITE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/hotbar_hands.png");
//graphics.blit(SLOT_SPRITE, centerX - 41 / 2, y, 0, 0, 0, 41, 22, 41, 22);

        graphics.blit(HOTBAR_SPRITE, centerX - 247 / 2, y, 0, 0, 0, 247, 22, 247, 22);


        graphics.pose().popPose();

        this.renderSlot(graphics, centerX - 17, y + 3, deltaTracker, player, offHandItem, 40);
        this.renderSlot(graphics, centerX + 2, y + 3, deltaTracker, player, mainHandItem, inventory.selected + 1);

        ci.cancel();
    }
}
