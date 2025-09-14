package com.site21.bittermelon.content.items.screwdriver;

import com.site21.bittermelon.content.blocks.devices.PanelDevice;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlock;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlockEntity;
import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
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

import static com.site21.bittermelon.init.neoforge.BitterSounds.SCREWDRIVER;

public class ScrewdriverItem extends BaseItem {
    private static final int USE_DURATION = 60;

    public ScrewdriverItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        if (level.isClientSide || player == null) return InteractionResult.FAIL;

        if (level.getBlockEntity(clickedPos) instanceof PanelDevice) {
            player.startUsingItem(context.getHand());
            return InteractionResult.CONSUME;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (level.isClientSide) return stack;

        if (entity instanceof Player player) {
            BlockPos targetPos = getTargetBlockPos(player);
            if (targetPos == null) return stack;

            if (level.getBlockEntity(targetPos) instanceof PanelDevice panelDevice) {
                panelDevice.togglePanel();
                player.sendSystemMessage(Component.literal("Panel has been " + (panelDevice.isPanelOpen() ? "opened" : "closed") + ".").withStyle(ChatFormatting.GREEN));
            }
        }

        return stack;
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
        if (remainingUseDuration % 20 == 0) {
            entity.level().playSound(null, entity.getOnPos(), SCREWDRIVER.get(), SoundSource.AMBIENT,
                    0.3f, 1f);
        }
    }
}
