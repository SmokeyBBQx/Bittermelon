package com.site21.bittermelon.mixin;

import net.minecraft.client.gui.Gui;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Gui.ContextualInfo.class)
public class ContextualInfoExtender implements IExtensibleEnum {

    @Shadow
    @Final
    private static Gui.ContextualInfo[] $VALUES;

    @Invoker(value="<init>")
    private static Gui.ContextualInfo create(String name, int ordinal) {
        throw new AssertionError();
    }

    static {
        var stressEntry = create("STRESS", $VALUES.length);

        //noinspection ShadowFinalModification
        $VALUES = ArrayUtils.add($VALUES, stressEntry);
    }
}
