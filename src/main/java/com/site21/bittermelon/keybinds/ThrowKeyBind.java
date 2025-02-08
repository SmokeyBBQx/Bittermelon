package com.site21.bittermelon.keybinds;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.entities.miscellaneous.ThrownItemProjectile;
import com.site21.bittermelon.networking.server.ThrowItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterKeyBindings.THROW_ITEM_KEY;
import static com.site21.bittermelon.util.LocalMessageHelper.sendLocalMessage;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ThrowKeyBind {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (THROW_ITEM_KEY.get().consumeClick()) {
            PacketDistributor.sendToServer(new ThrowItem());
        }
    }

    public static void throwItemAsProjectile(@NotNull Player player) {
        ItemStack heldItem = player.getMainHandItem();
        Level level = player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!heldItem.isEmpty()) {
            ThrownItemProjectile projectile = new ThrownItemProjectile(player.level(), player, heldItem.copy());
            projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            player.level().addFreshEntity(projectile);
            heldItem.shrink(1);

            Character character = CharacterManager.getInstance().getActiveCharacter(player.getUUID());
            if (character != null) {
                Component component = Component.literal(character.getName() + " throws a " + heldItem.getHoverName().getString())
                        .setStyle(Style.EMPTY.withColor(character.getEmoteColor()));
                sendLocalMessage(player, 10, component);
            }
        }
    }
}
