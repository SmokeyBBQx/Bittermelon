package com.site21.bittermelon.common.content.entities.scp1507.behavior;

import com.site21.bittermelon.common.content.entities.scp1507.SCP1507;
import com.site21.bittermelon.init.neoforge.BitterBlocks;
import com.site21.bittermelon.init.neoforge.BitterEntities;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.base.DelayedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.Set;

import static com.site21.bittermelon.init.neoforge.BitterSounds.FLAMINGO_HONK;

public class AwakenFlamingoBlocks extends DelayedBehaviour<SCP1507> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(BitterMemoryTypes.AWAKEN_TARGET.get())
            .usesMemories(MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET);
    private BlockPos pos;

    public AwakenFlamingoBlocks() {
        super(120);
    }

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, SCP1507 entity) {
        pos = BrainUtil.getMemory(entity, BitterMemoryTypes.AWAKEN_TARGET.get());
        return level.getBlockState(pos).is(BitterBlocks.PLASTIC_FLAMINGO) && entity.distanceToSqr(Vec3.atCenterOf(pos)) <= 10;
    }

    @Override
    protected void start(SCP1507 entity) {
        entity.resetAwakenTime();
    }

    @Override
    protected void tick(SCP1507 entity) {
        if (entity.getRandom().nextFloat() < 0.05f) {
            entity.playSound(FLAMINGO_HONK.value(), 0.6f, entity.getRandom().nextFloat() * 0.2f + 0.8f);
        }
    }

    @Override
    protected void doDelayedAction(SCP1507 entity) {
        if (!entity.level().getBlockState(pos).is(BitterBlocks.PLASTIC_FLAMINGO)) return;

        if (entity.level() instanceof ServerLevel level) {
            float rotation = level.getBlockState(pos).getValue(BlockStateProperties.ROTATION_16) * -22.5f;
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            SCP1507 summoned = BitterEntities.SCP_1507.get().spawn(level, pos, EntitySpawnReason.MOB_SUMMONED);
            assert summoned != null;
            summoned.setYRot(rotation);
        }
    }
}
