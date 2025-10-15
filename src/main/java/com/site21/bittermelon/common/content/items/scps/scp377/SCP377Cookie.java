package com.site21.bittermelon.common.content.items.scps.scp377;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.FORTUNE;
import static com.site21.bittermelon.init.neoforge.BitterItems.CRACKED_FORTUNE_COOKIE;
import static com.site21.bittermelon.init.neoforge.BitterItems.SCP_377_1;

public class SCP377Cookie extends Item {
    public SCP377Cookie(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (level.isClientSide) return InteractionResult.PASS;

        stack.consume(1, player);

        ItemStack fortune = new ItemStack(SCP_377_1.get());
        fortune.set(FORTUNE, Fortune.getRandom(player.getRandom()));

        player.getInventory().add(fortune);
        player.getInventory().add(CRACKED_FORTUNE_COOKIE.toStack());
        return InteractionResult.SUCCESS;
    }
}
