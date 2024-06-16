package net.smokeybbq.bittermelon.miscellaneous;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.util.Random;

import static net.smokeybbq.bittermelon.util.LocalMessageHandler.sendLocalMessage;

@Mod.EventBusSubscriber(modid = "bittermelon")
public class Stumble {
    ServerPlayer player;
    boolean stumbled;
    int stunTime = 100;
    int tickTimer = 0;
    private static final Random RANDOM = new Random();

    public Stumble(ServerPlayer player) {
        this.player = player;
        player.setPose(Pose.SWIMMING);
        playerPhysics();
        announceFall();
        dropItem();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void dropItem() {
        if (RANDOM.nextDouble() < 0.5) {
            ItemStack heldItem = player.getMainHandItem();
            player.drop(heldItem.copy(), true);
            heldItem.setCount(0);
        }
    }

    private void announceFall() {
        Character character = CharacterManager.getActiveCharacter(player);
        Component component = Component.literal(character.getName() + " falls to the ground.")
                .setStyle(Style.EMPTY.withColor(TextColor.parseColor(character.getEmoteColor())));
        sendLocalMessage(player, 10, component);
    }

    private void playerPhysics() {
        Vec3 lookDirection = player.getLookAngle();

        player.setDeltaMovement(player.getDeltaMovement().add(lookDirection).scale(1.3));
        player.hurtMarked = true;
        stumbled = true;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (stumbled && event.player == player) {
            player.setPose(Pose.SWIMMING);
        }

            float time = 5F - tickTimer / 20F;

            if (time >= 0) {
                String formattedTime = String.format("%.1f", time);
                player.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("Stunned for " + formattedTime + " seconds")));
            }

            tickTimer++;
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        ServerPlayer eventPlayer = player.server.getPlayerList().getPlayer(event.getEntity().getUUID());
        if (player == eventPlayer) {
            player.setDeltaMovement(player.getDeltaMovement().x(), 0, player.getDeltaMovement().z());
            player.hurtMarked = true;
            if (tickTimer >= stunTime) {
                stumbled = false;
                player.setPose(Pose.STANDING);
                MinecraftForge.EVENT_BUS.unregister(this);
                this.player = null;
            }
        }
    }
}

