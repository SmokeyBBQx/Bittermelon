package net.smokeybbq.bittermelon.miscellaneous;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static net.smokeybbq.bittermelon.util.LocalMessageHandler.sendLocalMessage;

@Mod.EventBusSubscriber(modid = "bittermelon")
public class Stumble {
    private final ServerPlayer player;
    private boolean stumbled;
    private int stunTime = 100;
    private int tickTimer = 0;
    private static final Random RANDOM = new Random();

    public Stumble(ServerPlayer player) {
        this.player = player;
        MinecraftForge.EVENT_BUS.register(this);
        playerPhysics();
        announceFall();
        dropItem();
    }

    private void dropItem() {
        if (RANDOM.nextDouble() < 0.5) {
            ItemStack heldItem = player.getMainHandItem();
            if (!heldItem.isEmpty()) {
                player.drop(heldItem.copy(), true);
                heldItem.setCount(0);
            }
        }
    }

    private void announceFall() {
//        Character character = CharacterManager.getActiveCharacter(player);
//        Component component = Component.literal(character.getName() + " falls to the ground.")
//                .setStyle(Style.EMPTY.withColor(TextColor.parseColor(character.getEmoteColor())));
//        sendLocalMessage(player, 10, component);
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
                event.player.setPose(Pose.SWIMMING);

                float time = 5F - tickTimer / 20F;
                if (time >= 0) {
                    String formattedTime = String.format("%.1f", time);
                    player.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("Stunned for " + formattedTime + " seconds")));
                }

                tickTimer++;
            }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer eventPlayer && eventPlayer == player) {
            player.setDeltaMovement(player.getDeltaMovement().x(), 0, player.getDeltaMovement().z());
            player.hurtMarked = true;
            if (tickTimer >= stunTime) {
                stumbled = false;
                player.setPose(Pose.STANDING);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }
}
