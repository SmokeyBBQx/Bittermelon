package com.site21.bittermelon.content.blocks.devices.implementations.intercom;

import com.site21.bittermelon.content.blocks.base.IndentedSmallBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.client.IntercomScreen;
import com.site21.bittermelon.content.items.IntercomPhoneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterItems.INTERCOM_PHONE;

public class IntercomBlock extends IndentedSmallBlock implements EntityBlock {

    public IntercomBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (player.isCrouching()) {
            if (!level.isClientSide) {
                if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) > 2 * 2) {
                    player.sendSystemMessage(Component.literal("Too far away to pick up the phone.").withStyle(ChatFormatting.RED));
                    return InteractionResult.FAIL;
                }

                if (level.getBlockEntity(pos) instanceof IntercomBlockEntity intercom) {
                    if (intercom.isPhonePickedUp()) {
                        player.sendSystemMessage(Component.literal("Someone has already picked up the phone.").withStyle(ChatFormatting.RED));
                        return InteractionResult.FAIL;
                    }
                    ItemStack phone = new ItemStack(INTERCOM_PHONE.get());
                    phone.set(CORD_CONNECTION.get(), pos);
                    player.setItemInHand(InteractionHand.MAIN_HAND, phone);
                    player.sendSystemMessage(Component.literal("You pick up the phone.").withStyle(ChatFormatting.GRAY));
                    level.playSound(null, pos, SoundEvents.HEAVY_CORE_HIT, SoundSource.PLAYERS);
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
            level.playSound(null, pos, SoundEvents.HEAVY_CORE_HIT, SoundSource.PLAYERS);
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
}
