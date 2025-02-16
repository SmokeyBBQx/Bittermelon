package com.site21.bittermelon.content.entities.ai.behavior.misc;

import com.site21.bittermelon.content.blocks.base.structuralblock.StructuralBlock;
import net.minecraft.world.entity.LivingEntity;

public class BreakBlock<E extends LivingEntity> extends net.tslat.smartbrainlib.api.core.behaviour.custom.misc.BreakBlock<E> {
    @Override
    protected void tick(E entity) {
        if (!(state.getBlock() instanceof StructuralBlock)) {
            super.tick(entity);
        }
        
        
    }
}
