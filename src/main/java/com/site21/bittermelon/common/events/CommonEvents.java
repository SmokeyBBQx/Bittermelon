package com.site21.bittermelon.common.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.scps.scp377.FortuneHandler;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageUtil;
import com.site21.bittermelon.common.systems.carry.CarryHandler;
import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.factory.AnatomyType;
import com.site21.bittermelon.common.systems.stress.StressHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.@NotNull Post event) {
        Entity entity = event.getEntity();
        FortuneHandler.onEntityTick(entity);

        if (entity instanceof LivingEntity livingEntity) {
            CarryHandler.tickCarrying(livingEntity);
        }

        if (entity instanceof Player player) {
            StressHandler.tickStress(player);
            if (!player.hasData(MEDICAL_STATS)) {
                player.setData(MEDICAL_STATS, AnatomyType.HUMAN.getFactory().build(BloodType.O_MINUS));
            } else {
                player.getData(MEDICAL_STATS).tick(player);
            }
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.@NotNull BreakEvent event) {
        BlockDamageUtil.clearDamage(event.getLevel(), event.getPos());

        // TODO: Reset damage if the new block state is a different block
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.@NotNull EntityInteract event) {
        if (CarryHandler.playerPickUpEntity(event.getEntity(), event.getTarget())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.@NotNull RightClickBlock event) {
        if (CarryHandler.placeEntity(event.getEntity(), event.getPos(), event.getHitVec())) {
            event.setCanceled(true);
        }
    }
}
