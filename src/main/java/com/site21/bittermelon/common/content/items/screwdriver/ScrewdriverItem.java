package com.site21.bittermelon.common.content.items.screwdriver;

import com.site21.bittermelon.common.systems.electronics.PanelDevice;
import com.site21.bittermelon.common.content.items.base.BitterItem;
import com.site21.bittermelon.common.content.items.base.ItemWeight;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

import static com.site21.bittermelon.init.neoforge.BitterSounds.*;

public class ScrewdriverItem extends BitterItem {
    private static final int USE_DURATION = 60;

    public ScrewdriverItem(Properties properties) {
        super(properties);
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

                playPanelSound(level, targetPos, panelDevice.isPanelOpen());
                player.sendSystemMessage(Component.literal("You " + (panelDevice.isPanelOpen() ? "open" : "close") + " the panel.")
                        .withStyle(ChatFormatting.ITALIC)
                        .withStyle(ChatFormatting.GRAY));
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

    private void playPanelSound(@NotNull Level level, BlockPos pos, boolean open) {
        level.playSound(null, pos, open ? SCREWDRIVER_OPEN.get() : SCREWDRIVER_CLOSE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}
