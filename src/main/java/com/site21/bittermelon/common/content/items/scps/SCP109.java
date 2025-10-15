package com.site21.bittermelon.common.content.items.scps;

import com.site21.bittermelon.common.content.blocks.substance.fluid.FluidBlockEntity;
import com.site21.bittermelon.common.content.items.base.ItemWeight;
import com.site21.bittermelon.common.content.items.substance.FluidContainerItem;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.site21.bittermelon.init.custom.Substances.WATER;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;

public class SCP109 extends FluidContainerItem {
    public SCP109(Properties properties) {
        super(properties, 1, 1, ItemWeight.MEDIUM, true);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        updateSubstance(player.getItemInHand(usedHand), new SubstanceStack(WATER.get(), 1000));
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return 0;
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        if (getTotalVolume(stack) <= 0) return false;

        Level level = entity.level();
        if (!level.isClientSide && !entity.isNoGravity() && entity.onGround()) {
            stack.set(COOLDOWN, stack.getOrDefault(COOLDOWN, 20) - 1);
            if (stack.getOrDefault(CAN_SPILL, true) && stack.getOrDefault(COOLDOWN, 20) <= 0) {
                stack.set(COOLDOWN, 20);
                spill(stack, level, entity.blockPosition(), getMaxTransferRate(stack) * entity.getRandom().nextFloat());
                entity.playSound(SoundEvents.WATER_AMBIENT, 0.3f, 1);
            }
        }
        return false;
    }

    @Override
    protected void transferSubstancesToBlock(BlockPos pos, @NotNull Level level, ItemStack stack, float volume) {
        if (level.getBlockEntity(pos) instanceof FluidBlockEntity fluidEntity) {
            transferSubstances(stack, getTotalVolume(stack), volume,
                    (substance, amount) -> fluidEntity.updateSubstance(substance));
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("⇲" + getItemSize().description + " ⚖" + getItemWeight().description).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("Contents: ∞/∞"));
    }
}
