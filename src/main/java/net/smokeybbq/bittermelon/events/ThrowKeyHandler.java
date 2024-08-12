package net.smokeybbq.bittermelon.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.entities.ThrownItemProjectile;
import net.smokeybbq.bittermelon.init.ModKeyBindings;
import net.smokeybbq.bittermelon.networking.PacketHandler;
import net.smokeybbq.bittermelon.networking.ThrowItemPacket;
import net.smokeybbq.bittermelon.util.ModLogger;

import static net.smokeybbq.bittermelon.util.LocalMessageHandler.sendLocalMessage;

public class ThrowKeyHandler {
    private static final float THROW_STRENGTH = 1.5f;

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (ModKeyBindings.THROW_ITEM.consumeClick()) {
            PacketHandler.INSTANCE.sendToServer(new ThrowItemPacket());
        }

    }

    public static void throwItemAsProjectile(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        Level level = player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!heldItem.isEmpty()) {
            ThrownItemProjectile projectile = new ThrownItemProjectile(player.level(), player, heldItem.copy());
            projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            player.level().addFreshEntity(projectile);
            if (!player.getAbilities().instabuild) {
                heldItem.shrink(1);
            }

            Character character = CharacterManager.getActiveCharacter(player.getUUID());
            if (character != null) {
                Component component = Component.literal(character.getName() + " throws a " + heldItem.getDisplayName())
                        .setStyle(Style.EMPTY.withColor(TextColor.parseColor(character.getEmoteColor())));
                sendLocalMessage(player, 10, component);
            }
        }
    }
}
