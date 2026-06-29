package com.site21.bittermelon.common.content.items.writingutensils;

import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlockEntity;
import com.site21.bittermelon.common.content.blocks.wallwriting.networking.OpenWallWritingScreen;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.WALL_WRITING;
import static net.minecraft.world.level.block.Block.UPDATE_CLIENTS;

public abstract class WallWriterItem extends BlockItem implements WallWriter {
    public WallWriterItem(Block block, Properties properties) {
        super(block, properties);
    }

    // Open the writing screen when shift-right-clicking a wall writing block
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return super.useOn(context);
        BlockState state = level.getBlockState(pos);

        if (!state.is(WALL_WRITING)) {
            return super.useOn(context);
        }

        if (level.getBlockEntity(pos) instanceof WallWritingBlockEntity wallWriting) {
            if (!level.isClientSide()) {
                if (tryApplyToWall(level, wallWriting, player, context.getItemInHand())) {
                    level.sendBlockUpdated(pos, state, state, UPDATE_CLIENTS);
                }
            } else if (!player.isShiftKeyDown()) {
                return InteractionResult.PASS;
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    // Make sure to open the writing screen when placing the block if needed
    @Override
    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pos, @NotNull Level level, @Nullable Player player, @NotNull ItemStack stack, @NotNull BlockState state) {
        boolean shouldUpdate = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        if (!level.isClientSide() && !shouldUpdate && player != null) {
            if (level.getBlockEntity(pos) instanceof WallWritingBlockEntity wallWriting) {
                if (player instanceof ServerPlayer serverPlayer) {
                    formatText(wallWriting, stack);
                    PacketDistributor.sendToPlayer(serverPlayer, new OpenWallWritingScreen(wallWriting.getBlockPos(),
                            wallWriting.getText().getColor().getId(),
                            wallWriting.getText().hasGlowingText()
                            ));
                }
            }
        }

        return shouldUpdate;
    }

    protected abstract void formatText(@NotNull WallWritingBlockEntity wallWriting, ItemStack stack);

    // Apply the text to the wall writing block entity and open the writing screen
    @Override
    public boolean tryApplyToWall(@NotNull Level level, @NotNull WallWritingBlockEntity wallWriting, @NotNull Player player, ItemStack stack) {
        if (player.isShiftKeyDown()) {
            formatText(wallWriting, stack);
            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new OpenWallWritingScreen(wallWriting.getBlockPos(),
                        wallWriting.getText().getColor().getId(),
                        wallWriting.getText().hasGlowingText()
                        ));
            }
            return true;
        }

        return false;
    }

    /**
     *  Same as in BlockItem, except it takes durability instead of consuming the item
     */
    @Override
    public @NotNull InteractionResult place(BlockPlaceContext context) {
        if (!this.getBlock().isEnabled(context.getLevel().enabledFeatures())) {
            return InteractionResult.FAIL;
        } else if (!context.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(context);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockplacecontext, blockstate)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                        updateBlockEntityComponents(level, blockpos, itemstack);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                        }
                    }

                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, context.getPlayer());
                    level.playSound(
                            player,
                            blockpos,
                            this.getPlaceSound(blockstate1, level, blockpos, context.getPlayer()),
                            SoundSource.BLOCKS,
                            (soundtype.getVolume() + 1.0F) / 2.0F,
                            soundtype.getPitch() * 0.8F
                    );
                    level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                    if (!player.hasInfiniteMaterials()) {
                        context.getItemInHand().setDamageValue(context.getItemInHand().getDamageValue() + 1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    // Clone from BlockItem so place override works
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

    // Clone from BlockItem so place override works
    private static void updateBlockEntityComponents(Level level, BlockPos pos, ItemStack stack) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity != null) {
            blockentity.applyComponentsFromItemStack(stack);
            blockentity.setChanged();
        }
    }
}
