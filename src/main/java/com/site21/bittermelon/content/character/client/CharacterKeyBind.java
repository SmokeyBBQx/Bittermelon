package com.site21.bittermelon.content.character.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.ClientHandler;
import com.site21.bittermelon.content.character.client.charactereditor.roleselection.RoleSelectionScreen;
import com.site21.bittermelon.content.character.client.characterselection.CharacterSelectionScreen;
import com.site21.bittermelon.content.character.networking.OpenCharacterScreenC2S;
import com.site21.bittermelon.content.character.networking.OpenCharacterScreenS2C;
import com.site21.bittermelon.content.medical.client.screen.networking.OpenHealthScreenC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterKeyBindings.CHARACTER_KEY;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class CharacterKeyBind {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (CHARACTER_KEY.get().consumeClick()) {
            openCharacterScreen();
        }
    }

    public static void openCharacterScreen() {
//        Minecraft.getInstance().setScreen(new RoleSelectionScreen(null, null));
        PacketDistributor.sendToServer(new OpenCharacterScreenC2S(Minecraft.getInstance().player.getUUID()));

//        HitResult hitResult = mc.hitResult;
//        if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
//            EntityHitResult entityHit = (EntityHitResult) hitResult;
//            PacketDistributor.sendToServer(new OpenHealthScreenC2S(player.getUUID(), entityHit.getEntity().getUUID()));
//        } else {
//            PacketDistributor.sendToServer(new OpenHealthScreenC2S(player.getUUID(), UUID.randomUUID()));
//        }

    }
}
