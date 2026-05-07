package com.site21.bittermelon.common.systems.component.medical;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
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
}
