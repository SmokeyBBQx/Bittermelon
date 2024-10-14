package com.site21.bittermelon.entities.behavior.needs;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.schedule.Activity;

import java.util.function.Function;

public record Need(EntityDataAccessor<Float> need, Activity activity, Function<Float, Float> priorityFunction) {
}
