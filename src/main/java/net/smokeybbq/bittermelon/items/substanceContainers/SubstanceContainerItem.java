package net.smokeybbq.bittermelon.items.substanceContainers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
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
import net.smokeybbq.bittermelon.blocks.PuddleBlock;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.substances.Substance;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import static net.smokeybbq.bittermelon.init.BlockInit.PUDDLE;

public class SubstanceContainerItem extends Item {
    protected int capacity;

    public SubstanceContainerItem(Properties pProperties) {
        super(pProperties);
    }

    public void updateSubstance(ItemStack itemStack, Substance substance, int amount) {
        Map<Substance, Integer> substances = loadFromItemStack(itemStack);
        int newAmount = Math.max(0,substances.getOrDefault(substance, 0) + amount);
        System.out.println("Current substance amount: " + substances.getOrDefault(substance, 0));
        System.out.println("Updating substance amount with: " + newAmount);
        substances.put(substance, newAmount);
        saveToItemStack(itemStack, substances);

        syncItemStackToClient(itemStack);
    }

    private void syncItemStackToClient(ItemStack itemStack) {

    }

    public int getTotalAmount(ItemStack itemStack) {
        Map<Substance, Integer> substances = loadFromItemStack(itemStack);
        return substances.values().stream().mapToInt(Integer::intValue).sum();
    }

    private CompoundTag serializeData(Map<Substance, Integer> substances) {
        CompoundTag nbt = new CompoundTag();
        ListTag substancesList = new ListTag();
        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            CompoundTag substanceTag = entry.getKey().serializeNBT();
            substanceTag.putInt("Amount", entry.getValue());
            substancesList.add(substanceTag);
        }
        nbt.put("Substances", substancesList);
        return nbt;
    }

    private Map<Substance, Integer> deserializeData(CompoundTag nbt) {
        Map<Substance, Integer> substances = new HashMap<>();
        ListTag substancesList = nbt.getList("Substances", 10);
        for (int i = 0; i < substancesList.size(); i++) {
            CompoundTag substanceTag = substancesList.getCompound(i);
            Substance substance = Substance.fromNBT(substanceTag);
            int amount = substanceTag.getInt("Amount");
            substances.put(substance, amount);
        }
        return substances;
    }

    public void saveToItemStack(ItemStack stack, Map<Substance, Integer> substances) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.put("SubstanceData", serializeData(substances));
    }

    public Map<Substance, Integer> loadFromItemStack(ItemStack stack) {
        Map<Substance, Integer> substances = new HashMap<>();
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("SubstanceData")) {
            substances = deserializeData(nbt.getCompound("SubstanceData"));
        }
        return substances;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> toolTipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, toolTipComponents, isAdvanced);
        loadFromItemStack(stack);
        int totalAmount = getTotalAmount(stack);
        toolTipComponents.add(Component.literal("Contents: " + totalAmount + "/" + capacity)
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        loadFromItemStack(stack);
        int totalAmount = getTotalAmount(stack);
        return Math.round(13.0F * totalAmount / capacity);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        loadFromItemStack(stack);
        float fillPercentage = (float) getTotalAmount(stack) / capacity;
        if (fillPercentage < 0.5f) {
            return 0xFF0000 | (Math.round(510 * fillPercentage) << 8);  // Red to Yellow
        } else {
            return 0x00FF00 | (Math.round(510 * (1 - fillPercentage)) << 16);  // Yellow to Green
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        ItemStack itemStack = pContext.getItemInHand();
        BlockPos pos = pContext.getClickedPos();

        if (player != null && player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                if (!loadFromItemStack(itemStack).isEmpty()) {
                    spillAction(level, pos, player, itemStack);
                    player.getInventory().setItem(player.getInventory().selected, itemStack);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(pContext);
    }

    private void spillAction(Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (getTotalAmount(itemStack) > 0) {
            BlockPos spillPos = pos.above();
            BlockState existingState = level.getBlockState(spillPos);
            BlockState existingStateBelow = level.getBlockState(pos);

            if (existingStateBelow.getBlock() instanceof PuddleBlock) {
                spill(pos, level, itemStack);
            } else if (existingState.isAir()) {
                level.setBlock(spillPos, PUDDLE.get().defaultBlockState(), 1);
                spill(spillPos, level, itemStack);
            } else {
                player.sendSystemMessage(Component.literal("Can't spill here!").withStyle(ChatFormatting.RED));
            }
        }
    }

    private void spill(BlockPos pos, Level level, ItemStack itemStack) {
        if (level.getBlockEntity(pos) instanceof PuddleBlockEntity puddleBlockEntity) {
            for (Map.Entry<Substance, Integer> entry : loadFromItemStack(itemStack).entrySet()) {
                puddleBlockEntity.addSubstance(entry.getKey(), entry.getValue());
                updateSubstance(itemStack, entry.getKey(), -entry.getValue());
            }

            puddleBlockEntity.setChanged();
            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
        }
        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
