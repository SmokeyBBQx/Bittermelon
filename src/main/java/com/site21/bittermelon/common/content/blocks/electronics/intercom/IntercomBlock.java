package com.site21.bittermelon.common.content.blocks.electronics.intercom;

import com.site21.bittermelon.common.content.blocks.base.IndentedSmallBlock;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.networking.OpenIntercomScreen;
import com.site21.bittermelon.common.content.items.IntercomPhoneItem;
import com.site21.bittermelon.common.systems.telecomms.intercom.IntercomManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
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
        if (level.isClientSide) return InteractionResult.PASS;

        if (player.isCrouching()) {

            if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) > 2 * 2) {
                player.displayClientMessage(Component.literal("Too far away to pick up the phone.").withStyle(ChatFormatting.RED), true);
                return InteractionResult.FAIL;
            }

            if (level.getBlockEntity(pos) instanceof IntercomBlockEntity intercom) {
                if (intercom.isPhonePickedUp()) {
                    player.displayClientMessage(Component.literal("Someone has already picked up the phone.").withStyle(ChatFormatting.RED), true);
                    return InteractionResult.FAIL;
                }

                ItemStack phone = INTERCOM_PHONE.toStack();
                intercom.setPhoneUser(player);
                phone.set(CORD_CONNECTION.get(), pos);
                player.setItemInHand(InteractionHand.MAIN_HAND, phone);
                player.displayClientMessage(Component.literal("You pick up the phone.").withStyle(ChatFormatting.GRAY), true);
                level.playSound(null, pos, SoundEvents.HEAVY_CORE_HIT, SoundSource.PLAYERS);
                intercom.setPhonePickedUp(true);
            }

            return InteractionResult.SUCCESS;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new OpenIntercomScreen(pos));
        }

        return InteractionResult.SUCCESS;
    }

    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (stack.getCount() < 1) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (level.isClientSide) return InteractionResult.FAIL;

        if (stack.getItem() instanceof IntercomPhoneItem) {
            stack.setCount(0);
            player.displayClientMessage(Component.literal("You place the phone back.").withStyle(ChatFormatting.GRAY), true);
            level.playSound(null, pos, SoundEvents.HEAVY_CORE_HIT, SoundSource.PLAYERS);
            if (level.getBlockEntity(pos) instanceof IntercomBlockEntity intercom) {
                intercom.setPhonePickedUp(false);
                intercom.setPhoneUser(null);
            }

            return InteractionResult.SUCCESS;
        }

        if (player.isCrouching() && stack.getCount() > 0) {
            player.displayClientMessage(Component.literal("You must have an empty hand to do that.").withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof IntercomBlockEntity intercom) {
            intercom.clearElectronicData(level);
            IntercomManager.get(level).removeIntercom(pos);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new IntercomBlockEntity(blockPos, blockState);
    }
}
