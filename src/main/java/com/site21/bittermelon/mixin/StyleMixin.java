package com.site21.bittermelon.mixin;

import com.site21.bittermelon.common.systems.chat.AlphaContainer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;


@Mixin(Style.class)
public abstract class StyleMixin implements AlphaContainer {
    @Shadow @Final @Nullable
    private TextColor color;
    @Shadow @Final @Nullable
    private Integer shadowColor;
    @Shadow @Final @Nullable
    private Boolean bold;
    @Shadow @Final @Nullable
    private Boolean italic;
    @Shadow @Final @Nullable
    private Boolean underlined;
    @Shadow @Final @Nullable
    private Boolean strikethrough;
    @Shadow @Final @Nullable
    private Boolean obfuscated;
    @Shadow @Final @Nullable
    private ClickEvent clickEvent;
    @Shadow @Final @Nullable
    private HoverEvent hoverEvent;
    @Shadow @Final @Nullable
    private String insertion;
    @Shadow @Final @Nullable
    private Identifier font;

    @Unique
    private float bittermelon$alpha = 1;

    @Override
    public float bittermelon$getAlpha() {
        return bittermelon$alpha;
    }

    @Override
    public void bittermelon$setAlpha(float alpha) {
        this.bittermelon$alpha = alpha;
    }

    @Override
    public Style bittermelon$withAlpha(float alpha) {
        Style newStyle = new Style(
                this.color,
                this.shadowColor,
                this.bold,
                this.italic,
                this.underlined,
                this.strikethrough,
                this.obfuscated,
                this.clickEvent,
                this.hoverEvent,
                this.insertion,
                this.font
        );

        ((AlphaContainer) newStyle).bittermelon$setAlpha(alpha);
        return newStyle;
    }

    @Inject(method = "equals", at = @At("RETURN"), cancellable = true)
    private void bittermelon$equals(Object other, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && other instanceof AlphaContainer otherAlpha) {
            cir.setReturnValue(this.bittermelon$alpha == otherAlpha.bittermelon$getAlpha());
        }
    }

    @Inject(method = "hashCode", at = @At("RETURN"), cancellable = true)
    private void bittermelon$hashCode(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Objects.hash(cir.getReturnValue(), bittermelon$alpha));
    }
}
