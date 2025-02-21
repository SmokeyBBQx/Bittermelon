package com.site21.bittermelon.init.neoforge;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

public class BitterMemoryTypes {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, Bittermelon.MOD_ID);

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> SOCIALIZE_TARGET = MEMORY_MODULE_TYPES.register("socialize_target",
            () -> new MemoryModuleType<>(Optional.empty()));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<List<Activity>>> EXCLUDED_ACTIVITIES = MEMORY_MODULE_TYPES.register("excluded_activities",
            () -> new MemoryModuleType<>(Optional.empty()));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> HAS_MENTAL_BREAK = MEMORY_MODULE_TYPES.register("has_mental_break",
            () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<List<ItemEntity>>> NEARBY_EDIBLE_ITEMS = MEMORY_MODULE_TYPES.register("nearby_edible_items",
            () -> new MemoryModuleType<>(Optional.empty()));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<List<FluidBlockEntity>>> NEARBY_DRINKABLE_FLUIDS = MEMORY_MODULE_TYPES.register("nearby_drinkable_fluids",
            () -> new MemoryModuleType<>(Optional.empty()));
}
