package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ActivityInit {
    public static final DeferredRegister<Activity> ACTIVITY = DeferredRegister.create(BuiltInRegistries.ACTIVITY, Bittermelon.MOD_ID);

    public static final DeferredHolder<Activity, Activity> LURE = ACTIVITY.register("lure", () -> new Activity("lure"));
    public static final DeferredHolder<Activity, Activity> LISTEN = ACTIVITY.register("listen", () -> new Activity("listen"));
    public static final DeferredHolder<Activity, Activity> EXHALE = ACTIVITY.register("exhale", () -> new Activity("exhale"));
}
