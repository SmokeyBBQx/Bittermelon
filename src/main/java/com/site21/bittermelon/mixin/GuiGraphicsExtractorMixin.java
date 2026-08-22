package com.site21.bittermelon.mixin;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {
    @Unique
    private static final Identifier BITTERMELON$FOG = Bittermelon.identifier("textures/misc/item_amnesia_fog.png");

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "itemDecorations*", at = @At("HEAD"), cancellable = true)
    private void bittermelon$cancelDecorations(Font font, ItemStack itemStack, int x, int y, @Nullable String countText, CallbackInfo ci) {
        if (minecraft.player != null && minecraft.player.hasEffect(BitterMobEffects.AMNESIA)) {
            ci.cancel();
        }
    }

    @Inject(method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V", at = @At("HEAD"), cancellable = true)
    private void bittermelon$renderFog(LivingEntity owner, Level level, ItemStack itemStack, int x, int y, int seed, CallbackInfo ci) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BitterMobEffects.AMNESIA) || itemStack.isEmpty())
            return;

        ci.cancel();
        GuiGraphicsExtractor graphics = (GuiGraphicsExtractor) (Object) this;
        float t = (Util.getMillis() % 8000L) / 8000.0f * 64.0f;
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BITTERMELON$FOG,
                x,
                y,
                t,
                0,
                16,
                16,
                64,
                64,
                0xFFFFFFFF
        );
        graphics.text(minecraft.font, "?", x + 5, y + 4, 0xFFFFFFFF, true);
    }
}
