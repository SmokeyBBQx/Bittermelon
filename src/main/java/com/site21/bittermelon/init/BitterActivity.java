package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.checkerframework.checker.units.qual.A;

public class BitterActivity {
    public static final DeferredRegister<Activity> ACTIVITY = DeferredRegister.create(BuiltInRegistries.ACTIVITY, Bittermelon.MOD_ID);

    public static final DeferredHolder<Activity, Activity> HUNT = ACTIVITY.register("hunt", () -> new Activity("hunt"));
    public static final DeferredHolder<Activity, Activity> PROCREATE = ACTIVITY.register("procreate", () -> new Activity("procreate"));
    public static final DeferredHolder<Activity, Activity> SOCIALIZE = ACTIVITY.register("socialize", () -> new Activity("socialize"));
    public static final DeferredHolder<Activity, Activity> MENTAL_BREAK = ACTIVITY.register("mental_break", () -> new Activity("mental_break"));
    public static final DeferredHolder<Activity, Activity> EAT = ACTIVITY.register("eat", () -> new Activity("eat"));
    public static final DeferredHolder<Activity, Activity> PHOTOSYNTHESIZE = ACTIVITY.register("photosynthesize", () -> new Activity("photosynthesize"));
    public static final DeferredHolder<Activity, Activity> DRINK = ACTIVITY.register("drink", () -> new Activity("drink"));
    public static final DeferredHolder<Activity, Activity> URINATE = ACTIVITY.register("urinate", () -> new Activity("urinate"));
    public static final DeferredHolder<Activity, Activity> DEFECATE = ACTIVITY.register("defecate", () -> new Activity("defecate"));
    public static final DeferredHolder<Activity, Activity> EXPLORE = ACTIVITY.register("explore", () -> new Activity("explore"));
    public static final DeferredHolder<Activity, Activity> GROOM = ACTIVITY.register("groom", () -> new Activity("groom"));
    public static final DeferredHolder<Activity, Activity> PLAY = ACTIVITY.register("play", () -> new Activity("play"));
}
