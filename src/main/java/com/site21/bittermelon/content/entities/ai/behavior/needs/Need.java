package com.site21.bittermelon.content.entities.ai.behavior.needs;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.schedule.Activity;

import java.util.function.Function;
import java.util.function.Predicate;

public record Need<E extends LivingEntity & NeedsUser<E>>(
        EntityDataAccessor<Float> data,
        Activity activity,
        Function<Float, Float> priorityFunction,
        Predicate<E> canBeFulfilled) {
}
