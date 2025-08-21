package com.site21.bittermelon.content.character;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.medical.blood.BloodType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public class PlayerInfo {
    public static final Codec<PlayerSkin.Model> MODEL_CODEC;
    public static final StreamCodec<ByteBuf, PlayerSkin.Model> MODEL_STREAM_CODEC;
    public static final Codec<PlayerInfo> CODEC;
    public static final StreamCodec<ByteBuf, PlayerInfo> STREAM_CODEC;

    private String skinURL;
    private PlayerSkin.Model model;

    public PlayerInfo(String skinURL, PlayerSkin.Model model) {
        this.skinURL = skinURL;
        this.model = model;
    }

    public String getSkinURL() {
        return skinURL;
    }

    public PlayerSkin.Model getModel() {
        return model;
    }

    public void setSkinURL(String skinURL) {
        this.skinURL = skinURL;
    }

    public void setModel(PlayerSkin.Model model) {
        this.model = model;
    }

    static {
        MODEL_CODEC = Codec.STRING.xmap(
                PlayerSkin.Model::byName,
                PlayerSkin.Model::id
        );

        MODEL_STREAM_CODEC = ByteBufCodecs.idMapper(
                i -> PlayerSkin.Model.values()[i],
                PlayerSkin.Model::ordinal
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
