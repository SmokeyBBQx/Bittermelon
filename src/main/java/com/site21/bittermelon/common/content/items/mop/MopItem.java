package com.site21.bittermelon.common.content.items.mop;

import com.site21.bittermelon.common.content.blocks.substance.fluid.FluidBlock;
import com.site21.bittermelon.common.content.blocks.substance.fluid.FluidBlockEntity;
import com.site21.bittermelon.common.content.items.base.ItemWeight;
import com.site21.bittermelon.common.content.items.substance.FluidContainerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MopItem extends FluidContainerItem {
    public MopItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight, false);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        ItemStack offhandItem = player.getOffhandItem();

        if (isContainerEmpty(itemInHand)) {
            return InteractionResultHolder.pass(itemInHand);
        }

        if (usedHand == InteractionHand.MAIN_HAND && offhandItem.getItem() instanceof FluidContainerItem) {
            if (!level.isClientSide) {
                transferSubstancesToContainer(itemInHand, offhandItem, level, player);
            }
        }

        return InteractionResultHolder.success(itemInHand);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();

        if (!level.isClientSide && player != null) {
            if (canMopAt(level, stack, clickedPos)) {
                player.startUsingItem(context.getHand());
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        BlockPos targetPos = getTargetBlockPos(entity);

        if (targetPos == null) return (int) getLimitedTransferRate(stack);

        int useDuration = 10;

        if (entity.level().getBlockEntity(targetPos) instanceof FluidBlockEntity fluid) {
            useDuration = (int) Math.min(getTransferRate(stack), fluid.getTotalVolume());
        } else if (!isContainerEmpty(stack)) {
            useDuration = (int) getLimitedTransferRate(stack);
        }

        return Math.max(10, useDuration);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BRUSH;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (entity instanceof Player player && !level.isClientSide) {
            BlockPos targetPos = getTargetBlockPos(player);

            if (targetPos != null) {
                if (level.getBlockState(targetPos).getBlock() instanceof FluidBlock) {
                    if (getTotalVolume(stack) >= getCapacity(stack)) {
                        handleSpillAction(targetPos, level, player, stack);
                    } else {
                        transferSubstancesFromBlock(targetPos, level, stack);
                    }
                } else if (!isContainerEmpty(stack)) {
                    handleSpillAction(targetPos, level, player, stack);
                }
            }
        }

        return stack;
    }

    private boolean canMopAt(@NotNull Level level, ItemStack stack, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof FluidBlock || !isContainerEmpty(stack);
    }

    private @Nullable BlockPos getTargetBlockPos(@NotNull LivingEntity entity) {
        var hitResult = entity.pick(entity.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), 0.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) hitResult).getBlockPos();
        }
        return null;
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration % 8 == 0) {
            entity.level().playSound(null, entity.getOnPos(), SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS,
                    0.3F, 1.0F + (entity.getRandom().nextFloat() * 0.4F));
        }
    }

    @Override
    protected void playEmptySound(@NotNull Level level, BlockPos pos) {
        level.playSound(null, pos,
                SoundEvents.SPONGE_ABSORB, SoundSource.PLAYERS, 0.5F, 1.5F);
    }

    @Override
    protected void playFillSound(@NotNull Level level, BlockPos pos) {
        level.playSound(null, pos,
                SoundEvents.SPONGE_ABSORB, SoundSource.PLAYERS, 0.5F, 1.0F);
    }
}