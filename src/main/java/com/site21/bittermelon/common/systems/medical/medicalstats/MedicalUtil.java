package com.site21.bittermelon.common.systems.medical.medicalstats;

import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MedicalUtil {
    public static @NotNull MedicalStats getMedicalStats(@NotNull Level level, UUID entityUUID) {
        return level.getEntity(entityUUID).getData(BitterAttachmentTypes.MEDICAL_STATS);
    }
}
