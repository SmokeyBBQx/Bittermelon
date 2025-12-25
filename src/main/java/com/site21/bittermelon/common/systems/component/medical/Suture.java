package com.site21.bittermelon.common.systems.component.medical;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.client.tool.InstrumentWidget;
import com.site21.bittermelon.common.systems.medical.client.tool.SutureWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record Suture(float efficiency, Optional<ResourceLocation> icon) implements MedicalInstrument {
    public Suture(float efficiency) {
        this(efficiency, Optional.empty());
    }

    public static final Codec<Suture> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("efficiency").forGetter(Suture::efficiency),
                    ResourceLocation.CODEC.optionalFieldOf("icon").forGetter(Suture::icon)
            ).apply(instance, Suture::new));

    @Contract(pure = true)
    @Override
    public @NotNull Optional<ResourceLocation> icon() {
        return Optional.empty();
    }

    @Contract(pure = true)
    @Override
    public @NotNull InstrumentWidget createWidget(ItemStack stack, int x, int y, int width, int height, HealthScreen screen) {
        return new SutureWidget(stack, x, y, width, height, screen);
    }
}
