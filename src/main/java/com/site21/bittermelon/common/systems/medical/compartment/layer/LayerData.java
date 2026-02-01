package com.site21.bittermelon.common.systems.medical.compartment.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PIVOT;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.SHAPE;

public class LayerData {
    public static final Codec<LayerData> CODEC;
    public static final StreamCodec<ByteBuf, LayerData> STREAM_CODEC;

    private final String name;
    private final int width, height, depth;
    private final LayerSlot[][] grid;
    private final HashMap<Point, UUID>[] compartments;
    private ResourceLocation texture;

    public LayerData(String name, int width, int height, int depth, @NotNull LayerSlot[][] grid,
                     HashMap<Point, UUID>[] compartments, ResourceLocation texture) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.grid = grid;
        this.compartments = compartments;
        this.texture = texture;
    }

    public LayerData(String name, int width, int height, int depth, @NotNull List<SlotPoint> shape) {
        this(name, width, height, depth, new LayerSlot[height][width], createCompartmentArray(depth), null);
        for (SlotPoint p : shape) {
            grid[p.y()][p.x()] = new LayerSlot(p.type(), depth);
        }
    }

    public LayerData(String name, int width, int height, @NotNull List<SlotPoint> shape) {
        this(name, width, height, 1, new LayerSlot[height][width], createCompartmentArray(1), null);
        for (SlotPoint p : shape) {
            grid[p.y()][p.x()] = new LayerSlot(p.type(), 1);
        }
    }

    public LayerData(String name, int width, int height, int depth, @NotNull List<SlotData> slotData,
                     List<HashMap<Point, UUID>> compartments, Optional<ResourceLocation> texture) {
        this(name, width, height, depth, new LayerSlot[height][width], createCompartmentArray(compartments), texture.orElse(null));
        System.out.println("Reconstructing LayerData '" + name + "' with " + slotData.size() + " slots.");
        for (SlotData sd : slotData) {
            grid[sd.y()][sd.x()] = sd.slot();
        }
    }

    @Contract("_, _, _, _, _ -> new")
    public static @NotNull LayerData fromRegularShape(String name, int width, int height, int depth, SlotType type) {
        LayerSlot[][] grid = new LayerSlot[height][width];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                grid[y][x] = new LayerSlot(type, depth);
            }
        }
        return new LayerData(name, width, height, depth, grid, createCompartmentArray(depth), null);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull LayerData fromRegularShape(String name, int width, int height, SlotType type) {
        return fromRegularShape(name, width, height, 1, type);
    }

    @SuppressWarnings("unchecked")
    private static HashMap<Point, UUID>[] createCompartmentArray(int depth) {
        HashMap<Point, UUID>[] arr = new HashMap[depth];
        for (int i = 0; i < depth; i++) {
            arr[i] = new HashMap<>();
        }
        return arr;
    }

    private static HashMap<Point, UUID>[] createCompartmentArray(List<HashMap<Point, UUID>> list) {
        @SuppressWarnings("unchecked")
        HashMap<Point, UUID>[] arr = new HashMap[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
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

    public int getDepth() {
        return depth;
    }

    public LayerSlot[][] getGrid() {
        return grid;
    }

    public Map<Point, UUID>[] getCompartments() {
        return compartments;
    }

    public List<HashMap<Point, UUID>> getCompartmentsList() {
        return Arrays.asList(compartments);
    }

    public List<Map<Point, UUID>> getCompartmentsListAbstract() {
        return Arrays.asList(compartments);
    }


    public Map<Point, UUID> getCompartmentsAt(int z) {
        return compartments[z];
    }

    public UUID getCompartmentAt(int x, int y, int z) {
        Point pivot = grid[y][x].getPivot(z);
        if (pivot == null) return null;
        return compartments[z].get(pivot);
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public Optional<ResourceLocation> getTextureOptional() {
        return Optional.ofNullable(texture);
    }

    public LayerData setTexture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    /**
     * Converts the grid into a list of serializable SlotData objects for storage
     *
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
     *
     * @param x     the x-coordinate to check
     * @param y     the y-coordinate to check
     * @param z     the depth layer to check
     * @param shape the shape defining which slots the compartment would occupy
     * @return true if the compartment can fit; false otherwise
     */
    public boolean canFit(int x, int y, int z, @NotNull List<Point> shape) {
        if (z < 0 || z >= depth) return false;

        for (Point p : shape) {
            int targetX = x + p.x();
            int targetY = y + p.y();
            if (targetX < 0 || targetX >= width || targetY < 0 || targetY >= height) return false;
            if (grid[targetY][targetX] == null) return false;
            if (grid[targetY][targetX].isOccupied(z)) return false;
        }
        return true;
    }

    /**
     * Checks if a compartment instance can fit at the given (x, y) position in the layer.
     *
     * @param x           the x-coordinate to check
     * @param y           the y-coordinate to check
     * @param z           the depth layer to check
     * @param compartment the compartment instance to check
     * @return true if the compartment can fit; false otherwise
     */
    public boolean canFit(int x, int y, int z, @NotNull CompartmentInstance compartment) {
        List<Point> shape = compartment.getOrDefault(SHAPE, List.of());
        return canFit(x, y, z, shape);
    }

    /**
     * Attempts to place a compartment instance at the specified (x, y) position in the layer.
     *
     * @param x           the x-coordinate to place the compartment at
     * @param y           the y-coordinate to place the compartment at
     * @param z           the depth layer to place the compartment in
     * @param compartment the compartment instance to place
     * @return true if the compartment was successfully placed; false otherwise
     */
    public boolean tryToPlace(int x, int y, int z, @NotNull CompartmentInstance compartment) {
        List<Point> shape = compartment.getOrDefault(SHAPE, List.of());
        if (!canFit(x, y, z, shape)) return false;

        Point pivotBase = compartment.getOrDefault(PIVOT, new Point(0, 0));
        Point pivot = new Point(x + pivotBase.x(), y + pivotBase.y());
        compartments[z].put(pivot, compartment.getId());

        for (Point p : shape) {
            int targetX = x + p.x();
            int targetY = y + p.y();
            grid[targetY][targetX].setPivot(z, pivot);
        }

        return true;
    }

    /**
     * Clears all slots associated with the specified compartment instance ID.
     *
     * @param instanceId the UUID of the compartment instance to remove
     */
    public void removeInstance(@NotNull UUID instanceId) {
        for (int z = 0; z < depth; ++z) {
            if (!compartments[z].containsValue(instanceId)) return;

            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    if (grid[y][x] == null) continue;

                    Point pivot = grid[y][x].getPivot(z);
                    if (pivot == null) continue;

                    UUID compartmentId = compartments[z].get(pivot);
                    if (compartmentId == null || instanceId.equals(compartmentId)) {
                        grid[y][x].setPivot(z, null);
                        compartments[z].remove(pivot);
                    }
                }
            }
        }
    }

    /**
     * Streams all non-null slots in the layer. Useful for iterating over occupied slots.
     *
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

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid) + compartments.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LayerData other &&
                Arrays.deepEquals(this.grid, other.grid) &&
                this.compartments.equals(other.compartments);
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
                        Codec.INT.fieldOf("depth").forGetter(LayerData::getDepth),
                        Codec.list(SlotData.CODEC).fieldOf("slots").forGetter(LayerData::getSerializableGrid),
                        Codec.list(Codec.unboundedMap(Point.STRING_CODEC, UUIDUtil.CODEC)).fieldOf("compartments")
                                .forGetter(LayerData::getCompartmentsListAbstract),
                        ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(LayerData::getTextureOptional)
                ).apply(instance, (name, width, height, depth, slots,
                                   compartments, texture) ->
                        new LayerData(name, width, height, depth, slots, compartments.stream().map(HashMap::new).toList(), texture)
                )
        );

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                LayerData::getName,
                ByteBufCodecs.INT,
                LayerData::getWidth,
                ByteBufCodecs.INT,
                LayerData::getHeight,
                ByteBufCodecs.INT,
                LayerData::getDepth,
                SlotData.STREAM_CODEC.apply(ByteBufCodecs.list()),
                LayerData::getSerializableGrid,
                ByteBufCodecs.map(
                        HashMap::new,
                        Point.STREAM_CODEC,
                        UUIDUtil.STREAM_CODEC).apply(ByteBufCodecs.list()),
                LayerData::getCompartmentsList,
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                LayerData::getTextureOptional,
                LayerData::new
        );
    }
}
