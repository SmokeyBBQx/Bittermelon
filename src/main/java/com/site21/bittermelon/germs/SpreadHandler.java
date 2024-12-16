package com.site21.bittermelon.germs;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.BitterDataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.site21.bittermelon.init.BitterAttachmentTypes.GERMS;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class SpreadHandler {

    @SubscribeEvent
    public static void onItemPickup(@NotNull ItemEntityPickupEvent.Post event) {
        System.out.println(event.getPlayer().getData(GERMS.get()));
        LivingEntity entity = event.getPlayer();

        List<UUID> itemGerms = event.getOriginalStack().get(BitterDataComponents.GERMS.get());
        List<UUID> playerGerms = new ArrayList<>(entity.getData(GERMS.get()));

        transferGerms(itemGerms, playerGerms, event.getCurrentStack(), entity);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.@NotNull Post event) {
        Player player = event.getEntity();
        ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offhandItem = player.getItemInHand(InteractionHand.OFF_HAND);

        List<UUID> playerGerms = new ArrayList<>(player.getData(GERMS.get()));

        if (!mainHandItem.isEmpty()) {
            List<UUID> mainHandGerms = mainHandItem.get(BitterDataComponents.GERMS.get());
            if (mainHandGerms != null && !new HashSet<>(playerGerms).containsAll(mainHandGerms)) {
                transferGerms(mainHandGerms, playerGerms, mainHandItem, player);
            }
        }

        if (!offhandItem.isEmpty()) {
            List<UUID> offHandGerms = offhandItem.get(BitterDataComponents.GERMS.get());
            if (offHandGerms != null && !new HashSet<>(playerGerms).containsAll(offHandGerms)) {
                transferGerms(offHandGerms, playerGerms, offhandItem, player);
            }
        }
    }

    private static void transferGerms(List<UUID> itemGerms, List<UUID> entityGerms, ItemStack item, LivingEntity entity) {
        if (itemGerms == null) {
            itemGerms = new ArrayList<>();
        }

        for (UUID uuid : entityGerms) {
            if (!itemGerms.contains(uuid)) {
                itemGerms.add(uuid);
            }
        }

        for (UUID uuid : itemGerms) {
            if (!entityGerms.contains(uuid)) {
                entityGerms.add(uuid);
            }
        }

        item.set(BitterDataComponents.GERMS.get(), itemGerms);
        entity.setData(GERMS.get(), entityGerms);
    }

    // TODO: Push, Eat
}
