package com.site21.bittermelon.common.systems.component.medical;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record Scalpel(float efficiency, Optional<ResourceLocation> icon) implements MedicalInstrument {
    public Scalpel(float efficiency) {
        this(efficiency, Optional.empty());
    }

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
}
