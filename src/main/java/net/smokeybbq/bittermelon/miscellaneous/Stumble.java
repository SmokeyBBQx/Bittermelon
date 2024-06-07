package net.smokeybbq.bittermelon.miscellaneous;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.security.auth.callback.Callback;

@Mod.EventBusSubscriber(modid = "bittermelon")
public class Stumble {
    ServerPlayer player;
    boolean stumbled;

    public Stumble(ServerPlayer player) {
        this.player = player;
        stumbled = true;
    }

    public void stumble() {
        player.setForcedPose(Pose.SWIMMING);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (stumbled == true) {
            player.setForcedPose(Pose.SWIMMING);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        ServerPlayer eventPlayer = player.server.getPlayerList().getPlayer(event.getEntity().getUUID());
        if (player == eventPlayer) {
            event.setCanceled(true);
            stumbled = false;
            player.setForcedPose(null);
        }
    }
}

