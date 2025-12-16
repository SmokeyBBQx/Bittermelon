package com.site21.bittermelon.common.systems.medical.compartment.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class LayerData {
    public static final Codec<LayerData> CODEC;
    public static final StreamCodec<ByteBuf, LayerData> STREAM_CODEC;

    private final String name;
    private final int width, height;
    private final LayerSlot[][] grid;
    private final Map<Point, UUID> compartments;

    public LayerData(String name, int width, int height, @NotNull LayerSlot[][] grid, Map<Point, UUID> compartments) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.compartments = compartments;
    }

    public LayerData(String name, int width, int height, @NotNull List<SlotPoint> shape) {
        this(name, width, height, new LayerSlot[height][width], new HashMap<>());
        for (SlotPoint p : shape) {
            grid[p.y()][p.x()] = new LayerSlot(p.type());
        }
    }

    public LayerData(String string, Integer width, Integer height, @NotNull List<SlotData> slotData, Map<Point, UUID> compartments) {
        this(string, width, height, new LayerSlot[height][width], compartments);
        for (SlotData sd : slotData) {
            grid[sd.y()][sd.x()] = sd.slot();
        }
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull LayerData fromRegularShape(String name, int width, int height, SlotType type) {
        LayerSlot[][] grid = new LayerSlot[height][width];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                grid[y][x] = new LayerSlot(type);
            }
        }
        return new LayerData(name, width, height, grid, new HashMap<>());
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

    public Map<Point, UUID> getCompartments() {
        return compartments;
    }

    public UUID getCompartmentAt(int x, int y) {
        Point pivot = grid[y][x].getPivot();
        if (pivot == null) return null;
        return compartments.get(pivot);
    }

    /**
     * Converts the grid into a list of serializable SlotData objects for storage
     * @return A list of SlotData representing non-null slots in the grid.
     */
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

    /**
     * Checks if a compartment with the specified shape can fit at the given (x, y) position in the layer.
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @param shape the shape defining which slots the compartment would occupy
     * @return true if the compartment can fit; false otherwise
     */
    public boolean canFit(int x, int y, @NotNull List<Point> shape) {
        for (Point p : shape) {
            int targetX = x + p.x();
            int targetY = y + p.y();
            if (targetX < 0 || targetX >= width || targetY < 0 || targetY >= height) return false;
            if (grid[targetY][targetX] == null) return false;
            if (grid[targetY][targetX].isOccupied()) return false;
        }
        return true;
    }

    /**
     * Attempts to place a compartment instance at the specified (x, y) position in the layer.
     * @param x the x-coordinate to place the compartment at
     * @param y the y-coordinate to place the compartment at
     * @param compartment the compartment instance to place
     * @param shape the shape defining which slots the compartment occupies
     * @return true if the compartment was successfully placed; false otherwise
     */
    public boolean tryToPlace(int x, int y, @NotNull CompartmentInstance compartment, @NotNull List<Point> shape) {
        if (!canFit(x, y, shape)) return false;

        Point pivotBase = compartment.getCompartment().getPivot();
        Point pivot = new Point(x + pivotBase.x(), y + pivotBase.y());
        compartments.put(pivot, compartment.getId());

        for (Point p : shape) {
            int targetX = x + p.x();
            int targetY = y + p.y();
            grid[targetY][targetX].setPivot(pivot);
        }

        return true;
    }

    /**
     * Clears all slots associated with the specified compartment instance ID.
     * @param instanceId the UUID of the compartment instance to remove
     */
    public void removeInstance(@NotNull UUID instanceId) {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (grid[y][x] == null) continue;

                Point pivot = grid[y][x].getPivot();
                if (pivot == null) continue;

                UUID compartmentId = compartments.get(pivot);
                if (compartmentId == null || instanceId.equals(compartmentId)) {
                    grid[y][x].setPivot(null);
                    compartments.remove(pivot);
                }
            }
        }
    }

    /**
     * Reveals slots in the specified shape at the given (x, y) position by setting their visibility.
     * @param x the x-coordinate to start revealing from
     * @param y the y-coordinate to start revealing from
     * @param shape the shape defining which slots to reveal
     * @param visibility the visibility level to set for the revealed slots
     */
    public void revealSlots(int x, int y, @NotNull List<Point> shape, float visibility) {
        for (Point p : shape) {
            int targetX = x + p.x();
            int targetY = y + p.y();
            if (targetX < 0 || targetX >= width || targetY < 0 || targetY >= height) continue;
            if (grid[targetY][targetX] != null) {
                grid[targetY][targetX].setVisibility(visibility);
            }
        }
    }

    /**
     * Streams all non-null slots in the layer. Useful for iterating over occupied slots.
     * @return A stream of SlotData representing non-null slots.
     */
    public Stream<SlotData> streamSlots() {
        List<SlotData> slots = new ArrayList<>();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (grid[y][x] != null) {
                    slots.add(new SlotData(x, y, grid[y][x]));
                }
            }
        }
        return slots.stream();
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
                        Codec.list(SlotData.CODEC).fieldOf("slots").forGetter(LayerData::getSerializableGrid),
                        Codec.unboundedMap(Point.STRING_CODEC, UUIDUtil.CODEC).fieldOf("compartments").forGetter(LayerData::getCompartments)
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
                ByteBufCodecs.map(
                        HashMap::new,
                        Point.STREAM_CODEC,
                        UUIDUtil.STREAM_CODEC),
                LayerData::getCompartments,
                LayerData::new
        );
    }
}
