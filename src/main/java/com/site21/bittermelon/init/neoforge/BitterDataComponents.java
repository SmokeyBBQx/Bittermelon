package com.site21.bittermelon.init.neoforge;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.itemcontainers.client.ItemContainerContents;
import com.site21.bittermelon.content.items.substance.data.SubstanceContents;
import com.site21.bittermelon.content.medical.compartments.CompartmentData;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
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

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> CORD_CONNECTION = DATA_COMPONENTS.registerComponentType(
            "cord_connection",
            builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> PORT_ID = DATA_COMPONENTS.registerComponentType(
            "port_id",
            builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ROTATION = DATA_COMPONENTS.registerComponentType(
            "rotation",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CAN_SPILL = DATA_COMPONENTS.registerComponentType(
            "can_spill",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COOLDOWN = DATA_COMPONENTS.registerComponentType(
            "cooldown",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> LIT = DATA_COMPONENTS.registerComponentType(
            "lit",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> LOADED = DATA_COMPONENTS.registerComponentType(
            "loaded",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompartmentData>> COMPARTMENT = DATA_COMPONENTS.registerComponentType(
            "compartment",
            builder -> builder.persistent(CompartmentData.CODEC).networkSynchronized(CompartmentData.STREAM_CODEC)
    );
}
