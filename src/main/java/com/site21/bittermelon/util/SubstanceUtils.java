package com.site21.bittermelon.util;

import com.site21.bittermelon.substance.SubstanceStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SubstanceUtils {
    public static final float GAS_CONSTANT = 8.3144f; // 8.3144 L * kPa / K * mol

    public static float getTotalAmount(@NotNull List<SubstanceStack> substances) {
        return substances.stream()
                .map(SubstanceStack::getVolume)
                .reduce(0f, Float::sum);
    }

    public static float getPressure(List<SubstanceStack> substances, float volume, float temperature) {
        return SubstanceUtils.getTotalAmount(substances) * GAS_CONSTANT * temperature / volume;
    }
}
