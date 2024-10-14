package com.site21.bittermelon.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class DeathEventHandler {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // TODO: Save dead NPC characters so they can be restored or looked back on

        if (!entity.level().isClientSide()) {
            if (!(entity instanceof Player)) {
                CharacterManager characterManager = CharacterManager.getInstance();
                List<Character> characters = characterManager.getCharacters(entity.getUUID());
                List<Character> charactersToRemove = new ArrayList<>(characters);
                for (Character character : charactersToRemove) {
                    characterManager.removeCharacter(character);
                }
            }
        }
    }
}
