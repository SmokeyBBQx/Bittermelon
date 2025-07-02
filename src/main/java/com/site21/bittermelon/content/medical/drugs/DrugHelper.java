package com.site21.bittermelon.content.medical.drugs;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public final class DrugHelper {
    public static void ingestDrug(@NotNull LivingEntity entity, DrugInstance instance) {
        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);
        if (character == null) return;
        MedicalStats medicalStats = character.getMedicalStats();
        medicalStats.addDrug(instance);
    }
}
