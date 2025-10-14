package com.site21.bittermelon.content.items.writablepaper;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;

public class WritablePaper extends BaseItem {
    public WritablePaper(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

//    @Override
//    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
//        if (player instanceof ServerPlayer serverPlayer) PacketDistributor.sendToPlayer(serverPlayer, new OpenPaperEditScreen(player.getItemInHand(usedHand)));
//        return InteractionResultHolder.success(player.getItemInHand(usedHand));
//    }
}
