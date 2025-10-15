package com.site21.bittermelon.common.content.items.scps.scp377;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.COOKIE_COUNT;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.EMPTY_TIME;
import static com.site21.bittermelon.init.neoforge.BitterItems.FORTUNE_COOKIE;

public class SCP377 extends Item {
    private static final long REFILL_DELAY = 2000;
    private static final int DEFAULT_COOKIE_COUNT = 20;

    public SCP377(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (level.isClientSide) return InteractionResultHolder.pass(stack);
        int cookieCount = stack.getOrDefault(COOKIE_COUNT, DEFAULT_COOKIE_COUNT);

        if (cookieCount > 0) {
            player.addItem(FORTUNE_COOKIE.toStack());
            cookieCount--;
            stack.set(COOKIE_COUNT, cookieCount);
            if (cookieCount == 0) {
                stack.set(EMPTY_TIME, level.getGameTime());
            }
            return InteractionResultHolder.success(stack);
        }

        player.sendSystemMessage(Component.literal("Box empty..").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC));

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide) return;

        if (level.getGameTime() - stack.getOrDefault(EMPTY_TIME, -1L) > REFILL_DELAY) {
            stack.set(COOKIE_COUNT, DEFAULT_COOKIE_COUNT);
            stack.remove(EMPTY_TIME);
        }
    }
}
