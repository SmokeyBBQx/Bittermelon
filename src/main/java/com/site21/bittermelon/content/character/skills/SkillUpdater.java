package com.site21.bittermelon.content.character.skills;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.Bittermelon.LOGGER;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STEP_COUNTER;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.TIME_SINCE_LAST_EXERCISE;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class SkillUpdater {
    private static final int EXERCISE_STEP_THRESHOLD = 400;
    private static final int AGILITY_LOSS_INTERVAL = 400;

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
                character.modifySkill(Skill.AGILITY, 0.01f);
                player.setData(STEP_COUNTER, 0);
                player.setData(TIME_SINCE_LAST_EXERCISE, 0);

                LOGGER.info("{} gains agility. Their agility is now: {}", character.getName(), character.getSkill(Skill.AGILITY));
            } else {
                player.setData(STEP_COUNTER, updatedStepCounter);
            }
        } else {
            player.setData(TIME_SINCE_LAST_EXERCISE, player.getData(TIME_SINCE_LAST_EXERCISE) + 1);
        }

        if (player.isSleeping()) {
            handleAgilityLoss(player, character);
        }
    }

    private static void handleAgilityLoss(@NotNull Player player, Character character) {
        if (player.getData(TIME_SINCE_LAST_EXERCISE) >= AGILITY_LOSS_INTERVAL) {
                character.modifySkill(Skill.AGILITY, -0.00001f);
                player.setData(TIME_SINCE_LAST_EXERCISE, 0);
                LOGGER.info("{} loses agility. Their agility is now: {}", character.getName(), character.getSkill(Skill.AGILITY));
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.@NotNull Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack item = event.getItem();
            if (item.getFoodProperties(player) != null) {
                Character character = CharacterManager.get(player.level()).getActiveCharacter(player);
                handleAgilityLoss(player, character);
            }
        }
    }
}
