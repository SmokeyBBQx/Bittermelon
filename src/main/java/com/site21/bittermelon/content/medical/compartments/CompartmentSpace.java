package com.site21.bittermelon.content.medical.compartments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.medical.compartments.firstaid.Retractor;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class CompartmentSpace {
    private final HashSet<UUID> compartments;
    private final Map<Integer, HashSet<UUID>> layers;
    private final Map<Integer, UUID> obscuringCompartments;
    private int unlockedLayer;
    private ResourceLocation backgroundTexture;

    public static final Codec<CompartmentSpace> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(UUIDUtil.CODEC).xmap(HashSet::new, ArrayList::new)
                    .fieldOf("compartments").forGetter(compartment -> compartment.compartments),
            Codec.unboundedMap(Codec.INT, Codec.list(UUIDUtil.CODEC).xmap(
                    HashSet::new,
                    ArrayList::new
            )).fieldOf("layers").forGetter(compartment -> compartment.layers),
            Codec.unboundedMap(Codec.INT, UUIDUtil.CODEC)
                    .fieldOf("obscuringCompartments").forGetter(compartment -> compartment.obscuringCompartments),
            Codec.INT.fieldOf("unlockedLayer").forGetter(compartment -> compartment.unlockedLayer),
            ResourceLocation.CODEC.fieldOf("backgroundTexture").forGetter(compartment -> compartment.backgroundTexture)
    ).apply(instance, CompartmentSpace::new));

    public static final StreamCodec<ByteBuf, CompartmentSpace> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(HashSet::new, UUIDUtil.STREAM_CODEC),
            CompartmentSpace::getCompartments,
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.INT,
                    ByteBufCodecs.collection(HashSet::new, UUIDUtil.STREAM_CODEC)
            ),
            CompartmentSpace::getLayers,
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.INT,
                    UUIDUtil.STREAM_CODEC
            ),
            CompartmentSpace::getObscuringCompartments,
            ByteBufCodecs.INT,
            CompartmentSpace::getUnlockedLayer,
            ResourceLocation.STREAM_CODEC,
            CompartmentSpace::getBackgroundTexture,
            CompartmentSpace::new
    );

    public CompartmentSpace(HashSet<UUID> compartments, Map<Integer, HashSet<UUID>> layers,
                            Map<Integer, UUID> obscuringCompartments, int unlockedLayer, ResourceLocation backgroundTexture) {
        this.compartments = compartments;
        this.layers = layers;
        this.obscuringCompartments = obscuringCompartments;
        this.unlockedLayer = unlockedLayer;
        this.backgroundTexture = backgroundTexture;
    }

    public CompartmentSpace(ResourceLocation backgroundTexture) {
        this.compartments = new HashSet<>();
        this.layers = new HashMap<>();
        layers.put(0, new HashSet<>());
        this.obscuringCompartments = new HashMap<>();
        this.unlockedLayer = 0;
        this.backgroundTexture = backgroundTexture;
    }

    public CompartmentSpace() {
        this(ResourceLocation.withDefaultNamespace("textures/block/nether_wart_block.png"));
    }

    public HashSet<UUID> getCompartments() {
        return compartments;
    }

    public Map<Integer, HashSet<UUID>> getLayers() {
        return layers;
    }

    public Map<Integer, UUID> getObscuringCompartments() {
        return obscuringCompartments;
    }

    public ResourceLocation getBackgroundTexture() {
        return backgroundTexture;
    }

    public void setBackgroundTexture(ResourceLocation texture) {
        this.backgroundTexture = texture;
    }

    public void addToLayer(int layer, UUID uuid) {
        compartments.add(uuid);
        layers.computeIfAbsent(layer, k -> new HashSet<>()).add(uuid);
    }

    public void removeFromLayer(int layer, UUID uuid) {
        compartments.remove(uuid);
        layers.computeIfPresent(layer, (k, v) -> {
            v.remove(uuid);
            return v.isEmpty() ? null : v;
        });
    }

    public int getUnlockedLayer() {
        return unlockedLayer;
    }

    public void setUnlockedLayer(int unlockedLayer) {
        this.unlockedLayer = unlockedLayer;
    }

    public void updateUnlockedLayer(MedicalStats medicalStats) {
        int count = 1;
        for (Map.Entry<Integer, UUID> entry : obscuringCompartments.entrySet()) {
            CompartmentInstance compartment = medicalStats.getCompartment(entry.getValue());
            if (compartment == null) {
                count++;
                continue;
            }

            boolean hasRetractor = compartment.getCompartmentSpace().compartments.stream().anyMatch(childID -> {
                        CompartmentInstance child = medicalStats.getCompartment(childID);
                        return child != null && child.getCompartment() instanceof Retractor;
                    });

            if (hasRetractor) {
                count++;
                continue;
            }
            break;
        }
        setUnlockedLayer(count);
    }
}
