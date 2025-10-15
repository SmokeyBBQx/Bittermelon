package com.site21.bittermelon.common.systems.substance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SubstanceStack implements DataComponentHolder, MutableDataComponentHolder {
    public static final Codec<SubstanceStack> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, SubstanceStack> STREAM_CODEC;

    public static final SubstanceStack EMPTY = new SubstanceStack(null);
    private final Substance substance;
    private float amount; // in moles
    private float temperature; // in Kelvin
    private final PatchedDataComponentMap components;

    public SubstanceStack(Substance substance, float amount, float temperature, PatchedDataComponentMap components) {
        this.substance = substance;
        this.amount = amount;
        this.temperature = temperature;
        this.components = components;
    }

    public SubstanceStack(Substance substance, float amount, float temperature) {
        this(substance, amount, temperature, new PatchedDataComponentMap(substance.components()));
    }

    public SubstanceStack(@NotNull Holder<Substance> tag, float amount, float temperature) {
        this(tag.value(), amount, temperature);
    }

    public SubstanceStack(@NotNull Holder<Substance> tag, float amount, float temperature, DataComponentPatch components) {
        this(tag.value(), amount, temperature, PatchedDataComponentMap.fromPatch(tag.value().components(), components));
    }

    public SubstanceStack(@Nullable Void unused) {
        substance = null;
        components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
    }

    public Substance getSubstance() {
        return substance;
    }

    public Holder<Substance> getSubstanceHolder() {
        return this.getSubstance().builtInRegistryHolder();
    }

    public float getAmount() {
        return amount;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void modifyAmount(float delta) {
        amount = Math.max(0, amount + delta);
    }

    public void modifyTemperature(float delta) {
        temperature = Math.max(0, temperature + delta);
    }

    public float getVolume() {
        return amount * substance.getMolarVolume();
    }

    public void setVolume(float volume) {
        amount = volume / substance.getMolarVolume();
    }

    public void modifyVolume(float delta) {
        setVolume(Math.max(0, getVolume() + delta));
    }

    public boolean isEmpty() {
        return amount <= 0;
    }

    public SubstanceStack copy() {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            return new SubstanceStack(getSubstance(), getAmount(), getTemperature(), components.copy());
        }
    }

    public boolean canMergeWith(SubstanceStack other) {
        if (this == other) {
            return true;
        } else {
            return Objects.equals(components, other.components) && substance == other.getSubstance();
        }
    }

    public static boolean matches(SubstanceStack stack, SubstanceStack other) {
        if (stack == other) {
            return true;
        } else {
            return stack.amount == other.amount
                    && stack.temperature == other.temperature
                    && isSameSubstanceSameComponents(stack, other);
        }
    }

    public static boolean isSameSubstanceSameComponents(SubstanceStack stack, SubstanceStack other) {
        if (stack == other) {
            return true;
        } else {
            return Objects.equals(stack.components, other.components) && stack.substance == other.substance;
        }
    }

    public static boolean listMatches(@NotNull List<SubstanceStack> list, @NotNull List<SubstanceStack> other) {
        if (list.size() != other.size()) {
            return false;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (!matches(list.get(i), other.get(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static int hashStackList(@NotNull List<SubstanceStack> list) {
        int i = 0;

        for (SubstanceStack stack : list) {
            i = i * 31 + hashSubstanceAndComponents(stack);
        }

        return i;
    }

    public static int hashSubstanceAndComponents(@javax.annotation.Nullable SubstanceStack stack) {
        if (stack != null) {
            int i = 31 + stack.getSubstance().hashCode();
            return 31 * i + stack.getComponents().hashCode();
        } else {
            return 0;
        }
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
        return isEmpty() ? DataComponentMap.EMPTY : components;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Substance.CODEC.fieldOf("substance").forGetter(SubstanceStack::getSubstanceHolder),
                ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("amount").forGetter(SubstanceStack::getAmount),
                ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("temperature").forGetter(SubstanceStack::getTemperature),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(stack -> stack.components.asPatch())
        ).apply(instance, SubstanceStack::new));

        STREAM_CODEC = StreamCodec.composite(
                Substance.STREAM_CODEC,
                SubstanceStack::getSubstanceHolder,
                ByteBufCodecs.FLOAT,
                SubstanceStack::getAmount,
                ByteBufCodecs.FLOAT,
                SubstanceStack::getTemperature,
                DataComponentPatch.STREAM_CODEC,
                stack -> stack.components.asPatch(),
                SubstanceStack::new
        );
    }
}
