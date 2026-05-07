package com.site21.bittermelon.common.content.items.scps.scp1079;

import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CANDY_COUNT;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.OPEN;
import static com.site21.bittermelon.init.neoforge.BitterItems.*;

public class SCP1079Item extends Item {
    private static final int DEFAULT_CANDY_COUNT = 60;

    public SCP1079Item(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player,
                                          @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (level.isClientSide)
            return InteractionResult.PASS;

        boolean isOpen = stack.getOrDefault(BitterDataComponents.OPEN.get(), false);

        if (!isOpen) {
            stack.set(BitterDataComponents.OPEN.get(), true);

            player.displayClientMessage(
                    Component.literal("You open the bag.")
                            .withStyle(ChatFormatting.LIGHT_PURPLE)
                            .withStyle(ChatFormatting.ITALIC), true);

            player.getCooldowns().addCooldown(stack, 10);
            return InteractionResult.SUCCESS;
        }

        int candyCount = stack.getOrDefault(BitterDataComponents.CANDY_COUNT.get(), DEFAULT_CANDY_COUNT);

        if (candyCount > 0) {
            player.addItem(SCP_1079_CANDY.toStack());
            candyCount--;
            stack.set(BitterDataComponents.CANDY_COUNT.get(), candyCount);

            if (candyCount == 0)
                stack.set(BitterDataComponents.EMPTY_TIME.get(), level.getGameTime());

            player.getCooldowns().addCooldown(stack, 10);
            return InteractionResult.SUCCESS;
        }

        player.displayClientMessage(
                Component.literal("The bag is empty.")
                        .withStyle(ChatFormatting.LIGHT_PURPLE)
                        .withStyle(ChatFormatting.ITALIC), true);

        return InteractionResult.PASS;
    }

}






