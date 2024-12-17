package com.site21.bittermelon.items.containers.substance;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.blocks.FluidBlock;
import com.site21.bittermelon.blocks.blockentities.FluidBlockEntity;
import com.site21.bittermelon.init.BitterDataComponents;
import com.site21.bittermelon.items.base.ItemSize;
import com.site21.bittermelon.items.base.ItemWeight;
import com.site21.bittermelon.networking.server.TransferRateUpdate;
import com.site21.bittermelon.substance.SubstanceStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

import static com.site21.bittermelon.init.BitterBlocks.FLUID;

public class FluidContainerItem extends SubstanceContainerItem {
    public static final int MIN_TRANSFER_RATE = 1;
    public static final int MAX_TRANSFER_RATE = 100;
    private static final int DRINK_SPEED = 32;

    public FluidContainerItem(Properties properties, int width, int height, ItemWeight itemWeight, int capacity) {
        super(properties, width, height, itemWeight, capacity);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("Contents: " + getSubstanceData(stack).getTotalVolume() + "/" + capacity));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        // TODO: Figure out a way to make use and useOn not overlap

        ItemStack itemInHand = player.getItemInHand(usedHand);
        ItemStack offhandItem = player.getOffhandItem();

        if (isContainerEmpty(itemInHand)) {
            playEmptySound(level, player.getOnPos());
            return InteractionResultHolder.fail(player.getMainHandItem());
        }

        if (usedHand == InteractionHand.MAIN_HAND && offhandItem.getItem() instanceof FluidContainerItem) {
            if (!level.isClientSide) {
                if (player.isShiftKeyDown()) {
                    transferSubstancesToContainer(itemInHand, offhandItem, level, player);
                }
            }
        } else {
            playDrinkSound(level, player.getOnPos());

            return ItemUtils.startUsingInstantly(level, player, usedHand);
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();

        if (!level.isClientSide && player != null) {
            if (!isContainerEmpty(stack)) {
                if (player.isShiftKeyDown()) {
                    return handleSpillAction(clickedPos, level, player, stack);
                }
            }

            if (level.getBlockState(clickedPos).getBlock() instanceof FluidBlock) {
                transferSubstancesFromBlock(clickedPos, level, stack);
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(context);
    }

    private InteractionResult handleSpillAction(@NotNull BlockPos clickedOnPos, @NotNull Level level, Player player, ItemStack stack) {
        BlockPos spillPos = clickedOnPos.above();
        BlockState existingState = level.getBlockState(spillPos);
        BlockState clickedOnState = level.getBlockState(clickedOnPos);

        if (clickedOnState.getBlock() instanceof FluidBlock) {
            transferSubstancesToBlock(clickedOnPos, level, stack);
        } else if (existingState.canBeReplaced()) {
            level.setBlock(spillPos, FLUID.get().defaultBlockState(), 3);
            transferSubstancesToBlock(spillPos, level, stack);
        } else {
            player.sendSystemMessage(Component.literal("Can't spill here!").withStyle(ChatFormatting.RED));
            return InteractionResult.PASS;
        }

        return InteractionResult.SUCCESS;
    }

    private void transferSubstancesToBlock(BlockPos pos, @NotNull Level level, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof FluidBlockEntity fluidEntity) {
            transferSubstances(stack, getTotalVolume(stack), getLimitedTransferRate(stack),
                    (substance, amount) -> fluidEntity.updateSubstance(substance));
            playEmptySound(level, pos);
        }
    }

    private void transferSubstancesFromBlock(BlockPos pos, @NotNull Level level, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof FluidBlockEntity fluidEntity) {
            float availableCapacity = capacity - getTotalVolume(stack);
            float transferRate = Math.min(getTransferRate(stack), availableCapacity);

            List<SubstanceStack> transferredSubstances = fluidEntity.transferSubstancesVolume(transferRate);
            SubstanceContents.Mutable mutableData = getMutableSubstanceData(stack);

            for (SubstanceStack substance : transferredSubstances) {
                mutableData.updateSubstance(substance);
            }

            setSubstanceDataFromMutable(stack, mutableData);
            playFillSound(level, pos);
        }
    }

