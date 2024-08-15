package net.smokeybbq.bittermelon.events;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;
//import net.smokeybbq.bittermelon.miscellaneous.Stumble;

public class DamageHandler {

    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getSource().is(DamageTypes.FALL)) {
                Character character = CharacterManager.getActiveCharacter(player.getUUID());
                Compartment leftLeg = character.getMedicalStats().getCompartments().get("left_leg");
                Compartment rightLeg = character.getMedicalStats().getCompartments().get("right_leg");

                leftLeg.modifyHealth(-event.getAmount());
                rightLeg.modifyHealth(-event.getAmount());

//                Stumble stumble = new Stumble(CharacterManager.getServer().getPlayerList().getPlayer(character.getEntityUUID()));
            }
        }
    }
}
