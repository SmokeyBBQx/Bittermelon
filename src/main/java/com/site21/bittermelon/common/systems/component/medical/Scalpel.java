package com.site21.bittermelon.common.systems.component.medical;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.client.tool.ScalpelWidget;
import com.site21.bittermelon.common.systems.medical.client.tool.ToolWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record Scalpel(float efficiency, Optional<ResourceLocation> icon) implements MedicalInstrument {
    public static final Codec<Scalpel> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("efficiency").forGetter(Scalpel::efficiency),
                    ResourceLocation.CODEC.optionalFieldOf("icon").forGetter(Scalpel::icon)
            ).apply(instance, Scalpel::new));

    @Contract(pure = true)
    @Override
    public @NotNull Optional<ResourceLocation> icon() {
        return icon;
    }

    @Contract(pure = true)
    @Override
    public @NotNull ToolWidget createWidget(ItemStack stack, int x, int y, int width, int height, HealthScreen screen) {
        return new ScalpelWidget(stack, x, y, width, height, screen);
    }
}
