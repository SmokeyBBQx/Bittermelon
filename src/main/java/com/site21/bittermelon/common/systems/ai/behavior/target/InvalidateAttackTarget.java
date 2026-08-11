package com.site21.bittermelon.common.systems.ai.behavior.target;

import com.site21.bittermelon.common.systems.medical.legacy.medicalstats.MedicalStats;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;

public class InvalidateAttackTarget<E extends LivingEntity> extends net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget<E> {
    @Override
    protected boolean isTargetInvalid(@NotNull E entity, @NotNull LivingEntity target) {
        if (entity.level() != target.level())
            return true;

        if (target.getData(MEDICAL_STATS) instanceof MedicalStats medicalStats) {
            return medicalStats.getConsciousness() <= 0;
        }

        return target.isDeadOrDying() || target.isRemoved();
    }
}
