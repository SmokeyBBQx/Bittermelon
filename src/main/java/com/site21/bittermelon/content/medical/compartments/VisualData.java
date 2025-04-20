package com.site21.bittermelon.content.medical.compartments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class VisualData {
    public int x;
    public int y;
    public float scale;

    public static final Codec<VisualData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(VisualData::getX),
            Codec.INT.fieldOf("y").forGetter(VisualData::getY),
            Codec.FLOAT.fieldOf("scale").forGetter(VisualData::getScale)
    ).apply(instance, VisualData::new));

    public static final StreamCodec<ByteBuf, VisualData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            VisualData::getX,
            ByteBufCodecs.INT,
            VisualData::getY,
            ByteBufCodecs.FLOAT,
            VisualData::getScale,
            VisualData::new
    );

    // TODO: Move icon to VisualData

    public VisualData(int x, int y, float scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public VisualData(int x, int y) {
        this(x, y, 1);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getScale() {
        return scale;
    }
}
