package com.site21.bittermelon.common.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class CommonEvents {

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.@NotNull BreakEvent event) {
        BlockDamageHelper.clearDamage(event.getLevel(), event.getPos());

        // TODO: Reset damage if the new block state is a different block
    }
}
