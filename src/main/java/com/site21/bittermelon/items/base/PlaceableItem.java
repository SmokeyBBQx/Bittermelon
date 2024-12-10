package com.site21.bittermelon.items.base;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PlaceableItem extends BaseItem {
    private final Block block;

    public PlaceableItem(Properties properties, int width, int height, ItemWeight itemWeight, Block block) {
        super(properties, width, height, itemWeight);
        this.block = block;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        InteractionResult interactionResult = this.place(new BlockPlaceContext(context));
        if (!interactionResult.consumesAction()) {
            InteractionResult interactionResult1 = super.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
            return interactionResult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionResult1;
        } else {
            return interactionResult;
        }
    }


    public InteractionResult place(BlockPlaceContext context) {
        if (!this.getBlock().isEnabled(context.getLevel().enabledFeatures())) {
            return InteractionResult.FAIL;
        }

        if (!context.canPlace()) {
            return InteractionResult.FAIL;
        }

        BlockPlaceContext blockPlaceContext = this.updatePlacementContext(context);

        if (blockPlaceContext == null) {
            return InteractionResult.FAIL;
        }

        BlockState blockState = this.getPlacementState(blockPlaceContext);
        if (blockState == null) {
            return InteractionResult.FAIL;
        }

        if (!this.placeBlock(blockPlaceContext, blockState)) {
            return InteractionResult.FAIL;
        }

        BlockPos blockPos = blockPlaceContext.getClickedPos();
        Level level = blockPlaceContext.getLevel();
        Player player = blockPlaceContext.getPlayer();
        ItemStack itemStack = blockPlaceContext.getItemInHand();
        BlockState blockState1 = level.getBlockState(blockPos);

        if (blockState1.is(blockState.getBlock())) {
            blockState1 = this.updateBlockStateFromTag(blockPos, level, itemStack, blockState1);
            this.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState1);
            updateBlockEntityComponents(level, blockPos, itemStack);
            blockState1.getBlock().setPlacedBy(level, blockPos, blockState1, player, itemStack);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockPos, itemStack);
            }
        }

        SoundType soundtype = blockState1.getSoundType(level, blockPos, context.getPlayer());
        level.playSound(
                player,
                blockPos,
                this.getPlaceSound(blockState1, level, blockPos, context.getPlayer()),
                SoundSource.BLOCKS,
                (soundtype.getVolume() + 1.0F) / 2.0F,
                soundtype.getPitch() * 0.8F
        );
        level.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(player, blockState1));
        itemStack.consume(1, player);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }


    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        return context;
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockstate = this.getBlock().getStateForPlacement(context);
        return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
    }

    protected SoundEvent getPlaceSound(BlockState p_state, Level world, BlockPos pos, Player entity) {
        return p_state.getSoundType(world, pos, entity).getPlaceSound();
    }

    private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
        BlockItemStateProperties blockitemstateproperties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        if (blockitemstateproperties.isEmpty()) {
            return state;
        } else {
            BlockState blockstate = blockitemstateproperties.apply(state);
            if (blockstate != state) {
                level.setBlock(pos, blockstate, 2);
            }

            return blockstate;
        }
    }

    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        Player player = context.getPlayer();
        CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return (!this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos()))
                && context.getLevel().isUnobstructed(state, context.getClickedPos(), collisioncontext);
    }

    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return context.getLevel().setBlock(context.getClickedPos(), state, 11);
    }

    private static void updateBlockEntityComponents(Level level, BlockPos poa, ItemStack stack) {
        BlockEntity blockentity = level.getBlockEntity(poa);
        if (blockentity != null) {
            blockentity.applyComponentsFromItemStack(stack);
            blockentity.setChanged();
        }
    }

    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        return updateCustomBlockEntityTag(level, player, pos, stack);
    }

    public static boolean updateCustomBlockEntityTag(Level level, @Nullable Player player, BlockPos pos, ItemStack stack) {
        MinecraftServer minecraftserver = level.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            CustomData customdata = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
            if (!customdata.isEmpty()) {
                BlockEntity blockentity = level.getBlockEntity(pos);
                if (blockentity != null) {
                    if (level.isClientSide || !blockentity.onlyOpCanSetNbt() || player != null && player.canUseGameMasterBlocks()) {
                        return customdata.loadInto(blockentity, level.registryAccess());
                    }

                    return false;
                }
            }

            return false;
        }
    }

    protected boolean mustSurvive() {
        return true;
    }

    public Block getBlock() {
        return this.block;
    }
}
