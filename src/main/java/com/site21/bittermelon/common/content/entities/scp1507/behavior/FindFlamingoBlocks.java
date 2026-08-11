package com.site21.bittermelon.common.content.entities.scp1507.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.scp1507.SCP1507;
import com.site21.bittermelon.init.neoforge.BitterBlocks;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtil;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.List;

public class FindFlamingoBlocks extends ExtendedBehaviour<SCP1507> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(SBLMemoryTypes.NEARBY_BLOCKS.get())
            .hasMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
            .usesMemory(MemoryModuleType.WALK_TARGET)
            .usesMemory(MemoryModuleType.LOOK_TARGET)
            .noMemory(BitterMemoryTypes.AWAKEN_TARGET.get());

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(SCP1507 entity) {
        var nearbyBlocks = entity.getBrain().getMemory(SBLMemoryTypes.NEARBY_BLOCKS.get()).orElse(null);
        if (nearbyBlocks == null) return;

        for (Pair<BlockPos, BlockState> block : nearbyBlocks) {
            if (block.getSecond().is(BitterBlocks.PLASTIC_FLAMINGO)) {
                BlockPos pos = block.getFirst();
                setTarget(entity, pos);
                alertAllies(pos, entity);
                return;
            }
        }
    }

    private void alertAllies(BlockPos pos, SCP1507 owner) {
        for (SCP1507 ally : EntityRetrievalUtil.getEntities(owner, 30, 10, 30, SCP1507.class)) {
            setTarget(ally, pos);
        }
    }

    private void setTarget(SCP1507 entity, BlockPos pos) {
        BrainUtil.setMemory(entity, BitterMemoryTypes.AWAKEN_TARGET.get(), pos);
        BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 1, 3));
        BrainUtil.setMemory(entity, MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pos));
    }
}
