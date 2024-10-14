package com.site21.bittermelon.entities.brain;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrain;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NeedsBrain<E extends LivingEntity & SmartBrainOwner<E> & NeedsBrainOwner> extends SmartBrain<E> {
    public NeedsBrain(List<MemoryModuleType<?>> memories, List<? extends ExtendedSensor<E>> extendedSensors, @Nullable List<BrainActivityGroup<E>> taskList) {
        super(memories, extendedSensors, taskList);
    }
    

}
