package com.site21.bittermelon.common.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.scps.scp377.FortuneHandler;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageUtil;
import com.site21.bittermelon.common.systems.stress.StressHandler;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
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
        BlockDamageUtil.clearDamage(event.getLevel(), event.getPos());

        // TODO: Reset damage if the new block state is a different block
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.@NotNull EntityInteract event) {
        Player player = event.getEntity();

        if (player.isShiftKeyDown() && event.getHand() == InteractionHand.MAIN_HAND) {
            event.getTarget().startRiding(player, true);
            System.out.println("Passenger index: " + player.getPassengers().indexOf(event.getTarget()));
            player.setData(BitterAttachmentTypes.CARRIED_PASSENGER, player.getPassengers().indexOf(event.getTarget()));
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.@NotNull RightClickBlock event) {
        Player player = event.getEntity();

        if (player.isShiftKeyDown() && event.getHand() == InteractionHand.MAIN_HAND) {
            int passengerIndex = player.getData(BitterAttachmentTypes.CARRIED_PASSENGER);
            if (passengerIndex >= 0 && passengerIndex < player.getPassengers().size()) {
                Entity carriedEntity = player.getPassengers().get(passengerIndex);
                carriedEntity.stopRiding();
                player.removeData(BitterAttachmentTypes.CARRIED_PASSENGER);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.@NotNull RightClickEmpty event) {
        Player player = event.getEntity();

        if (player.isShiftKeyDown() && event.getHand() == InteractionHand.MAIN_HAND) {
            int passengerIndex = player.getData(BitterAttachmentTypes.CARRIED_PASSENGER);
            if (passengerIndex >= 0 && passengerIndex < player.getPassengers().size()) {
                Entity carriedEntity = player.getPassengers().get(passengerIndex);
                carriedEntity.stopRiding();
                player.removeData(BitterAttachmentTypes.CARRIED_PASSENGER);
            }
        }
    }
}
