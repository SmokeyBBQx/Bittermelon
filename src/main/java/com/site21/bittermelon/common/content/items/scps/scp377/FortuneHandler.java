package com.site21.bittermelon.common.content.items.scps.scp377;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.FORTUNE_INSTANCES;

public class FortuneHandler {
    private static final long ACTIVATION_DELAY = 100;

    public static void onEntityTick(@NotNull Entity entity) {
        if (entity.level().isClientSide) return;
        if (!(entity instanceof Player player)) return;

        if (entity.getExistingDataOrNull(FORTUNE_INSTANCES) != null) {
            List<FortuneInstance> fortuneInstances = new ArrayList<>(entity.getData(FORTUNE_INSTANCES));
            Iterator<FortuneInstance> iterator = fortuneInstances.iterator();
            boolean modified = false;

            while (iterator.hasNext()) {
                FortuneInstance instance = iterator.next();
                if (applyFortune(player, instance, entity.level())) {
                    iterator.remove();
                    modified = true;
                }
            }

            if (modified) {
                player.setData(FORTUNE_INSTANCES, fortuneInstances);
            }
        }
    }

    private static boolean applyFortune(Player target, FortuneInstance instance, @NotNull Level level) {
        if (level.isClientSide) return false;

        if (level.getGameTime() - instance.readTime() >= ACTIVATION_DELAY) {
            instance.fortune().applyTo(target);
            return true;
        }
        return false;
    }
}
