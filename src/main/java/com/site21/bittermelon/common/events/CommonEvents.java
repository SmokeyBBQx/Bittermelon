package com.site21.bittermelon.common.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.scps.scp377.FortuneHandler;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageHelper;
import com.site21.bittermelon.common.systems.stress.StressHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.@NotNull Post event) {
        Entity entity = event.getEntity();
        FortuneHandler.onEntityTick(entity);

        if (entity instanceof Player player) {
            StressHandler.tickStress(player);
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.@NotNull BreakEvent event) {
        BlockDamageHelper.clearDamage(event.getLevel(), event.getPos());

        // TODO: Reset damage if the new block state is a different block
    }
}
