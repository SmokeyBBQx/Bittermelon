package com.site21.bittermelon.common.content.items.medical.tools;

import com.site21.bittermelon.common.content.items.substance.FluidContainerItem;
import com.site21.bittermelon.common.systems.medical.legacy.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.custom.Substances.BLOOD;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.SUBSTANCE_FLUID;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CAN_SPILL;

public class SyringeItem extends FluidContainerItem {
    private static final int INJECTION_SPEED = 32;

    public SyringeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        ItemStack offhandItem = player.getOffhandItem();

        if (usedHand == InteractionHand.MAIN_HAND
                && offhandItem.getItem() instanceof FluidContainerItem
                && itemInHand.getOrDefault(CAN_SPILL, true)) {

            if (!level.isClientSide() && player.isShiftKeyDown()) {
                transferSubstancesToContainer(itemInHand, offhandItem, level, player);
            }
        } else {
            BlockHitResult blockHit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            BlockState blockState = level.getBlockState(blockHit.getBlockPos());

            if (itemInHand.getOrDefault(CAN_SPILL, true) && !blockState.is(SUBSTANCE_FLUID)) {
                return ItemUtils.startUsingInstantly(level, player, usedHand);
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return INJECTION_SPEED + getTransferRate(stack);
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.CROSSBOW;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        LivingEntity target = entity;

        HitResult hitResult = entity.pick(entity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE), 0.0F, false);
        if (hitResult instanceof EntityHitResult entityHitResult) {
            if (entityHitResult.getEntity() instanceof LivingEntity hitEntity) {
                target = hitEntity;
            }
        }

        if (entity.isShiftKeyDown()) {
            stack = consumeSubstances(stack, getLimitedTransferRate(stack), target);
        } else {
            int spaceAvailable = getCapacity(stack) - getTotalVolume(stack);
            int transferRate = Math.min(getTransferRate(stack), spaceAvailable);

            SubstanceStack substanceStack = drawSubstanceFromEntity(target, transferRate);
            updateSubstance(stack, substanceStack);
        }

        return stack;
    }

    private @NotNull SubstanceStack drawSubstanceFromEntity(@NotNull LivingEntity entity, int transferRate) {
        SubstanceStack stack = new SubstanceStack(BLOOD.get(), 0);
        stack.setVolume(transferRate);

        if (entity.getData(MEDICAL_STATS) instanceof AnimalMedicalStats medicalStats) {
//                stack.set(BLOOD_DATA, new BloodData(medicalStats.getBloodType(), medicalStats.getActiveDrugs()));
            medicalStats.modifyBloodVolume(-transferRate);
        }

        return stack;
    }
}
