package com.site21.bittermelon.common.systems.component.medical;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record Retractor(Optional<Identifier> icon) implements MedicalInstrument {
    public static final Codec<Retractor> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.optionalFieldOf("icon").forGetter(Retractor::icon)
            ).apply(instance, Retractor::new));

    public Retractor() {
        this(Optional.empty());
    }

    @Override
    public Optional<Identifier> icon() {
        return icon;
    }
}
