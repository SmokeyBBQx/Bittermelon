package net.smokeybbq.bittermelon.miscellaneous;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
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
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void stumble() {
        player.setForcedPose(Pose.SWIMMING);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.player == this.player && stumbled) {
                player.setForcedPose(Pose.SWIMMING);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (this.player == player) {
                player.setDeltaMovement(player.getDeltaMovement().x(), 0, player.getDeltaMovement().z());
                stumbled = false;
                player.setForcedPose(null);
                MinecraftForge.EVENT_BUS.unregister(this);
                this.player = null;
            }
        }
    }
}

