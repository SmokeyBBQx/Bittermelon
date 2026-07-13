package com.site21.bittermelon.common.content.entities.scp1507.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.scp1507.SCP1507;
import com.site21.bittermelon.init.neoforge.BitterBlocks;
import com.site21.bittermelon.init.neoforge.BitterEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterSounds.FLAMINGO_HONK;

public class AwakenFlamingoBlocks extends ExtendedBehaviour<SCP1507> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(SBLMemoryTypes.NEARBY_BLOCKS.get())
            .usesMemory(MemoryModuleType.LOOK_TARGET);

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(SCP1507 entity) {
        var nearbyBlocks = entity.getBrain().getMemory(SBLMemoryTypes.NEARBY_BLOCKS.get()).orElse(null);
        if (nearbyBlocks == null) return;

        for (Pair<BlockPos, BlockState> block : nearbyBlocks) {
            BlockPos pos = block.getFirst();
            BlockState state = block.getSecond();
            if (state.is(BitterBlocks.PLASTIC_FLAMINGO)) {
                BrainUtil.setMemory(entity, MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pos));
                entity.playSound(FLAMINGO_HONK.value(), 0.8f, entity.getRandom().nextFloat() * 0.2f + 0.8f);
                if (entity.level() instanceof ServerLevel level) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    SCP1507 summoned = BitterEntities.SCP_1507.get().spawn(level, pos, EntitySpawnReason.MOB_SUMMONED);
                    float rotation = state.getValue(BlockStateProperties.ROTATION_16) * -22.5f;
                    assert summoned != null;
                    summoned.setYRot(rotation);
                }
                return;
            }
        }
    }
}
