package com.site21.bittermelon.init;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

public class BitterMemoryModuleType {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, Bittermelon.MOD_ID);

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> ACTION_COOLDOWN = MEMORY_MODULE_TYPES.register("action_cooldown",
            () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> HAS_REACHED_NEAREST_PLAYER = MEMORY_MODULE_TYPES.register("has_reached_nearest_player",
            () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> IS_EXHALING = MEMORY_MODULE_TYPES.register("is_exhaling",
            () -> new MemoryModuleType<>(Optional.of(Unit.CODEC)));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> IS_LISTENING = MEMORY_MODULE_TYPES.register("is_listening",
            () -> new MemoryModuleType<>(Optional.of(Unit.CODEC)));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> SOCIALIZE_TARGET = MEMORY_MODULE_TYPES.register("socialize_target",
            () -> new MemoryModuleType<>(Optional.empty()));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<List<Activity>>> EXCLUDED_ACTIVITIES = MEMORY_MODULE_TYPES.register("excluded_activities",
            () -> new MemoryModuleType<>(Optional.empty()));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> HAS_MENTAL_BREAK = MEMORY_MODULE_TYPES.register("has_mental_break",
            () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));
}
