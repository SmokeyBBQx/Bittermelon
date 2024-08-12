package net.smokeybbq.bittermelon.mixins;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import net.smokeybbq.bittermelon.networking.PacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerPoseClient {
    @Inject(method = "Lnet/minecraft/world/entity/player/Player;updatePlayerPose()V", at = @At(value = "HEAD"), cancellable = true)
    public void updatePlayerPose(CallbackInfo ci) {
        Player player = (Player)(Object) this;

        System.out.println("Mixin working for: " + player);

}

    private boolean shouldPlayerCrawl(Player player) {
        return true;
    }
}
