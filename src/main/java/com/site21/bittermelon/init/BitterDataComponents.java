package com.site21.bittermelon.init;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.containers.item.client.ItemContainerContents;
import com.site21.bittermelon.content.items.containers.substance.data.SubstanceContents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BitterDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Bittermelon.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SubstanceContents>> SUBSTANCE_CONTENTS = DATA_COMPONENTS.registerComponentType(
            "substance_contents",
            builder -> builder.persistent(SubstanceContents.CODEC).networkSynchronized(SubstanceContents.STREAM_CODEC).cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TRANSFER_RATE = DATA_COMPONENTS.registerComponentType(
            "transfer_rate",
            builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> RELEASE_PRESSURE = DATA_COMPONENTS.registerComponentType(
            "release_pressure",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> LAST_UPDATED = DATA_COMPONENTS.registerComponentType(
            "last_updated",
            builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CAN_SMOKE = DATA_COMPONENTS.registerComponentType(
            "can_smoke",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HAS_LANDED = DATA_COMPONENTS.registerComponentType(
            "has_landed",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IS_WRAPPED = DATA_COMPONENTS.registerComponentType(
            "is_wrapped",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> ITEM_CONTAINER_CONTENTS = DATA_COMPONENTS.registerComponentType(
            "item_container_contents",
            builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<UUID>>> GERMS = DATA_COMPONENTS.registerComponentType(
            "germs",
            builder -> builder.persistent(Codec.list(UUIDUtil.CODEC)).networkSynchronized(ByteBufCodecs.collection(ArrayList::new, UUIDUtil.STREAM_CODEC)).cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ID_NUMBER = DATA_COMPONENTS.registerComponentType(
            "id_number",
            builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> POSITION_1 = DATA_COMPONENTS.registerComponentType(
            "position_1",
            builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> POSITION_2 = DATA_COMPONENTS.registerComponentType(
            "position_2",
            builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> TEMPERATURE = DATA_COMPONENTS.registerComponentType(
            "temperature",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );
}
