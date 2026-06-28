package com.site21.bittermelon.common.systems.character;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.PlayerModelType;

public class PlayerInfo {
    public static final Codec<PlayerModelType> MODEL_CODEC;
    public static final StreamCodec<ByteBuf, PlayerModelType> MODEL_STREAM_CODEC;
    public static final Codec<PlayerInfo> CODEC;
    public static final StreamCodec<ByteBuf, PlayerInfo> STREAM_CODEC;

    private String skinURL;
    private PlayerModelType model;

    public PlayerInfo(String skinURL, PlayerModelType model) {
        this.skinURL = skinURL;
        this.model = model;
    }

    public String getSkinURL() {
        return skinURL;
    }

    public PlayerModelType getModel() {
        return model;
    }

    /**
     * Sets the skin URL. This should be a direct link to the skin image.
     * @param skinURL the URL of the skin image
     */
    public void setSkinURL(String skinURL) {
        this.skinURL = skinURL;
    }

    /**
     * Sets the skin model (SLIM or WIDE).
     * @param model the skin model
     */
    public void setModel(PlayerModelType model) {
        this.model = model;
    }

    static {
        MODEL_CODEC = StringRepresentable.fromEnum(PlayerModelType::values);

        MODEL_STREAM_CODEC = ByteBufCodecs.idMapper(
                i -> PlayerModelType.values()[i],
                PlayerModelType::ordinal
        );

        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("skinURL").forGetter(PlayerInfo::getSkinURL),
                MODEL_CODEC.fieldOf("model").forGetter(PlayerInfo::getModel)
        ).apply(instance, PlayerInfo::new));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                PlayerInfo::getSkinURL,
                MODEL_STREAM_CODEC,
                PlayerInfo::getModel,
                PlayerInfo::new
        );
    }
}
