package com.site21.bittermelon.content.items.writingutensils;

import com.site21.bittermelon.content.blocks.wallwriting.WallWritingBlock;
import com.site21.bittermelon.content.blocks.wallwriting.WallWritingBlockEntity;
import com.site21.bittermelon.content.blocks.wallwriting.networking.OpenWallWritingScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.WALL_WRITING;
import static net.minecraft.world.level.block.Block.UPDATE_CLIENTS;

public abstract class WallWriterItem extends BlockItem implements WallWriter {
    public WallWriterItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null || !player.isShiftKeyDown()) return super.useOn(context);
        BlockState state = level.getBlockState(pos);

        if (!state.is(WALL_WRITING)) return super.useOn(context);
        if (level.isClientSide) return InteractionResult.CONSUME;

        if (level.getBlockEntity(pos) instanceof WallWritingBlockEntity wallWriting) {
            if (tryApplyToWall(level, wallWriting, player, context.getItemInHand())) {
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(context);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pos, @NotNull Level level, @Nullable Player player, @NotNull ItemStack stack, @NotNull BlockState state) {
        boolean shouldUpdate = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        if (!level.isClientSide && !shouldUpdate && player != null) {
            if (level.getBlockEntity(pos) instanceof WallWritingBlockEntity wallWriting) {
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new OpenWallWritingScreen(wallWriting.getBlockPos()));
                    formatText(wallWriting, stack);
                    level.sendBlockUpdated(pos, state, state, UPDATE_CLIENTS);
                }
            }
        }

        return shouldUpdate;
    }

    protected abstract void formatText(@NotNull WallWritingBlockEntity wallWriting, ItemStack stack);

    @Override
    public boolean tryApplyToWall(@NotNull Level level, @NotNull WallWritingBlockEntity wallWriting, @NotNull Player player, ItemStack stack) {
        if (player.isShiftKeyDown() && player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new OpenWallWritingScreen(wallWriting.getBlockPos()));
        } else {
            level.playSound(null, wallWriting.getBlockPos(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0f, 1.0f);
        }

        formatText(wallWriting, stack);

        return true;
    }
}
