package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.items.containers.substancecontainers.SubstanceContainerData;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentsInit {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Bittermelon.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SubstanceContainerData>> SUBSTANCE_CONTAINER_DATA = DATA_COMPONENTS.registerComponentType(
            "substance_container_data",
            builder -> builder.persistent(SubstanceContainerData.CODEC).networkSynchronized(SubstanceContainerData.STREAM_CODEC).cacheEncoding()
    );
}
