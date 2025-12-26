package com.site21.bittermelon.common.systems.medical.compartment.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LayerSlot {
    public static final Codec<LayerSlot> CODEC;
    public static final StreamCodec<ByteBuf, LayerSlot> STREAM_CODEC;

    private final SlotType type;
    private Point pivot;
    private float bloodLevel;

    public LayerSlot(SlotType type, @Nullable Point pivot, float bloodLevel) {
        this.type = type;
        this.pivot = pivot;
        this.bloodLevel = bloodLevel;
    }

    public LayerSlot(SlotType type, @NotNull Optional<Point> point, float bloodLevel) {
        this(type, point.orElse(null), bloodLevel);
    }

    public LayerSlot(SlotType type) {
        this(type, Optional.empty(), 0f);
    }

    public SlotType getType() {
        return type;
    }

    @Nullable
    public Point getPivot() {
        return pivot;
    }

    public Optional<Point> getPivotOpt() {
        return Optional.ofNullable(pivot);
    }

    public void setPivot(Point pivot) {
        this.pivot = pivot;
    }

    public float getBloodLevel() {
        return bloodLevel;
    }

    public void setBloodLevel(float bloodLevel) {
        this.bloodLevel = bloodLevel;
    }

    public void updateBloodLevel(float delta) {
        bloodLevel = Math.max(0f, Math.min(1f, bloodLevel + delta));
    }

    public boolean isOccupied() {
        return getPivot() != null;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SlotType.CODEC.fieldOf("type").forGetter(LayerSlot::getType),
                Point.CODEC.optionalFieldOf("pivot").forGetter(LayerSlot::getPivotOpt),
                Codec.FLOAT.fieldOf("blood_level").forGetter(LayerSlot::getBloodLevel)
        ).apply(instance, LayerSlot::new));

        STREAM_CODEC = StreamCodec.composite(
                SlotType.STREAM_CODEC,
                LayerSlot::getType,
                ByteBufCodecs.optional(Point.STREAM_CODEC),
                LayerSlot::getPivotOpt,
                ByteBufCodecs.FLOAT,
                LayerSlot::getBloodLevel,
                LayerSlot::new
        );
    }
}
