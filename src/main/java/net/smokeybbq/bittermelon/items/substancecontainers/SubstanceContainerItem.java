package net.smokeybbq.bittermelon.items.substancecontainers;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.smokeybbq.bittermelon.init.BlockInit.PUDDLE;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SubstanceContainerItem extends Item {
    private static final Random RANDOM = new Random();
    protected int capacity;
    private static final int MIN_TRANSFER_RATE = 1;
    private static final int MAX_TRANSFER_RATE = 100;

    public SubstanceContainerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilitySerializable<CompoundTag>() {
            final LazyOptional<ISubstanceContainer> container = LazyOptional.of(() -> new SubstanceContainerCapability(capacity));

            @Override
            public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return ModCapabilities.SUBSTANCE_CONTAINER_CAPABILITY.orEmpty(cap, container);
            }

            @Override
            public CompoundTag serializeNBT() {
                return container.map(ISubstanceContainer::serializeNBT).orElse(new CompoundTag());
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                container.ifPresent(c -> c.deserializeNBT(nbt));
            }
        };
    }

    private LazyOptional<ISubstanceContainer> getSubstanceContainer(ItemStack stack) {
        return stack.getCapability(ModCapabilities.SUBSTANCE_CONTAINER_CAPABILITY);
    }

    public void updateSubstance(ItemStack stack, Substance substance, int amount) {
        getSubstanceContainer(stack).ifPresent(cap -> {
            boolean updated = false;
            for (Map.Entry<Substance, Integer> entry : cap.getSubstances().entrySet()) {
                if (entry.getKey().getName().equals(substance.getName())) {
                    cap.updateSubstance(entry.getKey(), amount);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                cap.updateSubstance(substance, amount);
            }
            updateVisuals(stack);
        });
    }
//    private void syncToClient(ItemStack stack, ServerPlayer player) {
//        stack.getCapability(ModCapabilities.SUBSTANCE_CONTAINER_CAPABILITY).ifPresent(cap -> {
//            int slot = player.getInventory().findSlotMatchingItem(stack);
//            if (slot != -1) {
//                SubstanceContainerSyncPacket packet = new SubstanceContainerSyncPacket(slot, cap.getSubstances());
//                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
//                ModLogger.debug("Sent SubstanceContainerSyncPacket to player: " + player.getName().getString());
//            }
//        });
//    }

    private void updateVisuals(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putLong("LastUpdate", System.currentTimeMillis());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> toolTipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, toolTipComponents, isAdvanced);
        getSubstanceContainer(stack).ifPresent(cap ->
                toolTipComponents.add(Component.literal("Contents: " + cap.getTotalAmount() + "/" + capacity))
        );
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return getSubstanceContainer(stack)
                .map(cap -> (int) ((long) cap.getTotalAmount() * MAX_BAR_WIDTH / capacity))
                .orElse(0);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return getSubstanceContainer(stack)
                .map(cap -> {
                    float fillPercentage = (float) cap.getTotalAmount() / capacity;
                    if (fillPercentage < 0.5f) {
                        return 0xFF0000 | (Math.round(510 * fillPercentage) << 8);  // Red to Yellow
                    } else {
                        return 0x00FF00 | (Math.round(510 * (1 - fillPercentage)) << 16);  // Yellow to Green
                    }
                })
                .orElse(0xFF0000);  // Default to red if capability is not present
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

        if (hand == InteractionHand.MAIN_HAND && offHandItem.getItem() instanceof SubstanceContainerItem) {
            if (player.isShiftKeyDown()) {

                if (!level.isClientSide) {
                    transferSubstances(mainHandItem, offHandItem, level, player);
                }

                return InteractionResultHolder.success(mainHandItem);
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        ItemStack itemStack = pContext.getItemInHand();
        BlockPos pos = pContext.getClickedPos();

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

                    for (Map.Entry<Substance, Integer> entry : sourceSubstances.entrySet()) {
                        Substance substance = entry.getKey();
                        int availableAmount = entry.getValue();
                        int transferAmount = Math.min(availableAmount, transferRate);
                        int spaceAvailable = targetContainer.getCapacity() - targetContainer.getTotalAmount();

                        if (spaceAvailable > 0) {
                            int actualTransferAmount = Math.min(transferAmount, spaceAvailable);

                            sourceContainer.updateSubstance(substance, -actualTransferAmount);
                            targetContainer.updateSubstance(substance, actualTransferAmount);

                            ModLogger.info("Transferred " + actualTransferAmount + " of " + substance.getName() + " from main hand to off hand");

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
            substances.forEach((substance, amount) -> {
                int actualAmount = Math.min(amount, getTransferRate(itemStack) / substances.size());
                ModLogger.info("Adding " + actualAmount + " of " + substance.getName() + " to puddle at " + pos);
                puddleBlockEntity.updateSubstance(substance, actualAmount);
                updateSubstance(itemStack, substance, -actualAmount);
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

                Map<Substance, Integer> substances = cap.getSubstances();
                for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
                    Substance substance = entry.getKey();
                    int availableAmount = entry.getValue();
                    int spillAmount = Math.round(availableAmount * spillPercentage);

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
}
