package com.site21.bittermelon.common.systems.medical.compartment.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class LayerSlot {
    public static final Codec<LayerSlot> CODEC;
    public static final StreamCodec<ByteBuf, LayerSlot> STREAM_CODEC;

    private final SlotType type;
    private UUID instanceId;
    private float visibility;
    private float bloodLevel;

    public LayerSlot(SlotType type, @Nullable UUID instanceId, float visibility, float bloodLevel) {
        this.type = type;
        this.instanceId = instanceId;
        this.visibility = visibility;
        this.bloodLevel = bloodLevel;
    }

    public LayerSlot(SlotType type, @NotNull Optional<UUID> instanceId, Float visibility, Float bloodLevel) {
        this(type, instanceId.orElse(null), visibility, bloodLevel);
    }

    public LayerSlot(SlotType type) {
        this(type, null, 0f, 0f);
    }

    public SlotType getType() {
        return type;
    }

    @Nullable
    public UUID getInstanceId() {
        return instanceId;
    }

    public Optional<UUID> getInstanceIdOpt() {
        return Optional.ofNullable(instanceId);
    }

    public void setInstanceId(UUID instanceId) {
        this.instanceId = instanceId;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public float getBloodLevel() {
        return bloodLevel;
    }

    public void setBloodLevel(float bloodLevel) {
        this.bloodLevel = bloodLevel;
    }

    public boolean isOccupied() {
        return instanceId != null;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SlotType.CODEC.fieldOf("type").forGetter(LayerSlot::getType),
                UUIDUtil.CODEC.optionalFieldOf("instance_id").forGetter(LayerSlot::getInstanceIdOpt),
                Codec.FLOAT.fieldOf("visibility").forGetter(LayerSlot::getVisibility),
                Codec.FLOAT.fieldOf("blood_level").forGetter(LayerSlot::getBloodLevel)
        ).apply(instance, LayerSlot::new));

        STREAM_CODEC = StreamCodec.composite(
                SlotType.STREAM_CODEC,
                LayerSlot::getType,
                ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC),
                LayerSlot::getInstanceIdOpt,
                ByteBufCodecs.FLOAT,
                LayerSlot::getVisibility,
                ByteBufCodecs.FLOAT,
                LayerSlot::getBloodLevel,
                LayerSlot::new
        );
    }
}
