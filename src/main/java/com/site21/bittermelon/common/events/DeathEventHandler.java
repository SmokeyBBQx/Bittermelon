package com.site21.bittermelon.common.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.scp718.SCP718;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.EYEBALL_GROWTH;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class DeathEventHandler {

    @SubscribeEvent
    public static void onEntityDeath(@NotNull LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // TODO: Save dead NPC characters so they can be restored or looked back on

        if (!entity.level().isClientSide()) {
            if (!(entity instanceof Player)) {
                CharacterManager characterManager = CharacterManager.get(entity.level());
                List<Character> characters = characterManager.getCharactersByEntityUUID(entity.getUUID());
                List<Character> charactersToRemove = new ArrayList<>(characters);
                for (Character character : charactersToRemove) {
                    characterManager.removeCharacter(character.getId());
                }
            }

            if (entity.hasEffect(EYEBALL_GROWTH)) {
                SCP718.explodeFluid(entity.level(), entity.getX(), entity.getEyeY(), entity.getZ());
            }
        }
    }
}
