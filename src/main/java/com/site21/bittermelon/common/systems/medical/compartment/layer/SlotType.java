package com.site21.bittermelon.common.systems.medical.compartment.layer;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum SlotType implements StringRepresentable {
    CAVITY(ResourceLocation.withDefaultNamespace("textures/block/tinted_glass.png")),
    SKIN(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png")),
    MUSCLE(ResourceLocation.withDefaultNamespace("textures/block/netherrack.png")),
    FAT(ResourceLocation.withDefaultNamespace("textures/block/horn_coral_block.png")),
    MEMBRANE(ResourceLocation.withDefaultNamespace("textures/block/dead_brain_coral_block.png")),
    BONE(ResourceLocation.withDefaultNamespace("textures/block/bone_block_side.png")),
    BRAIN_TISSUE(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png")),
    ORGAN(ResourceLocation.withDefaultNamespace("textures/block/brown_terracotta.png")),;

    private final ResourceLocation texture;

    public ResourceLocation getTexture() {
        return texture;
    }

    SlotType(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase();
    }

    public static final EnumCodec<SlotType> CODEC = StringRepresentable.fromEnum(SlotType::values);

    public static final StreamCodec<ByteBuf, SlotType> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> SlotType.values()[i],
            SlotType::ordinal
    );
}
