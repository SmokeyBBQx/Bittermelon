package com.site21.bittermelon.common.content.items.scps.scp377;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.COOKIE_COUNT;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.EMPTY_TIME;
import static com.site21.bittermelon.init.neoforge.BitterItems.FORTUNE_COOKIE;
import static com.site21.bittermelon.init.neoforge.BitterSounds.SCP_377_COOKIE_TAKE;
import static com.site21.bittermelon.init.neoforge.BitterSounds.SCP_377_EMPTY;

public class SCP377Item extends Item {
    private static final long REFILL_DELAY = 2000;
    private static final int DEFAULT_COOKIE_COUNT = 20;

    public SCP377Item(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player,
            @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (level.isClientSide())
            return InteractionResult.PASS;
        int cookieCount = stack.getOrDefault(COOKIE_COUNT, DEFAULT_COOKIE_COUNT);

        if (cookieCount > 0) {
            player.addItem(FORTUNE_COOKIE.toStack());
            cookieCount--;
            stack.set(COOKIE_COUNT, cookieCount);
            level.playSound(
                    null,
                    player.getOnPos(),
                    SCP_377_COOKIE_TAKE.value(),
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f);
            if (cookieCount == 0) {
                stack.set(EMPTY_TIME, level.getGameTime());
                level.playSound(
                        null,
                        player.getOnPos(),
                        SCP_377_EMPTY.value(),
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f);
            }
            player.getCooldowns().addCooldown(stack, 10);
            return InteractionResult.SUCCESS;
        }

        player.sendSystemMessage(Component.literal("Box empty..")
                .withStyle(ChatFormatting.RED)
                .withStyle(ChatFormatting.ITALIC));

        return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull ServerLevel level, @NotNull Entity entity,
            @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);

        if (level.isClientSide())
            return;

        if (level.getGameTime() - stack.getOrDefault(EMPTY_TIME, -1L) > REFILL_DELAY) {
            stack.set(COOKIE_COUNT, DEFAULT_COOKIE_COUNT);
            stack.remove(EMPTY_TIME);
        }
    }
}
