package com.site21.bittermelon.common.systems.character.skills;

import com.site21.bittermelon.common.systems.character.Character;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STEP_COUNTER;

public class SkillUpdater {
    private static final int EXERCISE_STEP_THRESHOLD = 400;
    private static final float AGILITY_GAIN = 0.001f;
    private static final float AGILITY_LOSS = -0.001f;

    public static void tickSkills(LivingEntity entity, Character character) {
        handleAgility(entity, character);
    }

    private static void handleAgility(@NotNull LivingEntity entity, Character character) {
        if (entity.isSprinting() || entity.isSwimming()) {
            int updatedStepCounter = entity.getData(STEP_COUNTER) + 1;

            if (updatedStepCounter > EXERCISE_STEP_THRESHOLD) {
                character.modifySkill(Skill.AGILITY, AGILITY_GAIN);
                entity.setData(STEP_COUNTER, 0);
            } else {
                entity.setData(STEP_COUNTER, updatedStepCounter);
            }
        }

        if (entity.isSleeping()) {
            character.modifySkill(Skill.AGILITY, AGILITY_LOSS);
        }
    }
}