    private void transferSubstancesToContainer(ItemStack sourceStack, ItemStack targetStack, Level level, Player player) {
        float totalSourceVolume = getTotalVolume(sourceStack);
        float transferRate = getLimitedTransferRate(sourceStack);
        float spaceAvailable = getCapacity(targetStack) - getTotalVolume(targetStack);

        if (totalSourceVolume <= 0 || spaceAvailable <= 0) return;

        float totalTransferVolume = Math.min(transferRate, Math.min(totalSourceVolume, spaceAvailable));

        transferSubstances(sourceStack, totalSourceVolume, totalTransferVolume,
                (substance, amount) -> updateTargetContainer(targetStack, substance, amount));

        if (totalTransferVolume > 0) {
            playFillSound(level, player.getOnPos());
        }
    }


    private void transferSubstances(ItemStack sourceStack, float totalAmount, float transferRate, SubstanceTransferHandler handler) {
        if (totalAmount <= 0) return;

        SubstanceContents.Mutable mutableData = getMutableSubstanceData(sourceStack);
        Iterator<SubstanceStack> iterator = mutableData.substances.iterator();

        while (iterator.hasNext()) {
            SubstanceStack substance = iterator.next();
            if (substance == null) continue;

            float proportion = totalAmount > 0 ? substance.getVolume() / totalAmount : 0;
            float transferAmount = Math.min(transferRate * proportion, substance.getVolume());

            if (transferAmount > 0) {
                SubstanceStack transferredSubstance = substance.copy();
                transferredSubstance.setVolume(transferAmount);

                handler.handle(transferredSubstance, transferAmount);
                substance.modifyVolume(-transferAmount);

                if (substance.getVolume() <= transferAmount) {
                    iterator.remove();
                }
            }
        }

        setSubstanceDataFromMutable(sourceStack, mutableData);
    }

    private void updateTargetContainer(ItemStack targetStack, SubstanceStack substance, float amount) {
        SubstanceContents.Mutable targetData = getMutableSubstanceData(targetStack);
        targetData.substances.add(substance);
        setSubstanceDataFromMutable(targetStack, targetData);
    }

    public static int getTransferRate(ItemStack stack) {
        return stack.getOrDefault(BitterDataComponents.TRANSFER_RATE.get(), MIN_TRANSFER_RATE);
    }

    public float getLimitedTransferRate(ItemStack stack) {
        return Math.min(stack.getOrDefault(BitterDataComponents.TRANSFER_RATE.get(), MIN_TRANSFER_RATE), getTotalVolume(stack));
    }

    public static void setTransferRate(ItemStack stack, int rate) {
        stack.set(BitterDataComponents.TRANSFER_RATE.get(), Mth.clamp(rate, MIN_TRANSFER_RATE, MAX_TRANSFER_RATE));
    }

    private void playEmptySound(Level level, BlockPos pos) {
        level.playSound(null, pos,
                SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 0.5F, 1.5F);
    }

    private void playFillSound(Level level, BlockPos pos) {
        level.playSound(null, pos,
                SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 0.5F, 1.0F);
    }

    private void playDrinkSound(Level level, BlockPos pos) {
        level.playSound(null, pos,
                SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.5F,
                level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    private void playBurpSound(Level level, BlockPos pos) {
        level.playSound(null, pos,
                SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F,
                level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return DRINK_SPEED + getTransferRate(stack);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide()) {
                player.sendSystemMessage(getFlavorMessageComponent(stack));
            }
        }

        float totalAmount = getTotalVolume(stack);
        float transferRate = getLimitedTransferRate(stack);
        SubstanceContents.Mutable mutableData = getMutableSubstanceData(stack);

        Iterator<SubstanceStack> iterator = mutableData.substances.iterator();

        while (iterator.hasNext()) {
            SubstanceStack substance = iterator.next();
            float proportion = totalAmount > 0 ? substance.getVolume() / totalAmount : 0;
            float consumeAmount = Math.min(transferRate * proportion, substance.getVolume());

            substance.modifyVolume(-consumeAmount);

            if (substance.getVolume() <= consumeAmount) {
                iterator.remove();
            }
        }

        setSubstanceDataFromMutable(stack, mutableData);

        playBurpSound(level, entity.getOnPos());

        return stack;
    }
    @FunctionalInterface
    private interface SubstanceTransferHandler {
        void handle(SubstanceStack substance, float amount);
    }
}
