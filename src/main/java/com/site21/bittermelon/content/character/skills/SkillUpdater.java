package com.site21.bittermelon.content.character.skills;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.Bittermelon.LOGGER;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STEP_COUNTER;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class SkillUpdater {
    private static final int EXERCISE_STEP_THRESHOLD = 400;
    private static final float AGILITY_GAIN = 0.001f;
    private static final float AGILITY_LOSS = -0.001f;

    @SubscribeEvent
    public static void onPlayerTick(@NotNull PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        Character character = CharacterManager.get(player.level()).getActiveCharacter(player);

        if (character == null) return;

        handleAgility(player, character);
    }

    private static void handleAgility(@NotNull Player player, Character character) {
        if (player.isSprinting() || player.isSwimming()) {
            int updatedStepCounter = player.getData(STEP_COUNTER) + 1;

            if (updatedStepCounter > EXERCISE_STEP_THRESHOLD) {
                character.modifySkill(Skill.AGILITY, AGILITY_GAIN);
                player.setData(STEP_COUNTER, 0);

                LOGGER.info("{} gains agility. Their agility is now: {}", character.getName(), character.getSkill(Skill.AGILITY));
            } else {
                player.setData(STEP_COUNTER, updatedStepCounter);
            }
        }

        if (player.isSleeping()) {
            character.modifySkill(Skill.AGILITY, AGILITY_LOSS);
            logAgilityLoss(character);
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.@NotNull Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack item = event.getItem();
            FoodProperties foodProperties = item.getFoodProperties(player);
            if (foodProperties != null) {
                Character character = CharacterManager.get(player.level()).getActiveCharacter(player);
                if (character == null) return;
                character.modifySkill(Skill.AGILITY, (float) -foodProperties.nutrition() / 10000);
                logAgilityLoss(character);
            }
        }
    }

    private static void logAgilityLoss(@NotNull Character character) {
        LOGGER.info("{} loses agility. Their agility is now: {}", character.getName(), character.getSkill(Skill.AGILITY));
    }
}
