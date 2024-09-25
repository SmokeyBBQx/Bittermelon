package com.site21.bittermelon.init;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class MemoryModuleTypeInit {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, Bittermelon.MOD_ID);

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> EXHALE_COOLDOWN = MEMORY_MODULE_TYPES.register("exhale_cooldown",
            () -> new MemoryModuleType<>(Optional.of(Unit.CODEC)));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> LURE_COOLDOWN = MEMORY_MODULE_TYPES.register("lure_cooldown",
            () -> new MemoryModuleType<>(Optional.of(Unit.CODEC)));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> LISTEN_COOLDOWN = MEMORY_MODULE_TYPES.register("listen_cooldown",
            () -> new MemoryModuleType<>(Optional.of(Unit.CODEC)));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> IS_EXHALING = MEMORY_MODULE_TYPES.register("is_exhaling",
            () -> new MemoryModuleType<>(Optional.of(Unit.CODEC)));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> IS_LISTENING = MEMORY_MODULE_TYPES.register("is_listening",
            () -> new MemoryModuleType<>(Optional.of(Unit.CODEC)));

}
