package com.site21.bittermelon.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.chat.AlphaContainer;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(Style.Serializer.class)
public class StyleSerializerMixin {
    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    remap = false,
                    target = "com/mojang/serialization/codecs/RecordCodecBuilder.mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;"
            )
    )
    private static MapCodec<Style> extendCodec(MapCodec<Style> original) {
        return RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                                original.forGetter(Function.identity()),
                                Codec.FLOAT.optionalFieldOf("alpha", 1.0f).forGetter(style ->
                                        ((AlphaContainer) style).bittermelon$getAlpha())
                        )
                        .apply(instance, (style, alpha) -> {
                            ((AlphaContainer) style).bittermelon$setAlpha(alpha);
                            return style;
                        })
        );
    }
}
