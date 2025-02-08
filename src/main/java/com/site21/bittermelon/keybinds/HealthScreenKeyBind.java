package com.site21.bittermelon.keybinds;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.medical.client.gui.HealthScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

import java.util.Optional;

import static com.site21.bittermelon.init.BitterKeyBindings.HEALTH_SCREEN_KEY;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class HealthScreenKeyBind {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (HEALTH_SCREEN_KEY.get().consumeClick()) {
            openHealthScreen();
        }
    }

    public static void openHealthScreen() {
        Minecraft mc = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            HitResult hitResult = mc.hitResult;
            Character targetCharacter = null;
            Entity targetEntity = null;

            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHit = (EntityHitResult) hitResult;
                targetEntity = entityHit.getEntity();

                targetCharacter = CharacterManager.getInstance().getActiveCharacter(targetEntity.getUUID());
            }

            if (targetCharacter == null) {
                targetCharacter = CharacterManager.getInstance().getActiveCharacter(player.getUUID());
            }

            if (targetCharacter != null) {
                ItemStack heldItem = player.getMainHandItem();
                mc.setScreen(new HealthScreen(targetCharacter, player, heldItem));
            }
        }
    }
}
