package com.site21.bittermelon.items.containers.substancecontainers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record SubstanceContainerData(Map<ResourceLocation, Float> substances) {
    public static final Codec<SubstanceContainerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(ResourceLocation.CODEC, Codec.FLOAT).fieldOf("substances").forGetter(SubstanceContainerData::substances)
            ).apply(instance, SubstanceContainerData::new)
    );
    public static final StreamCodec<ByteBuf, SubstanceContainerData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(@NotNull ByteBuf buf, SubstanceContainerData value) {
            FriendlyByteBuf friendlyBuf = new FriendlyByteBuf(buf);
            friendlyBuf.writeMap(value.substances,
                    FriendlyByteBuf::writeResourceLocation,
                    FriendlyByteBuf::writeFloat
            );
        }

        @Override
        public @NotNull SubstanceContainerData decode(@NotNull ByteBuf buf) {
            FriendlyByteBuf friendlyBuf = new FriendlyByteBuf(buf);
            Map<ResourceLocation, Float> substances = friendlyBuf.readMap(
                    FriendlyByteBuf::readResourceLocation,
                    FriendlyByteBuf::readFloat
            );
            return new SubstanceContainerData(substances);
        }
    };

    public SubstanceContainerData() {
        this(new HashMap<>());
    }

    public static SubstanceContainerData empty() {
        return new SubstanceContainerData();
    }

    public float getAmount(ResourceLocation substance) {
        return substances.getOrDefault(substance, 0f);
    }

    public float getTotalAmount() {
        return substances.values().stream().reduce(0f, Float::sum);
    }

    public void setAmount(ResourceLocation substance, float amount) {
        if (amount > 0) {
            substances.put(substance, amount);
        } else {
            substances.remove(substance);
        }
    }

    public void addAmount(ResourceLocation substance, float amount) {
        setAmount(substance, getAmount(substance) + amount);
    }

    public boolean isEmpty() {
        return substances.isEmpty();
    }
}
