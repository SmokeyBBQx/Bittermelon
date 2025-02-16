package com.site21.bittermelon.content.medical.damage;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.combat.AttackTemplate;
import com.site21.bittermelon.content.combat.CombatHandler;
import com.site21.bittermelon.content.medical.compartments.CompartmentType;
import com.site21.bittermelon.content.medical.damage.generators.BluntForceTrauma;
import com.site21.bittermelon.content.medical.damage.generators.Stab;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.site21.bittermelon.init.BitterSounds.WRESTLE;
import static net.minecraft.world.damagesource.DamageTypes.MOB_ATTACK;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class DamageEventHandler {

    @SubscribeEvent
    public static void OnIncomingDamage(@NotNull LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Character character = CharacterManager.get(target.level()).getActiveCharacter(target);
        if (character == null) return;
        DamageSource source = event.getSource();

        if (source.is(MOB_ATTACK)) {
            return;
        }

        if (source.is(DamageTypes.PLAYER_ATTACK)) {
            event.setCanceled(true);
            ItemStack stack = source.getWeaponItem();
            // TODO: Implement damage type handling

            AttackTemplate attackTemplate;

            if (stack == null || stack.getItem() == Items.AIR) {
                attackTemplate = new AttackTemplate.AttackTemplateBuilder()
                        .setDamageSupplier(() -> new BluntForceTrauma(EnumSet.of(CompartmentType.SOFT_TISSUE, CompartmentType.HARD_TISSUE)))
                        .setArea(4)
                        .setDepthRange(1, 5)
                        .setDamage(15)
                        .setPriority(4)
                        .addModifier(MedicalStats::getMovement, 0.4f)
                        .addModifier(MedicalStats::getManipulation, 0.5f)
                        .addModifier(MedicalStats::getSight, 0.1f)
                        .setMessages(
                                "%s hits %s's %s"
                        )
                        .setSound(WRESTLE.get())
                        .build();
            } else {
                attackTemplate = new AttackTemplate.AttackTemplateBuilder()
                        .setDamageSupplier(Stab::new)
                        .setArea(2)
                        .setDepthRange(2, 6)
                        .setDamage(15)
                        .setPriority(4)
                        .addModifier(MedicalStats::getMovement, 0.4f)
                        .addModifier(MedicalStats::getManipulation, 0.5f)
                        .addModifier(MedicalStats::getSight, 0.1f)
                        .setMessages(
                                "%s stabs %s's %s with " + stack.getDisplayName().getString().toLowerCase(),
                                "%s pierces %s's %s with " + stack.getDisplayName().getString().toLowerCase()
                        )
                        .setSound(WRESTLE.get())
                        .build();
            }

            CombatHandler.handleAttack((LivingEntity) source.getEntity(), target, attackTemplate);
        }
    }
}
