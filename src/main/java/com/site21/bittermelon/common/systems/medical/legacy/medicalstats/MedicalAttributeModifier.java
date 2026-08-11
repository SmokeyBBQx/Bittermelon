package com.site21.bittermelon.common.systems.medical.legacy.medicalstats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MedicalAttributeModifier {
    public static final Codec<MedicalAttributeModifier> CODEC;
    public static final StreamCodec<ByteBuf, MedicalAttributeModifier> STREAM_CODEC;

    private final Operation operation;
    private float modifier;

    public MedicalAttributeModifier(Operation operation, float modifier) {
        this.operation = operation;
        this.modifier = modifier;
    }

    public float getModifier() {
        return modifier;
    }

    public void setModifier(float modifier) {
        this.modifier = modifier;
    }

    public Operation getOperation() {
        return operation;
    }

    public enum Operation implements StringRepresentable {
        AVERAGE,
        MULTIPLIER;

        @Contract(pure = true)
        @Override
        public @NotNull String getSerializedName() {
            return name();
        }

        public static final EnumCodec<Operation> CODEC = StringRepresentable.fromEnum(Operation::values);

        public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC = ByteBufCodecs.idMapper(
                i -> Operation.values()[i],
                Operation::ordinal
        );
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Operation.CODEC.fieldOf("operation").forGetter(MedicalAttributeModifier::getOperation),
                        Codec.FLOAT.fieldOf("modifier").forGetter(MedicalAttributeModifier::getModifier)
                ).apply(instance, MedicalAttributeModifier::new)
        );

        STREAM_CODEC = StreamCodec.composite(
                Operation.STREAM_CODEC, MedicalAttributeModifier::getOperation,
                ByteBufCodecs.FLOAT, MedicalAttributeModifier::getModifier,
                MedicalAttributeModifier::new
        );
    }
}
