package net.smokeybbq.bittermelon.miscellaneous;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerPoseClient {
    @Inject(method = "Lnet/minecraft/world/entity/player/Player;updatePlayerPose()V", at = @At(value = "HEAD"), cancellable = true)
    public void updatePlayerPose(CallbackInfo event) {
        Player player = (Player)(Object) this;

        System.out.println("Mixin working for: " + player);

//        if (Stumble.isStumbled(player)) {
            System.out.println("Player stumbling: " + player);
            player.setPose(Pose.SWIMMING);
//            event.cancel();
//        }
    }
}
