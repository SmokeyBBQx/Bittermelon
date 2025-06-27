package com.site21.bittermelon.content.medical.compartments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class VisualData {
    public int x;
    public int y;
    public int z;
    public float scale;
    public int width;
    public int height;

    public static final Codec<VisualData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(VisualData::getX),
            Codec.INT.fieldOf("y").forGetter(VisualData::getY),
            Codec.INT.fieldOf("z").forGetter(VisualData::getZ),
            Codec.FLOAT.fieldOf("scale").forGetter(VisualData::getScale),
            Codec.INT.fieldOf("width").forGetter(VisualData::getWidth),
            Codec.INT.fieldOf("height").forGetter(VisualData::getHeight)
    ).apply(instance, VisualData::new));

    public static final StreamCodec<ByteBuf, VisualData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            VisualData::getX,
            ByteBufCodecs.INT,
            VisualData::getY,
            ByteBufCodecs.INT,
            VisualData::getZ,
            ByteBufCodecs.FLOAT,
            VisualData::getScale,
            ByteBufCodecs.INT,
            VisualData::getWidth,
            ByteBufCodecs.INT,
            VisualData::getHeight,
            VisualData::new
    );

    // TODO: Move icon to VisualData

    public VisualData(int x, int y, int z, float scale, int width, int height) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = scale;
        this.width = width;
        this.height = height;
    }

    public VisualData(int x, int y, int z, int width, int height) {
       this(x, y, z, 1, width, height);
    }

    public VisualData(int x, int y, int width, int height) {
        this(x, y, 0, 1, width, height);
    }

    public VisualData(int x, int y, float scale) {
       this(x, y, 0, scale, 0, 0);
    }

    public VisualData(int x, int y) {
        this(x, y, 0, 1, 0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public float getScale() {
        return scale;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
