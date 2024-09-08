//package net.smokeybbq.bittermelon.blocks;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.LevelAccessor;
//import net.minecraft.world.level.block.*;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import net.minecraft.world.level.block.state.properties.BooleanProperty;
//import net.minecraft.world.level.block.state.properties.DirectionProperty;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.shapes.VoxelShape;
//import net.smokeybbq.bittermelon.blocks.blockentities.PlacedItemBlockEntity;
//import org.jetbrains.annotations.Nullable;
//
//public class PlacedItemBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
//    public static final VoxelShape[] ITEM_SHAPES = {
//            Block.box(0, 0, 0, 8, 8, 8),
//            Block.box(8, 0, 0, 16, 8, 8),
//            Block.box(0, 0, 8, 8, 8, 16),
//            Block.box(8, 0, 8, 16, 8, 16)
//    };
//    public static final DirectionProperty FACING = DirectionalBlock.FACING;
//    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
//    protected PlacedItemBlock(Properties pProperties) {
//        super(pProperties);
//    }
//
//    @Override
//    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
//        if (stateIn.getValue(WATERLOGGED)) {
//            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
//        }
//
//        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
//    }
//
//    @Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
//        return new
//    }
//
//    public int getSlotFromHitResult(BlockState state, BlockHitResult hit) {
//    }
//
//}
