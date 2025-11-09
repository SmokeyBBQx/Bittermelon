package com.site21.bittermelon.common.content.items.writablepaper;

import com.site21.bittermelon.common.content.items.base.BitterItem;
import com.site21.bittermelon.common.content.items.base.ItemWeight;
import com.site21.bittermelon.common.content.items.writablepaper.client.OpenPaperEditScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class WritablePaper extends BitterItem {
    public WritablePaper(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if (player instanceof ServerPlayer serverPlayer) PacketDistributor.sendToPlayer(serverPlayer, new OpenPaperEditScreen(player.getItemInHand(usedHand)));
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
