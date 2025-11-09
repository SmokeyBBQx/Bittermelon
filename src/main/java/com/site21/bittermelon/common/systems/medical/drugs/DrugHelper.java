package com.site21.bittermelon.common.systems.medical.drugs;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public final class DrugHelper {
    public static void ingestDrug(@NotNull LivingEntity entity, DrugInstance instance) {
        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);
        if (character == null) return;
        MedicalStats medicalStats = character.getMedicalStats();
        medicalStats.addDrug(instance);
    }

    public static boolean doesEntityHaveDrug(@NotNull LivingEntity entity, Holder<Drug> drug) {
        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);
        if (character == null) return false;
        return character.getMedicalStats().getActiveDrugs().stream()
                .anyMatch(drugInstance -> drugInstance.getDrug().is(drug));
    }
}
