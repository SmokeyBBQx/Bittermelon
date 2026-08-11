package com.site21.bittermelon.common.systems.medical.legacy.blood;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.common.systems.medical.legacy.component.MedicalTicker;
import com.site21.bittermelon.common.systems.medical.legacy.medicalstats.MedicalStats;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record BloodInfo(BloodType bloodType) implements MedicalTicker {
    public static final Codec<BloodInfo> CODEC = BloodType.CODEC.xmap(BloodInfo::new, BloodInfo::bloodType);

    @Override
    public void tick(@NotNull MedicalStats stats, @NotNull Level level) {
        if (level.getGameTime() % 20 != 0) return;



    }
}
