package net.smokeybbq.bittermelon.items.substancecontainers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.smokeybbq.bittermelon.blocks.PuddleBlock;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.init.ModCapabilities;
import net.smokeybbq.bittermelon.networking.PacketHandler;
import net.smokeybbq.bittermelon.networking.TransferRateUpdatePacket;
import net.smokeybbq.bittermelon.substances.Substance;
import net.smokeybbq.bittermelon.util.ModLogger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.smokeybbq.bittermelon.init.BlockInit.PUDDLE;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SubstanceContainerItem extends SubstanceItem {
    private static final Random RANDOM = new Random();
    private static final int MIN_TRANSFER_RATE = 1;
    private static final int MAX_TRANSFER_RATE = 100;
    private static final int DRINK_SPEED = 32;
    private final int capacity;


    public SubstanceContainerItem(Properties pProperties, int capacity) {
        super(pProperties, capacity);
        this.capacity = capacity;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> toolTipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, toolTipComponents, isAdvanced);
        getSubstanceContainer(stack).ifPresent(cap ->
                toolTipComponents.add(Component.literal("Contents: " + cap.getTotalAmount() + "/" + capacity))
        );
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();

        // Check if the container is empty
        if (isContainerEmpty(mainHandItem)) {
            playEmptySound(level, player);
            return InteractionResultHolder.fail(player.getMainHandItem());
        }

        if (getTransferRate(mainHandItem) < 1) {
            playEmptySound(level, player);
            return InteractionResultHolder.fail(player.getMainHandItem());
        }


        if (hand == InteractionHand.MAIN_HAND && offHandItem.getItem() instanceof SubstanceContainerItem) {
            if (!level.isClientSide) {
                if (player.isShiftKeyDown()) {

                    transferSubstances(mainHandItem, offHandItem, level, player);

                    return InteractionResultHolder.success(mainHandItem);
                }
            }
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.5F,
                    level.getRandom().nextFloat() * 0.1F + 0.9F);

            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        return super.use(level, player, hand);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        ItemStack itemStack = pContext.getItemInHand();
        BlockPos pos = pContext.getClickedPos();

        if (getTransferRate(itemStack) < 1) {
            assert player != null;
            playEmptySound(level, player);
            return InteractionResult.PASS;
        }

        if (!level.isClientSide && player != null) {

            // Priority 1: Transfer between containers
            if (player.getOffhandItem().getItem() instanceof SubstanceContainerItem) {
                transferSubstances(itemStack, player.getOffhandItem(), level, player);
                return InteractionResult.SUCCESS;
            }

            // Priority 2: Spill action (if shift is pressed)
            if (player.isShiftKeyDown()) {
                return getSubstanceContainer(itemStack)
                        .filter(cap -> !cap.getSubstances().isEmpty())
                        .map(cap -> {
                            spillAction(level, pos, player, itemStack, cap);
                            return InteractionResult.SUCCESS;
                        })
                        .orElse(InteractionResult.PASS);
            }

            // Priority 3: Interact with PuddleBlock
            if (level.getBlockState(pos).getBlock() instanceof PuddleBlock) {
                pickUpLiquid(level, pos, itemStack);
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(pContext);
    }

    private boolean isContainerEmpty(ItemStack stack) {
        return getSubstanceContainer(stack)
                .map(ISubstanceContainer::getTotalAmount)
                .orElse(0) <= 0;
    }

    private void playEmptySound(Level level, Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 0.5F, 1.5F);
    }

    private void pickUpLiquid(Level level, BlockPos pos, ItemStack itemStack) {
        if (level.getBlockEntity(pos) instanceof PuddleBlockEntity puddle) {
            int availableCapacity = capacity - getSubstanceContainer(itemStack)
                    .map(ISubstanceContainer::getTotalAmount)
                    .orElse(0);
            int transferRate = Math.min(getTransferRate(itemStack), availableCapacity);

            Map<Substance, Integer> transferredSubstances = puddle.transferSubstances(transferRate);
            transferredSubstances.forEach((substance, amount) -> {
                updateSubstance(itemStack, substance, amount);
                ModLogger.info("Updating " + substance + " with: " + amount);
            });
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private void transferSubstances(ItemStack sourceItem, ItemStack targetItem, Level level, Player player) {
        LazyOptional<ISubstanceContainer> sourceContainerOpt = sourceItem.getCapability(ModCapabilities.SUBSTANCE_CONTAINER_CAPABILITY);
        LazyOptional<ISubstanceContainer> targetContainerOpt = targetItem.getCapability(ModCapabilities.SUBSTANCE_CONTAINER_CAPABILITY);

        sourceContainerOpt.ifPresent(sourceContainer ->
                targetContainerOpt.ifPresent(targetContainer -> {
                    Map<Substance, Integer> sourceSubstances = sourceContainer.getSubstances();
                    int transferRate = getTransferRate(sourceItem);
                    int totalSourceAmount = sourceSubstances.values().stream().mapToInt(Integer::intValue).sum();
                    int spaceAvailable = targetContainer.getCapacity() - targetContainer.getTotalAmount();

                    if (totalSourceAmount > 0 && spaceAvailable > 0) {
                        int totalTransferAmount = Math.min(transferRate, Math.min(totalSourceAmount, spaceAvailable));

                        for (Map.Entry<Substance, Integer> entry : sourceSubstances.entrySet()) {
                            Substance substance = entry.getKey();
                            int availableAmount = entry.getValue();

                            double proportion = (double) availableAmount / totalSourceAmount;
                            int transferAmount = (int) Math.ceil(totalTransferAmount * proportion);
                            int actualTransferAmount = Math.min(transferAmount, availableAmount);

                            if (actualTransferAmount > 0) {
                                sourceContainer.updateSubstance(substance, -actualTransferAmount);
                                targetContainer.updateSubstance(substance, actualTransferAmount);

                                ModLogger.info("Transferred " + actualTransferAmount + " of " + substance.getName() + " from main hand to off hand");
                            }
                        }

                        if (totalTransferAmount > 0) {
                            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                                    SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 0.5F, 1.0F);

                            updateVisuals(sourceItem);
                            updateVisuals(targetItem);
                        }
                    }
                }));
    }

    private void spillAction(Level level, BlockPos pos, Player player, ItemStack itemStack, ISubstanceContainer cap) {
        if (cap.getTotalAmount() <= 0) return;

        BlockPos spillPos = pos.above();
        BlockState existingState = level.getBlockState(spillPos);
        BlockState existingStateBelow = level.getBlockState(pos);

        if (existingStateBelow.getBlock() instanceof PuddleBlock) {
            spill(pos, level, itemStack, cap);
        } else if (existingState.isAir()) {
            level.setBlock(spillPos, PUDDLE.get().defaultBlockState(), 3);
            spill(spillPos, level, itemStack, cap);
        } else {
            player.sendSystemMessage(Component.literal("Can't spill here!").withStyle(ChatFormatting.RED));
        }
    }

    private void spill(BlockPos pos, Level level, ItemStack itemStack, ISubstanceContainer cap) {
        if (level.getBlockEntity(pos) instanceof PuddleBlockEntity puddleBlockEntity) {
            Map<Substance, Integer> substances = cap.getSubstances();
            int totalAmount = substances.values().stream().mapToInt(Integer::intValue).sum();
            int transferRate = getTransferRate(itemStack);

            substances.forEach((substance, amount) -> {
                if (totalAmount > 0) {
                    float proportion = (float) amount / totalAmount;
                    int transferAmount = (int) Math.ceil(transferRate * proportion);
                    int actualAmount = Math.min(amount, transferAmount);

                    puddleBlockEntity.updateSubstance(substance, actualAmount);
                    updateSubstance(itemStack, substance, -actualAmount);
                }
            });

            puddleBlockEntity.setChanged();
            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level();
        if (!level.isClientSide && !entity.isNoGravity() && entity.onGround()) {
            CompoundTag tag = stack.getOrCreateTag();
            if (!tag.getBoolean("hasLanded")) {
                tag.putBoolean("hasLanded", true);
                spillOnLanding(stack, level, entity.blockPosition());
            }
        }
        return false; // Return false to allow normal update behavior
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        CompoundTag tag = stack.getOrCreateTag();
        if (tag.getBoolean("hasLanded")) {
            tag.putBoolean("hasLanded", false);
        }
    }

    private void spillOnLanding(ItemStack stack, Level level, BlockPos spillPos) {
        getSubstanceContainer(stack).ifPresent(cap -> {
            int totalAmount = cap.getTotalAmount();
            if (totalAmount <= 0) return;

            if (!(level.getBlockState(spillPos).getBlock() instanceof PuddleBlock)) {
                level.setBlock(spillPos, PUDDLE.get().defaultBlockState(), 3);
            }

            if (level.getBlockEntity(spillPos) instanceof PuddleBlockEntity puddleBlockEntity) {
                float spillPercentage = RANDOM.nextFloat();
                int totalSpillAmount = Math.round(totalAmount * spillPercentage);

                Map<Substance, Integer> substances = cap.getSubstances();
                for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
                    Substance substance = entry.getKey();
                    int availableAmount = entry.getValue();

                    float proportion = (float) availableAmount / totalAmount;
                    int spillAmount = (int) Math.ceil(totalSpillAmount * proportion);
                    spillAmount = Math.min(spillAmount, availableAmount);

                    if (spillAmount > 0) {
                        puddleBlockEntity.updateSubstance(substance, spillAmount);
                        updateSubstance(stack, substance, -spillAmount);
                        ModLogger.info("Spilled " + spillAmount + " of " + substance.getName() + " at " + spillPos);
                    }
                }

                puddleBlockEntity.setChanged();
                level.sendBlockUpdated(spillPos, level.getBlockState(spillPos), level.getBlockState(spillPos), 3);
                level.playSound(null, spillPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 0.5F, 1.0F);
            }
        });
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.isShiftKeyDown()) {
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof SubstanceContainerItem) {
                int currentRate = getTransferRate(heldItem);
                int newRate = Mth.clamp(currentRate + (event.getScrollDelta() > 0 ? 1 : -1), MIN_TRANSFER_RATE, MAX_TRANSFER_RATE);
                setTransferRate(heldItem, newRate);

                PacketHandler.INSTANCE.sendToServer(new TransferRateUpdatePacket(newRate));

                event.setCanceled(true);
            }
        }
    }

    public static int getTransferRate(ItemStack stack) {
        return stack.getOrCreateTag().getInt("TransferRate");
    }

    public static void setTransferRate(ItemStack stack, int rate) {
        stack.getOrCreateTag().putInt("TransferRate", rate);
    }

    public static void handleTransferRateUpdate(Player player, int newRate) {
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() instanceof SubstanceContainerItem) {
            setTransferRate(heldItem, newRate);
            ModLogger.info("Updated transfer rate for " + player.getName().getString() + " to " + newRate);
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
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


            getSubstanceContainer(stack).ifPresent(cap -> {
                Map<Substance, Integer> substances = cap.getSubstances();
                int totalSubstances = substances.size();
                if (totalSubstances == 0) return;

                int totalAmount = cap.getTotalAmount();

                substances.forEach((substance, amount) -> {
                    float proportion = (float) amount / totalAmount;
                    int consumeAmount = (int) Math.ceil(getTransferRate(stack) * proportion);
                    int actualAmount = Math.min(amount, consumeAmount);

                    updateSubstance(stack, substance, -actualAmount);
                    substance.getEffects(entity, actualAmount);
                });
            });

            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F,
                    level.getRandom().nextFloat() * 0.1F + 0.9F);


        }
        return stack;
    }
}
