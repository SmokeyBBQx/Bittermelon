package com.site21.bittermelon.common.systems.medical.compartment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VisualData {
    public static final Codec<VisualData> CODEC;
    public static final StreamCodec<ByteBuf, VisualData> STREAM_CODEC;
    public static final int DEFAULT_COLOR = 0xFFFFFFFF;

    public int x;
    public int y;
    public int width;
    public int height;
    public float scale;
    public ResourceLocation icon;
    public int color;

    public VisualData(int x, int y, int width, int height, float scale, @Nullable ResourceLocation icon, int color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.icon = icon;
        this.color = color;
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
    @Contract(pure = true)
    public VisualData(int x, int y, int width, int height, float scale, @NotNull Optional<ResourceLocation> icon, int color) {
        this(x, y, width, height, scale, icon.orElse(null), color);
    }

    public VisualData(int x, int y, int width, int height, float scale, ResourceLocation icon) {
        this(x, y, width, height, scale, icon, DEFAULT_COLOR);
    }

    @Contract(" -> new")
    public static @NotNull VisualData empty() {
        return new VisualData(0, 0, 1, 1, 1f, Optional.empty(), DEFAULT_COLOR);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Optional<ResourceLocation> getOptionalIcon() {
        return Optional.ofNullable(icon);
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
    }

    public VisualData x(int x) {
        this.x = x;
        return this;
    }

    public VisualData y(int y) {
        this.y = y;
        return this;
    }

    public VisualData width(int width) {
        this.width = width;
        return this;
    }

    public VisualData height(int height) {
        this.height = height;
        return this;
    }

    public VisualData scale(float scale) {
        this.scale = scale;
        return this;
    }

    public VisualData icon(ResourceLocation icon) {
        this.icon = icon;
        return this;
    }

    public VisualData icon(String name) {
        this.icon = Bittermelon.resource("textures/gui/organs/" + name + ".png");
        return this;
    }

    public VisualData color(int color) {
        this.color = color;
        return this;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("x").forGetter(VisualData::getX),
                Codec.INT.fieldOf("y").forGetter(VisualData::getY),
                Codec.INT.fieldOf("width").forGetter(VisualData::getWidth),
                Codec.INT.fieldOf("height").forGetter(VisualData::getHeight),
                Codec.FLOAT.fieldOf("scale").forGetter(visualData -> visualData.scale),
                ResourceLocation.CODEC.optionalFieldOf("icon").forGetter(VisualData::getOptionalIcon),
                Codec.INT.fieldOf("color").forGetter(VisualData::getColor)
        ).apply(instance, VisualData::new));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                VisualData::getX,
                ByteBufCodecs.INT,
                VisualData::getY,
                ByteBufCodecs.INT,
                VisualData::getWidth,
                ByteBufCodecs.INT,
                VisualData::getHeight,
                ByteBufCodecs.FLOAT,
                visualData -> visualData.scale,
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                VisualData::getOptionalIcon,
                ByteBufCodecs.INT,
                VisualData::getColor,
                VisualData::new
        );
    }
}
