package com.site21.bittermelon.common.systems.medical.legacy.compartment.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Point(int x, int y) {
    public static final Codec<Point> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(Point::x),
            Codec.INT.fieldOf("y").forGetter(Point::y)
    ).apply(instance, Point::new));

    public static final Codec<Point> STRING_CODEC = Codec.STRING.xmap(
            s -> {
                String[] parts = s.split(",");
                return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            },
            point -> point.x() + "," + point.y()
    );

    public static final StreamCodec<ByteBuf, Point> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            Point::x,
            ByteBufCodecs.INT,
            Point::y,
            Point::new
    );

    public Point move(int dx, int dy) {
        return new Point(x() + dx, y() + dy);
    }
}
