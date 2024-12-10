package com.site21.bittermelon.init;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.items.containers.item.ItemContainerContents;
import com.site21.bittermelon.items.containers.substance.SubstanceContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

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
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> LAST_UPDATED = DATA_COMPONENTS.registerComponentType(
            "last_updated",
            builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CAN_SMOKE = DATA_COMPONENTS.registerComponentType(
            "can_smoke",
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
}
