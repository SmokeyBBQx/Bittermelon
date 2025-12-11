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

import java.util.Optional;

public class VisualData {
    public static final Codec<VisualData> CODEC;
    public static final StreamCodec<ByteBuf, VisualData> STREAM_CODEC;
    public static final int DEFAULT_COLOR = 0xFFFFFFFF;

    public int x;
    public int y;
    public float scale;
    public ResourceLocation icon;
    public int color;

    public VisualData(int x, int y, float scale, ResourceLocation icon, int color) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.icon = icon;
        this.color = color;
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
    @Contract(pure = true)
    public VisualData(int x, int y, float scale, @NotNull Optional<ResourceLocation> icon, int color) {
        this(x, y, scale, icon.orElse(null), color);
    }

    public VisualData(int x, int y, float scale, ResourceLocation icon) {
        this(x, y, scale, icon, DEFAULT_COLOR);
    }

    @Contract(" -> new")
    public static @NotNull VisualData empty() {
        return new VisualData(0, 0, 1, 0, 0);
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

    public VisualData z(int z) {
        this.z = z;
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
        this.icon = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/organs/" + name + ".png");
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
                Codec.INT.fieldOf("z").forGetter(VisualData::getZ),
                ResourceLocation.CODEC.optionalFieldOf("icon").forGetter(VisualData::getOptionalIcon),
                Codec.INT.fieldOf("color").forGetter(VisualData::getColor)
        ).apply(instance, VisualData::new));

        STREAM_CODEC = new StreamCodec<>() {
            @Override
            public @NotNull VisualData decode(@NotNull ByteBuf buf) {
                int x = ByteBufCodecs.INT.decode(buf);
                int y = ByteBufCodecs.INT.decode(buf);
                int z = ByteBufCodecs.INT.decode(buf);
                float scale = ByteBufCodecs.FLOAT.decode(buf);
                boolean hasIcon = ByteBufCodecs.BOOL.decode(buf);
                ResourceLocation icon = hasIcon ? ResourceLocation.STREAM_CODEC.decode(buf) : null;
                int color = ByteBufCodecs.INT.decode(buf);
                return new VisualData(x, y, z, scale, icon, color);
            }

            @Override
            public void encode(@NotNull ByteBuf buf, @NotNull VisualData value) {
                ByteBufCodecs.INT.encode(buf, value.getX());
                ByteBufCodecs.INT.encode(buf, value.getY());
                ByteBufCodecs.INT.encode(buf, value.getZ());
                boolean hasIcon = value.getIcon() != null;
                ByteBufCodecs.BOOL.encode(buf, hasIcon);
                if (hasIcon) {
                    ResourceLocation.STREAM_CODEC.encode(buf, value.getIcon());
                }
                ByteBufCodecs.INT.encode(buf, value.getColor());
            }
        };
    }
}
