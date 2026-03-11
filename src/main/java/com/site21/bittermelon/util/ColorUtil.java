package com.site21.bittermelon.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ColorUtil {
    /**
     * Mixes multiple colors together based on their associated amounts.
     *
     * @param colors A map where keys are colors (as ARGB integers) and values are their respective amounts.
     * @return The resulting mixed color as an ARGB integer.
     */
    public static int mixColors(@NotNull Map<Integer, Integer> colors) {
        int totalAmount = 0;
        int redSum = 0, greenSum = 0, blueSum = 0;

        for (Map.Entry<Integer, Integer> entry : colors.entrySet()) {
            int color = entry.getKey();
            int amount = entry.getValue();

            totalAmount += amount;
            redSum += ((color >> 16) & 0xFF) * amount;
            greenSum += ((color >> 8) & 0xFF) * amount;
            blueSum += (color & 0xFF) * amount;
        }

        int red = Math.round((float) redSum / totalAmount);
        int green = Math.round((float) greenSum / totalAmount);
        int blue = Math.round((float) blueSum / totalAmount);

        return 0xFF000000 | (red << 16) | (green << 8) | blue;
    }

    /**
     * Mixes multiple colors together based on their associated amounts and returns the RGB components.
     *
     * @param colors A map where keys are colors (as ARGB integers) and values are their respective amounts.
     * @return An array containing the RGB components of the resulting mixed color.
     */
    @Contract("_ -> new")
    public static int @NotNull [] mixColorsRGB(@NotNull Map<Integer, Integer> colors) {
        int totalAmount = 0;
        int redSum = 0, greenSum = 0, blueSum = 0;

        for (Map.Entry<Integer, Integer> entry : colors.entrySet()) {
            int color = entry.getKey();
            int amount = entry.getValue();

            totalAmount += amount;
            redSum += ((color >> 16) & 0xFF) * amount;
            greenSum += ((color >> 8) & 0xFF) * amount;
            blueSum += (color & 0xFF) * amount;
        }

        if (totalAmount == 0) {
            return new int[]{0xAA, 0xD5, 0xDB}; // Default color if total amount is 0
        }

        int red = Math.round((float) redSum / totalAmount);
        int green = Math.round((float) greenSum / totalAmount);
        int blue = Math.round((float) blueSum / totalAmount);

        return new int[]{red, green, blue};
    }
}
