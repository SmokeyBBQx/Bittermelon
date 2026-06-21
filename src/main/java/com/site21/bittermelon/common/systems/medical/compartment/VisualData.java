package com.site21.bittermelon.common.systems.medical.compartment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record VisualData(
        int x,
        int y,
        int width,
        int height,
        float scale,
        @Nullable Identifier icon,
        int color
) {
    public static final Codec<VisualData> CODEC;
    public static final StreamCodec<ByteBuf, VisualData> STREAM_CODEC;
    public static final int DEFAULT_COLOR = 0xFFFFFFFF;

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
    @Contract(pure = true)
    public VisualData(int x, int y, int width, int height, float scale, @NotNull Optional<Identifier> icon, int color) {
        this(x, y, width, height, scale, icon.orElse(null), color);
    }

    public VisualData(int x, int y, int width, int height, float scale, Identifier icon) {
        this(x, y, width, height, scale, icon, DEFAULT_COLOR);
    }

    @Contract(" -> new")
    public static @NotNull VisualData empty() {
        return new VisualData(0, 0, 1, 1, 1f, Optional.empty(), DEFAULT_COLOR);
    }

    @Contract("_ -> new")
    public @NotNull VisualData withX(int x) {
        return new VisualData(x, this.y, this.width, this.height, this.scale, this.icon, this.color);
    }

    @Contract("_ -> new")
    public @NotNull VisualData withY(int y) {
        return new VisualData(this.x, y, this.width, this.height, this.scale, this.icon, this.color);
    }

    @Contract("_ -> new")
    public @NotNull VisualData withWidth(int width) {
        return new VisualData(this.x, this.y, width, this.height, this.scale, this.icon, this.color);
    }

    @Contract("_ -> new")
    public @NotNull VisualData withHeight(int height) {
        return new VisualData(this.x, this.y, this.width, height, this.scale, this.icon, this.color);
    }

    @Contract("_ -> new")
    public @NotNull VisualData withScale(float scale) {
        return new VisualData(this.x, this.y, this.width, this.height, scale, this.icon, this.color);
    }

    @Contract("_ -> new")
    public @NotNull VisualData withIcon(Identifier icon) {
        return new VisualData(this.x, this.y, this.width, this.height, this.scale, icon, this.color);
    }

    @Contract("_ -> new")
    public @NotNull VisualData withIcon(String name) {
        return new VisualData(this.x, this.y, this.width, this.height, this.scale,
                Bittermelon.identifier("textures/gui/organs/" + name + ".png"), this.color);
    }

    @Contract("_ -> new")
    public @NotNull VisualData withColor(int color) {
        return new VisualData(this.x, this.y, this.width, this.height, this.scale, this.icon, color);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("x").forGetter(VisualData::x),
                Codec.INT.fieldOf("y").forGetter(VisualData::y),
                Codec.INT.fieldOf("width").forGetter(VisualData::width),
                Codec.INT.fieldOf("height").forGetter(VisualData::height),
                Codec.FLOAT.fieldOf("scale").forGetter(VisualData::scale),
                Identifier.CODEC.optionalFieldOf("icon").forGetter(vd -> Optional.ofNullable(vd.icon)),
                Codec.INT.fieldOf("color").forGetter(VisualData::color)
        ).apply(instance, VisualData::new));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                VisualData::x,
                ByteBufCodecs.INT,
                VisualData::y,
                ByteBufCodecs.INT,
                VisualData::width,
                ByteBufCodecs.INT,
                VisualData::height,
                ByteBufCodecs.FLOAT,
                VisualData::scale,
                ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                vd -> Optional.ofNullable(vd.icon),
                ByteBufCodecs.INT,
                VisualData::color,
                VisualData::new
        );
    }
}
