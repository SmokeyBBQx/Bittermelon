package com.site21.bittermelon.init.neoforge;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.base.ItemSize;
import com.site21.bittermelon.common.content.items.scps.scp377.Fortune;
import com.site21.bittermelon.common.content.items.substance.data.SubstanceContents;
import com.site21.bittermelon.common.content.items.substance.pill.PillShape;
import com.site21.bittermelon.common.systems.medical.blood.BloodData;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentData;
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
            builder -> builder.persistent(Codec.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HAS_LANDED = DATA_COMPONENTS.registerComponentType(
            "has_landed",
            builder -> builder.persistent(Codec.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IS_WRAPPED = DATA_COMPONENTS.registerComponentType(
            "is_wrapped",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
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
            builder -> builder.persistent(Codec.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COOLDOWN = DATA_COMPONENTS.registerComponentType(
            "cooldown",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> LIT = DATA_COMPONENTS.registerComponentType(
            "lit",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Integer>>> TASE_PROBES = DATA_COMPONENTS.registerComponentType(
            "tase_probes",
            builder -> builder.persistent(Codec.list(Codec.INT)).networkSynchronized(ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.INT))
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> RELOAD_TIMER = DATA_COMPONENTS.registerComponentType(
            "reload_timer",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> AMMO = DATA_COMPONENTS.registerComponentType(
            "ammo",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompartmentData>> COMPARTMENT = DATA_COMPONENTS.registerComponentType(
            "compartment",
            builder -> builder.persistent(CompartmentData.CODEC).networkSynchronized(CompartmentData.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> ENERGY_LOSS_ON_BOUNCE = DATA_COMPONENTS.registerComponentType(
            "energy_loss_on_bounce",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MAX_BOUNCES = DATA_COMPONENTS.registerComponentType(
            "max_bounces",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BloodData>> BLOOD_DATA = DATA_COMPONENTS.registerComponentType(
            "blood_data",
            builder -> builder.persistent(BloodData.CODEC).networkSynchronized(BloodData.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COLOR = DATA_COMPONENTS.registerComponentType(
            "color",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PillShape>> PILL_SHAPE = DATA_COMPONENTS.registerComponentType(
            "pill_shape",
            builder -> builder.persistent(PillShape.CODEC).networkSynchronized(PillShape.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Fortune>> FORTUNE = DATA_COMPONENTS.registerComponentType(
            "fortune",
            builder -> builder.persistent(Fortune.CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> FORTUNE_READ = DATA_COMPONENTS.registerComponentType(
            "fortune_read",
            builder -> builder.persistent(Codec.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COOKIE_COUNT = DATA_COMPONENTS.registerComponentType(
            "cookie_count",
            builder -> builder.persistent(Codec.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> EMPTY_TIME = DATA_COMPONENTS.registerComponentType(
            "empty_time",
            builder -> builder.persistent(Codec.LONG)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> MESSAGE = DATA_COMPONENTS.registerComponentType(
            "message",
            builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> VOLUME = DATA_COMPONENTS.registerComponentType(
            "volume",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MAX_TRANSFER_RATE = DATA_COMPONENTS.registerComponentType(
            "max_transfer_rate",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> MAX_PRESSURE = DATA_COMPONENTS.registerComponentType(
            "max_pressure",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> WEIGHT = DATA_COMPONENTS.registerComponentType(
            "weight",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemSize>> SIZE = DATA_COMPONENTS.registerComponentType(
            "size",
            builder -> builder.persistent(ItemSize.CODEC).networkSynchronized(ItemSize.STREAM_CODEC)
    );
}
