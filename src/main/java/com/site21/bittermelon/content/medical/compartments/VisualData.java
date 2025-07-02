package com.site21.bittermelon.content.medical.compartments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class VisualData {
    public int x;
    public int y;
    public int z;
    public float scale;
    public int width;
    public int height;
    public ResourceLocation icon;
    public boolean isHidden;

    public static final Codec<VisualData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(VisualData::getX),
            Codec.INT.fieldOf("y").forGetter(VisualData::getY),
            Codec.INT.fieldOf("z").forGetter(VisualData::getZ),
            Codec.FLOAT.fieldOf("scale").forGetter(VisualData::getScale),
            Codec.INT.fieldOf("width").forGetter(VisualData::getWidth),
            Codec.INT.fieldOf("height").forGetter(VisualData::getHeight),
            ResourceLocation.CODEC.optionalFieldOf("icon").forGetter(VisualData::getOptionalIcon)
    ).apply(instance, VisualData::new));

    public static final StreamCodec<ByteBuf, VisualData> STREAM_CODEC = new StreamCodec<ByteBuf, VisualData>() {
        @Override
        public @NotNull VisualData decode(@NotNull ByteBuf buf) {
            int x = ByteBufCodecs.INT.decode(buf);
            int y = ByteBufCodecs.INT.decode(buf);
            int z = ByteBufCodecs.INT.decode(buf);
            float scale = ByteBufCodecs.FLOAT.decode(buf);
            int width = ByteBufCodecs.INT.decode(buf);
            int height = ByteBufCodecs.INT.decode(buf);
            boolean hasIcon = ByteBufCodecs.BOOL.decode(buf);
            ResourceLocation icon = hasIcon ? ResourceLocation.STREAM_CODEC.decode(buf) : null;
            return new VisualData(x, y, z, scale, width, height, icon);
        }

        @Override
        public void encode(@NotNull ByteBuf buf, @NotNull VisualData value) {
            ByteBufCodecs.INT.encode(buf, value.getX());
            ByteBufCodecs.INT.encode(buf, value.getY());
            ByteBufCodecs.INT.encode(buf, value.getZ());
            ByteBufCodecs.FLOAT.encode(buf, value.getScale());
            ByteBufCodecs.INT.encode(buf, value.getWidth());
            ByteBufCodecs.INT.encode(buf, value.getHeight());
            boolean hasIcon = value.getIcon() != null;
            ByteBufCodecs.BOOL.encode(buf, hasIcon);
            if (hasIcon) {
                ResourceLocation.STREAM_CODEC.encode(buf, value.getIcon());
            }
        }
    };

    public VisualData(int x, int y, int z, float scale, int width, int height, ResourceLocation icon) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = scale;
        this.width = width;
        this.height = height;
        this.icon = icon;
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
    @Contract(pure = true)
    public VisualData(int x, int y, int z, float scale, int width, int height, @NotNull Optional<ResourceLocation> icon) {
        this(x, y, z, scale, width, height, icon.orElse(null));
    }

    public VisualData(int x, int y, int z, float scale, int width, int height) {
        this(x, y, z, scale, width, height, Optional.empty());
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

    public Optional<ResourceLocation> getOptionalIcon() {
        return Optional.ofNullable(icon);
    }

    public ResourceLocation getIcon() {
        return icon;
    }
}
