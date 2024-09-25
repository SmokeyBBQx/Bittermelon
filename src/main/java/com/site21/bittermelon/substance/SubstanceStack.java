package com.site21.bittermelon.substance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.Bittermelon;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

import static com.site21.bittermelon.Bittermelon.LOGGER;
import static com.site21.bittermelon.init.ModRegistries.SUBSTANCE_REGISTRY;
import static com.site21.bittermelon.init.ModRegistries.SUBSTANCE_REGISTRY_KEY;

public class SubstanceStack implements MutableDataComponentHolder {
    public static final Codec<Holder<Substance>> SUBSTANCE_NON_EMPTY_CODEC = SUBSTANCE_REGISTRY.holderByNameCodec().validate(DataResult::success);

    public static final Codec<SubstanceStack> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(
                    instance -> instance.group(
                                    SUBSTANCE_NON_EMPTY_CODEC.fieldOf("id").forGetter(SubstanceStack::getSubstanceHolder),
                                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("amount").forGetter(SubstanceStack::getAmount),
                                    DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                                            .forGetter(stack -> stack.components.asPatch()))
                            .apply(instance, SubstanceStack::new)));

    public static final StreamCodec<RegistryFriendlyByteBuf, SubstanceStack> OPTIONAL_STREAM_CODEC = new StreamCodec<>() {
        private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Substance>> SUBSTANCE_STREAM_CODEC = ByteBufCodecs.holderRegistry(SUBSTANCE_REGISTRY_KEY);

        @Override
        public @NotNull SubstanceStack decode(RegistryFriendlyByteBuf buf) {
            float amount = buf.readFloat();
            if (amount <= 0) {
                return SubstanceStack.EMPTY;
            } else {
                Holder<Substance> holder = SUBSTANCE_STREAM_CODEC.decode(buf);
                DataComponentPatch patch = DataComponentPatch.STREAM_CODEC.decode(buf);
                return new SubstanceStack(holder, amount, patch);
            }
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buf, SubstanceStack stack) {
            if (stack.isEmpty()) {
                buf.writeFloat(0);
            } else {
                buf.writeFloat(stack.getAmount());
                SUBSTANCE_STREAM_CODEC.encode(buf, stack.getSubstanceHolder());
                DataComponentPatch.STREAM_CODEC.encode(buf, stack.components.asPatch());
            }
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, SubstanceStack> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull SubstanceStack decode(@NotNull RegistryFriendlyByteBuf buf) {
            SubstanceStack stack = SubstanceStack.OPTIONAL_STREAM_CODEC.decode(buf);
            if (stack.isEmpty()) {
                throw new DecoderException("Empty SubstanceStack not allowed");
            } else {
                return stack;
            }
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buf, SubstanceStack stack) {
            if (stack.isEmpty()) {
                throw new EncoderException("Empty SubstanceStack not allowed");
            } else {
                SubstanceStack.OPTIONAL_STREAM_CODEC.encode(buf, stack);
            }
        }
    };

    public static final SubstanceStack EMPTY = new SubstanceStack(null);
    private final Substance substance;
    private float amount;
    private float temperature;
    private final PatchedDataComponentMap components;


    public SubstanceStack(Substance substance, float amount, PatchedDataComponentMap components) {
        this.substance = substance;
        this.amount = amount;
        this.components = components;
    }

    public SubstanceStack(Substance substance, float amount) {
        this(substance, amount, new PatchedDataComponentMap(substance.components()));
    }

    public SubstanceStack(Holder<Substance> tag, float amount) {
        this(tag.value(), amount);
    }

    public SubstanceStack(Holder<Substance> tag, Float amount, DataComponentPatch components) {
        this(tag.value(), amount, new PatchedDataComponentMap(tag.value().components()));
    }

    private SubstanceStack(@javax.annotation.Nullable Void unused) {
        this.substance = null;
        this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
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
        this.amount = Math.max(0, this.amount + delta);
    }

    public void modifyTemperature(float delta) {
        this.amount = Math.max(0, this.temperature + delta);
    }

    public SubstanceState getState() {
        if (temperature < substance.properties.getFreezingTemperature()) {
            return SubstanceState.SOLID;
        } else if (temperature < substance.properties.getBoilingTemperature()) {
            return SubstanceState.LIQUID;
        } else if (temperature < substance.properties.getPlasmaTemperature()) {
            return SubstanceState.GAS;
        } else {
            return SubstanceState.PLASMA;
        }
    }

    public float getVolume() {
        return amount * substance.getSpecificVolume();
    }

    public boolean canMergeWith(SubstanceStack other) {
        if (this == other) {
            return true;
        } else {
            return Objects.equals(this.components, other.components) && this.substance == other.getSubstance();
        }
    }

    public SubstanceStack copy() {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            return new SubstanceStack(this.getSubstance(), this.getAmount(), this.components.copy());
        }
    }

    public boolean isEmpty() {
        return this.amount < 0;
    }

    @Override
    public <T> @Nullable T set(@NotNull DataComponentType<? super T> component, @Nullable T value) {
        return this.components.set(component, value);
    }

    @Override
    public <T> @Nullable T remove(@NotNull DataComponentType<? extends T> component) {
        return this.components.remove(component);
    }

    @Override
    public void applyComponents(@NotNull DataComponentPatch components) {
        this.components.applyPatch(components);
    }

    @Override
    public void applyComponents(@NotNull DataComponentMap components) {
        this.components.setAll(components);
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return (DataComponentMap) (!this.isEmpty() ? this.components : DataComponentMap.EMPTY);
    }


    public Tag save(HolderLookup.Provider levelRegistryAccess, Tag outputTag) {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty SubstanceStack");
        } else {
            // Neo: Logs extra information about this ItemStack on error
            return net.neoforged.neoforge.common.util.DataComponentUtil.wrapEncodingExceptions(this, CODEC, levelRegistryAccess, outputTag);
        }
    }

    public Tag save(HolderLookup.Provider levelRegistryAccess) {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty SubstanceStack");
        } else {
            // Neo: Logs extra information about this ItemStack on error
            return net.neoforged.neoforge.common.util.DataComponentUtil.wrapEncodingExceptions(this, CODEC, levelRegistryAccess);
        }
    }

    public Tag saveOptional(HolderLookup.Provider levelRegistryAccess) {
        return (Tag) (this.isEmpty() ? new CompoundTag() : this.save(levelRegistryAccess, new CompoundTag()));
    }

    public static Optional<SubstanceStack> parse(HolderLookup.Provider lookupProvider, Tag tag) {
        return CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag)
                .resultOrPartial(error -> LOGGER.error("Tried to load invalid fluid: '{}'", error));
    }

    public static SubstanceStack parseOptional(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        return tag.isEmpty() ? EMPTY : parse(lookupProvider, tag).orElse(EMPTY);
    }

}
