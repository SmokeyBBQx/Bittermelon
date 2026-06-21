package com.site21.bittermelon.common.systems.medical.compartment.layer;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum SlotType implements StringRepresentable {
    CAVITY(Identifier.withDefaultNamespace("textures/block/tinted_glass.png")),
    SKIN(Bittermelon.resource("textures/gui/medical/skin.png")),
    MUSCLE(Identifier.withDefaultNamespace("textures/block/netherrack.png")),
    FAT(Identifier.withDefaultNamespace("textures/block/horn_coral_block.png")),
    MEMBRANE(Identifier.withDefaultNamespace("textures/block/dead_brain_coral_block.png")),
    BONE(Identifier.withDefaultNamespace("textures/block/bone_block_side.png")),
    BRAIN_TISSUE(Identifier.withDefaultNamespace("textures/block/brain_coral_block.png")),
    ORGAN(Identifier.withDefaultNamespace("textures/block/brown_terracotta.png")),;

    private final Identifier texture;

    public Identifier getTexture() {
        return texture;
    }

    SlotType(Identifier texture) {
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
