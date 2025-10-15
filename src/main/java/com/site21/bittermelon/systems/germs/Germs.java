package com.site21.bittermelon.systems.germs;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Germs(List<Germ> germs) {

    public static final Codec<Germs> CODEC = Germ.CODEC
            .listOf()
            .xmap(
                    Germs::new,
                    Germs::germs
            );

    public static final StreamCodec<FriendlyByteBuf, Germs> STREAM_CODEC =  Germ.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(Germs::new, Germs::germs);

    @Override
    public boolean equals(Object other) {
        return this == other;
//            return other instanceof Germs(List<GermInstance> germs1) && GermInstance.listMatches(germs, germs1);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Germs" + this.germs;
    }
}
