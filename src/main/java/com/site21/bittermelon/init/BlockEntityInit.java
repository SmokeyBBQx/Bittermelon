package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.blocks.blockentities.FluidBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.BlockInit.FLUID;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bittermelon.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidBlockEntity>> FLUID_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("fluid_block_entity",
            () -> BlockEntityType.Builder.of(FluidBlockEntity::new, FLUID.get()).build(null));
}
