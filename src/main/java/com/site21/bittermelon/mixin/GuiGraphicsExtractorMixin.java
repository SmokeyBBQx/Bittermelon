package com.site21.bittermelon.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffectInstance;
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
    @Unique
    private static final int BITTERMELON$MAX_AMPLIFIER = 10;
    @Unique private static final float BITTERMELON$HIDE_THRESHOLD = 0.8f;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "itemDecorations*", at = @At("HEAD"), cancellable = true)
    private void bittermelon$cancelDecorations(Font font, ItemStack itemStack, int x, int y, @Nullable String countText, CallbackInfo ci) {
        if (minecraft.player != null && minecraft.player.hasEffect(BitterMobEffects.AMNESIA)) {
            ci.cancel();
        }
    }

    @WrapMethod(method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V")
    private void bittermelon$fogItem(LivingEntity owner, Level level, ItemStack itemStack, int x, int y, int seed, Operation<Void> original) {
        float fogLevel = bittermelon$fogLevel();

        if (fogLevel <= 0.0f || itemStack.isEmpty()) {
            original.call(owner, level, itemStack, x, y, seed);
            return;
        }

        if (fogLevel < BITTERMELON$HIDE_THRESHOLD) {
            original.call(owner, level, itemStack, x, y, seed);
        }

        bittermelon$drawFog(x, y, fogLevel);
    }

    @Unique
    private float bittermelon$fogLevel() {
        if (minecraft.player == null) return 0.0f;
        MobEffectInstance effect = minecraft.player.getEffect(BitterMobEffects.AMNESIA);
        if (effect == null) return 0.0f;
        return Mth.clamp((effect.getAmplifier() + 1) / (float) BITTERMELON$MAX_AMPLIFIER, 0.0f, 1.0f);
    }

    @Unique
    private void bittermelon$drawFog(int x, int y, float fogLevel) {
        GuiGraphicsExtractor graphics = (GuiGraphicsExtractor) (Object) this;
        int color = ARGB.white(fogLevel);
        float t = (Util.getMillis() % 8000L) / 8000.0f * 64.0f;

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BITTERMELON$FOG,
                x, y,
                t, 0,
                16, 16,
                64, 64,
                color
        );

        if (fogLevel >= BITTERMELON$HIDE_THRESHOLD) {
            graphics.text(minecraft.font, "?", x + 5, y + 4, 0xFFFFFFFF, true);
        }
    }
}
