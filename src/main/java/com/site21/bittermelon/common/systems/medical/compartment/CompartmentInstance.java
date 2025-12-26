package com.site21.bittermelon.common.systems.medical.compartment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CompartmentInstance implements DataComponentHolder, MutableDataComponentHolder {
    public static final Codec<CompartmentInstance> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, CompartmentInstance> STREAM_CODEC;

    private final Compartment compartment;
    private final UUID id;
    private final String name;
    private final PatchedDataComponentMap components;

    public CompartmentInstance(@NotNull Compartment compartment, UUID id, String name, PatchedDataComponentMap components) {
        this.compartment = compartment;
        this.id = id;
        this.name = name;
        this.components = components;
    }

    public CompartmentInstance(@NotNull Holder<Compartment> compartment, UUID id, String name, DataComponentPatch components) {
        this(compartment.value(), id, name, PatchedDataComponentMap.fromPatch(compartment.value().components(), components));
    }

    public void tick(MedicalStats medicalStats) {
        compartment.tick(medicalStats, this);
    }

    public Compartment getCompartment() {
        return compartment;
    }

    public Holder<Compartment> getCompartmentHolder() {
        return compartment.builtInRegistryHolder();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean tryToInsert(CompartmentInstance instance, int layerIndex, int x, int y) {
        return CompartmentUtil.insertCompartment(this, instance, layerIndex, x, y);
    }

    @Override
    public <T> @Nullable T set(@NotNull DataComponentType<T> component, @Nullable T value) {
        return components.set(component, value);
    }

    @Override
    public <T> @Nullable T remove(@NotNull DataComponentType<? extends T> component) {
        return components.remove(component);
    }

    @Override
    public void applyComponents(@NotNull DataComponentPatch patch) {
        components.applyPatch(patch);
    }

    @Override
    public void applyComponents(@NotNull DataComponentMap components) {
        this.components.setAll(this.components);
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return components;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Compartment.CODEC.fieldOf("compartment").forGetter(CompartmentInstance::getCompartmentHolder),
                UUIDUtil.CODEC.fieldOf("id").forGetter(CompartmentInstance::getId),
                Codec.STRING.fieldOf("name").forGetter(CompartmentInstance::getName),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                        .forGetter(ci -> ci.components.asPatch())
        ).apply(instance, CompartmentInstance::new));

        STREAM_CODEC = StreamCodec.composite(
                Compartment.STREAM_CODEC,
                CompartmentInstance::getCompartmentHolder,
                UUIDUtil.STREAM_CODEC,
                CompartmentInstance::getId,
                ByteBufCodecs.STRING_UTF8,
                CompartmentInstance::getName,
                DataComponentPatch.STREAM_CODEC,
                compartmentInstance -> compartmentInstance.components.asPatch(),
                CompartmentInstance::new
        );
    }
}
