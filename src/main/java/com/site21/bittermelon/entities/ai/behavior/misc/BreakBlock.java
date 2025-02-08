package com.site21.bittermelon.entities.ai.behavior.misc;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.blocks.StructuralBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;

import java.util.List;

public class BreakBlock<E extends LivingEntity> extends net.tslat.smartbrainlib.api.core.behaviour.custom.misc.BreakBlock<E> {
    @Override
    protected void tick(E entity) {
        if (!(state.getBlock() instanceof StructuralBlock)) {
            super.tick(entity);
        }
        
        
    }
}
