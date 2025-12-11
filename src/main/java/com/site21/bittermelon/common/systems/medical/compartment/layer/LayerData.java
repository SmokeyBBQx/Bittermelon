package com.site21.bittermelon.common.systems.medical.compartment.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class LayerData {
    public static final Codec<LayerData> CODEC;
    public static final StreamCodec<ByteBuf, LayerData> STREAM_CODEC;

    private final String name;
    private final int width, height;
    private final LayerSlot[][] grid;

    public LayerData(String name, int width, int height, @NotNull LayerSlot[][] grid) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.grid = grid;
    }

    public LayerData(String name, int width, int height, @NotNull List<SlotPoint> shape) {
        this(name, width, height, new LayerSlot[height][width]);
        for (SlotPoint p : shape) {
            grid[p.y()][p.x()] = new LayerSlot(p.type());
        }
    }

    public LayerData(String string, Integer width, Integer height, @NotNull List<SlotData> slotData) {
        this(string, width, height, new LayerSlot[height][width]);
        for (SlotData sd : slotData) {
            grid[sd.y()][sd.x()] = sd.slot();
        }
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public LayerSlot[][] getGrid() {
        return grid;
    }

    public List<SlotData> getSerializableGrid() {
        List<SlotData> slots = new ArrayList<>();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (grid[y][x] != null) {
                    slots.add(new SlotData(x, y, grid[y][x]));
                }
            }
        }
        return slots;
    }

    public boolean canFit(int x, int y, @NotNull List<Point> shape) {
        for (Point p : shape) {
            int targetX = x + p.x;
            int targetY = y + p.y;
            if (targetX < 0 || targetX >= width || targetY < 0 || targetY >= height) return false;
            if (grid[targetY][targetX] == null) return false;
            if (grid[targetY][targetX].isOccupied()) return false;
        }
        return true;
    }

    public boolean attemptToPlace(int x, int y, @NotNull UUID instanceId, @NotNull List<Point> shape) {
        if (!canFit(x, y, shape)) return false;

        for (Point p : shape) {
            int targetX = x + p.x;
            int targetY = y + p.y;
            grid[targetY][targetX].setInstanceId(instanceId);
        }

        return true;
    }

    public void removeInstance(@NotNull UUID instanceId) {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (grid[y][x] != null && instanceId.equals(grid[y][x].getInstanceId())) {
                    grid[y][x].setInstanceId(null);
                }
            }
        }
    }

    public void revealSlots(int x, int y, @NotNull List<Point> shape, float visibility) {
        for (Point p : shape) {
            int targetX = x + p.x;
            int targetY = y + p.y;
            if (targetX < 0 || targetX >= width || targetY < 0 || targetY >= height) continue;
            if (grid[targetY][targetX] != null) {
                grid[targetY][targetX].setVisibility(visibility);
            }
        }
    }

    public record SlotData(int x, int y, LayerSlot slot) {
        public static final Codec<SlotData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("x").forGetter(SlotData::x),
                Codec.INT.fieldOf("y").forGetter(SlotData::y),
                LayerSlot.CODEC.fieldOf("slot").forGetter(SlotData::slot)
        ).apply(instance, SlotData::new));

        public static final StreamCodec<ByteBuf, SlotData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                SlotData::x,
                ByteBufCodecs.INT,
                SlotData::y,
                LayerSlot.STREAM_CODEC,
                SlotData::slot,
                SlotData::new
        );
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Codec.STRING.fieldOf("name").forGetter(LayerData::getName),
                        Codec.INT.fieldOf("width").forGetter(LayerData::getWidth),
                        Codec.INT.fieldOf("height").forGetter(LayerData::getHeight),
                        Codec.list(SlotData.CODEC).fieldOf("slots").forGetter(LayerData::getSerializableGrid)
                ).apply(instance, LayerData::new)
        );

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                LayerData::getName,
                ByteBufCodecs.INT,
                LayerData::getWidth,
                ByteBufCodecs.INT,
                LayerData::getHeight,
                SlotData.STREAM_CODEC.apply(ByteBufCodecs.list()),
                LayerData::getSerializableGrid,
                LayerData::new
        );
    }
}
