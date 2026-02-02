package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.cage.client.BlockInfo;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class BitterDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Bittermelon.MOD_ID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<BlockInfo>>> BLOCK_INFO = ENTITY_DATA_SERIALIZERS.register(
            "blocks",
            () -> new EntityDataSerializer<>() {
                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, List<BlockInfo>> codec() {
                    return BlockInfo.STREAM_CODEC.apply(ByteBufCodecs.list());
                }

                @Override
                public List<BlockInfo> copy(List<BlockInfo> value) {
                    return new ArrayList<>(value);
                }
            }
    );
}
