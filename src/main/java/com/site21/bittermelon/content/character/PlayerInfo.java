package com.site21.bittermelon.content.character;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.medical.blood.BloodType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerInfo {
    public static final Codec<SkinModel> MODEL_CODEC;
    public static final StreamCodec<ByteBuf, SkinModel> MODEL_STREAM_CODEC;
    public static final Codec<PlayerInfo> CODEC;
    public static final StreamCodec<ByteBuf, PlayerInfo> STREAM_CODEC;

    private String skinURL;
    private SkinModel model;

    public PlayerInfo(String skinURL, SkinModel model) {
        this.skinURL = skinURL;
        this.model = model;
    }

    public String getSkinURL() {
        return skinURL;
    }

    public SkinModel getModel() {
        return model;
    }

    public void setSkinURL(String skinURL) {
        this.skinURL = skinURL;
    }

    public void setModel(SkinModel model) {
        this.model = model;
    }

    public enum SkinModel implements StringRepresentable {
        SLIM("slim"),
        WIDE("default");

        private final String id;

        SkinModel(String id) {
            this.id = id;
        }

        public static SkinModel byName(@Nullable String name) {
            if (name == null || name.equals("default")) {
                return WIDE;
            } else {
                return SLIM;
            }
        }

        public String id() {
            return this.id;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.id;
        }

        @Contract(pure = true)
        @OnlyIn(Dist.CLIENT)
        public PlayerSkin.@NotNull Model toMinecraftModel() {
            return PlayerSkin.Model.byName(this.id);
        }

        @OnlyIn(Dist.CLIENT)
        public static SkinModel fromMinecraftModel(PlayerSkin.@NotNull Model minecraftModel) {
            return byName(minecraftModel.id());
        }
    }

    static {
        MODEL_CODEC = StringRepresentable.fromEnum(SkinModel::values);

        MODEL_STREAM_CODEC = ByteBufCodecs.idMapper(
                i -> SkinModel.values()[i],
                SkinModel::ordinal
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
