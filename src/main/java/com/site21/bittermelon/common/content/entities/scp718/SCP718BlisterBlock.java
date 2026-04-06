package com.site21.bittermelon.common.content.entities.scp718;

import com.site21.bittermelon.common.events.CommonEvents;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.init.custom.Substances;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterEntities.SCP_718;

public class SCP718BlisterBlock extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    private static final VoxelShape SHAPE = Block.column(4.0, 0.0, 4.0);
    private static final float GROWTH_CHANCE = 0.5f;
    private static final int MAX_AGE = 2;

    public SCP718BlisterBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < GROWTH_CHANCE) {
            int age = state.getValue(AGE) + 1;
            if (age <= MAX_AGE) {
                level.setBlock(pos, state.setValue(AGE, age), 2);
            } else {
                level.removeBlock(pos, false);
                SCP718 scp718 = SCP_718.get().create(level, EntitySpawnReason.NATURAL);
                if (scp718 == null) return;

                scp718.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                level.addFreshEntity(scp718);

                if (level instanceof ServerLevel serverLevel) {
                    makeSoundAndParticles(serverLevel, pos, state);
                }
            }
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully()) {
            destroyBlister(level, pos, state);
        }

        super.stepOn(level, pos, state, entity);
    }

    private void destroyBlister(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        level.removeBlock(pos, false);
        List<SubstanceStack> substances = List.of(new SubstanceStack(Substances.EYEBALL_FLUID, 50));
        CommonEvents.drip(level, pos, substances);

        if (level instanceof ServerLevel serverLevel) {
            makeSoundAndParticles(serverLevel, pos, state);
        }
    }

    private void makeSoundAndParticles(ServerLevel level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, BitterSounds.SPLATTER.value(), SoundSource.BLOCKS, 1.0f,
                0.8f + level.random.nextFloat() * 0.4f);

        level.sendParticles(
                new BlockParticleOption(ParticleTypes.BLOCK_CRUMBLE, state),
                pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                10, 0.5, 0.5, 0.5, 0.0);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess scheduledTickAccess,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {
        return !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return pathComputationType == PathComputationType.AIR && !hasCollision || super.isPathfindable(state, pathComputationType);
    }
}
