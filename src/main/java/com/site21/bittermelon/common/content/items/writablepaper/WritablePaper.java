package com.site21.bittermelon.common.content.items.writablepaper;

import net.minecraft.world.item.Item;

public class WritablePaper extends Item {
    public WritablePaper(Properties properties) {
        super(properties);
    }

//    @Override
//    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
//        if (player instanceof ServerPlayer serverPlayer) PacketDistributor.sendToPlayer(serverPlayer, new OpenPaperEditScreen(player.getItemInHand(usedHand)));
//        return InteractionResultHolder.success(player.getItemInHand(usedHand));
//    }
}
