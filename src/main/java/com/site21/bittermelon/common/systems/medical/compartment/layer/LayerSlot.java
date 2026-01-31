package com.site21.bittermelon.common.systems.medical.compartment.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LayerSlot {
    public static final Codec<LayerSlot> CODEC;
    public static final StreamCodec<ByteBuf, LayerSlot> STREAM_CODEC;

    private final SlotType type;
    private Point[] pivots;
    private float bloodLevel;

    public LayerSlot(SlotType type, @Nullable Point[] pivots, float bloodLevel) {
        this.type = type;
        this.pivots = pivots;
        this.bloodLevel = bloodLevel;
    }

    public LayerSlot(SlotType type, @NotNull List<PivotData> pivots, float bloodLevel) {
        this(type, new Point[pivots.size()], bloodLevel);
        for (PivotData pd : pivots) {
            this.pivots[pd.depth()] = pd.pivot().orElse(null);
        }
    }

    public LayerSlot(SlotType type, int depth) {
        this(type, new Point[depth], 0f);
    }

    public SlotType getType() {
        return type;
    }

    @Nullable
    public Point getPivot(int depth) {
        return pivots[depth];
    }

    public Point[] getPivots() {
        return pivots;
    }

    public List<PivotData> getSerializablePivots() {
        List<PivotData> list = new ArrayList<>();

        for (int i = 0; i < pivots.length; i++) {
            list.add(new PivotData(i, Optional.ofNullable(pivots[i])));
        }

        return list;
    }

    public void setPivot(int depth, Point pivot) {
        this.pivots[depth] = pivot;
    }

    public void setPivots(Point[] pivots) {
        this.pivots = pivots;
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

    public boolean isOccupied(int depth) {
        return getPivot(depth) != null;
    }

    public record PivotData(int depth, Optional<Point> pivot) {
        public static final Codec<PivotData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("depth").forGetter(PivotData::depth),
                Point.CODEC.optionalFieldOf("pivot").forGetter(PivotData::pivot)
        ).apply(instance, PivotData::new));

        public static final StreamCodec<ByteBuf, PivotData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                PivotData::depth,
                ByteBufCodecs.optional(Point.STREAM_CODEC),
                PivotData::pivot,
                PivotData::new
        );
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SlotType.CODEC.fieldOf("type").forGetter(LayerSlot::getType),
                Codec.list(PivotData.CODEC).fieldOf("pivots").forGetter(LayerSlot::getSerializablePivots),
                Codec.FLOAT.fieldOf("blood_level").forGetter(LayerSlot::getBloodLevel)
        ).apply(instance, LayerSlot::new));

        STREAM_CODEC = StreamCodec.composite(
                SlotType.STREAM_CODEC,
                LayerSlot::getType,
                PivotData.STREAM_CODEC.apply(ByteBufCodecs.list()),
                LayerSlot::getSerializablePivots,
                ByteBufCodecs.FLOAT,
                LayerSlot::getBloodLevel,
                LayerSlot::new
        );
    }
}
