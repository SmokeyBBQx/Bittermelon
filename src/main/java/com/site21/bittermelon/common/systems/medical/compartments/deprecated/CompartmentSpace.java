package com.site21.bittermelon.common.systems.medical.compartments.deprecated;

@Deprecated
public class CompartmentSpace {
//    private final List<LayerData> layers;
//
//    public static final Codec<CompartmentSpace> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            Codec.unboundedMap(Codec.INT, Codec.list(UUIDUtil.CODEC).xmap(
//                    HashSet::new,
//                    ArrayList::new
//            )).fieldOf("layers").forGetter(compartment -> compartment.layers),
//    ).apply(instance, CompartmentSpace::new));
//
//    public static final StreamCodec<ByteBuf, CompartmentSpace> STREAM_CODEC = StreamCodec.composite(
//            ByteBufCodecs.collection(HashSet::new, UUIDUtil.STREAM_CODEC),
//            CompartmentSpace::getCompartments,
//            ByteBufCodecs.map(
//                    HashMap::new,
//                    ByteBufCodecs.INT,
//                    ByteBufCodecs.collection(HashSet::new, UUIDUtil.STREAM_CODEC)
//            ),
//            CompartmentSpace::getLayers,
//            CompartmentSpace::new
//    );
//
//    public CompartmentSpace(Map<Integer, HashSet<UUID>> layers) {
//        this.layers = layers;
//    }
//
//    public CompartmentSpace(ResourceLocation backgroundTexture) {
//        this.layers = new HashMap<>();
//    }
//
//    public CompartmentSpace() {
//        this(ResourceLocation.withDefaultNamespace("textures/block/nether_wart_block.png"));
//    }
//
//    public HashSet<UUID> getCompartments() {
//        return compartments;
//    }
//
//    public Map<Integer, HashSet<UUID>> getLayers() {
//        return layers;
//    }
//
//    public void addToLayer(int layer, UUID uuid) {
//        compartments.add(uuid);
//        layers.computeIfAbsent(layer, k -> new HashSet<>()).add(uuid);
//    }
//
//    public void removeFromLayer(int layer, UUID uuid) {
//        compartments.remove(uuid);
//        layers.computeIfPresent(layer, (k, v) -> {
//            v.remove(uuid);
//            return v.isEmpty() ? null : v;
//        });
//    }
}
