package com.site21.bittermelon.common.systems.component.medical;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.client.tool.InstrumentWidget;
import com.site21.bittermelon.common.systems.medical.client.tool.RetractorWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record Retractor(Optional<ResourceLocation> icon) implements MedicalInstrument {
    public static final Codec<Retractor> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("icon").forGetter(Retractor::icon)
            ).apply(instance, Retractor::new));

    public Retractor() {
        this(Optional.empty());
    }

    @Override
    public Optional<ResourceLocation> icon() {
        return icon;
    }

    @Override
    public InstrumentWidget createWidget(ItemStack stack, int x, int y, int width, int height, HealthScreen screen) {
        return new RetractorWidget(stack, x, y, width, height, screen);
    }
}
