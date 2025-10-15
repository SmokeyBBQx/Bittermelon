package com.site21.bittermelon.common.content.blocks.base;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IndentedSmallBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<IndentedSmallBlock> CODEC = simpleCodec(IndentedSmallBlock::new);
    private static final Map<Direction, VoxelShape> AABBS;

    public IndentedSmallBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABBS.get(state.getValue(FACING));
    }

    static {
        AABBS = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(4.0, 4.0, 12.0, 12.0, 12.0, 16.0),
                Direction.SOUTH, Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 4.0),
                Direction.EAST, Block.box(0.0, 4.0, 4.0, 4.0, 12.0, 12.0),
                Direction.WEST, Block.box(12.0, 4.0, 4.0, 16.0, 12.0, 12.0)
        ));
    }
}
