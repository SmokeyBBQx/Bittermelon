package com.site21.bittermelon.entities.brain;

import com.site21.bittermelon.entities.behavior.needs.Need;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Map;

public interface NeedsBrainOwner {
    public List<Need> getNeeds();
}
