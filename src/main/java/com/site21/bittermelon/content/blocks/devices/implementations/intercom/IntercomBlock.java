package com.site21.bittermelon.content.blocks.devices.implementations.intercom;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.client.IntercomScreen;
import com.site21.bittermelon.content.items.IntercomPhoneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterItems.INTERCOM_PHONE;
import static com.site21.bittermelon.init.neoforge.BitterItems.ITEMS;

public class IntercomBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> AABBS;

    public IntercomBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (player.isCrouching()) {
            if (!level.isClientSide) {
                if (level.getBlockEntity(pos) instanceof IntercomBlockEntity intercom) {
                    if (intercom.isPhonePickedUp()) {
                        player.sendSystemMessage(Component.literal("Someone has already picked up the phone.").withStyle(ChatFormatting.RED));
                        return InteractionResult.FAIL;
                    }
                    ItemStack phone = new ItemStack(INTERCOM_PHONE.get());
                    phone.set(CORD_CONNECTION.get(), pos);
                    player.setItemInHand(InteractionHand.MAIN_HAND, phone);
                    player.sendSystemMessage(Component.literal("You pick up the phone.").withStyle(ChatFormatting.GRAY));
                    intercom.setPhonePickedUp(true);
                }
            }
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }

        if (level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof IntercomBlockEntity intercomBlockEntity) {
                Minecraft.getInstance().setScreen(new IntercomScreen(intercomBlockEntity, true));
            }
        }

        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (stack.getCount() < 1) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (level.isClientSide) return ItemInteractionResult.FAIL;

        if (stack.getItem() instanceof IntercomPhoneItem) {
            stack.setCount(0);
            player.sendSystemMessage(Component.literal("You place the phone back.").withStyle(ChatFormatting.GRAY));
            if (level.getBlockEntity(pos) instanceof IntercomBlockEntity intercom) {
                intercom.setPhonePickedUp(false);
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (player.isCrouching() && stack.getCount() > 0) {
            player.sendSystemMessage(Component.literal("You must have an empty hand to do that.").withStyle(ChatFormatting.RED));
            return ItemInteractionResult.FAIL;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new IntercomBlockEntity(blockPos, blockState);
    }

    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABBS.get(state.getValue(FACING));
    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState blockstate = super.getStateForPlacement(context);
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction[] adirection = context.getNearestLookingDirections();

        for (Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction1 = direction.getOpposite();
                assert blockstate != null;
                blockstate = blockstate.setValue(FACING, direction1);
                if (!blockgetter.getBlockState(blockpos.relative(direction)).canBeReplaced(context)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    protected @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    protected @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        AABBS = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(4.0, 4.0, 12.0, 12.0, 12.0, 16.0),
                Direction.SOUTH, Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 4.0),
                Direction.EAST, Block.box(0.0, 4.0, 4.0, 4.0, 12.0, 12.0),
                Direction.WEST, Block.box(12.0, 4.0, 4.0, 16.0, 12.0, 12.0)
        ));
    }
}
