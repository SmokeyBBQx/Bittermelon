package com.site21.bittermelon.util;

import com.site21.bittermelon.content.substance.SubstanceStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SubstanceUtils {
    public static final float GAS_CONSTANT = 8.3144f; // 8.3144 L * kPa / K * mol

    /**
     * Get the total amount of substance in a list of SubstanceStacks.
     * @param substances the list of SubstanceStacks
     * @return the total amount of substance in moles
     */
    public static float getTotalAmount(@NotNull List<SubstanceStack> substances) {
        return substances.stream()
                .map(SubstanceStack::getVolume)
                .reduce(0f, Float::sum);
    }

    /**
     * Calculate the pressure of an ideal gas using the Ideal Gas Law: P = nRT/V
     * @param substances the list of SubstanceStacks
     * @param volume the volume in liters
     * @param temperature the temperature in Kelvin
     * @return the pressure in kPa
     */
    public static float getPressure(List<SubstanceStack> substances, float volume, float temperature) {
        return SubstanceUtils.getTotalAmount(substances) * GAS_CONSTANT * temperature / volume;
    }
}
