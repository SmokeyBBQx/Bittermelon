package com.site21.bittermelon.util;

import java.util.Map;

public class ColorUtil {
    public static int mixColors(Map<Integer, Float> colors) {
        float totalAmount = 0;
        float redSum = 0, greenSum = 0, blueSum = 0;

        for (Map.Entry<Integer, Float> entry : colors.entrySet()) {
            int color = entry.getKey();
            float amount = entry.getValue();

            totalAmount += amount;
            redSum += ((color >> 16) & 0xFF) * amount;
            greenSum += ((color >> 8) & 0xFF) * amount;
            blueSum += (color & 0xFF) * amount;
        }

        if (totalAmount == 0) {
            return 0xFFAAD5DB;
        }

        int red = Math.round(redSum / totalAmount);
        int green = Math.round(greenSum / totalAmount);
        int blue = Math.round(blueSum / totalAmount);

        return (red << 16) | (green << 8) | blue;
    }

    public static int[] mixColorsRGB(Map<Integer, Float> colors) {
        float totalAmount = 0;
        float redSum = 0, greenSum = 0, blueSum = 0;

        for (Map.Entry<Integer, Float> entry : colors.entrySet()) {
            int color = entry.getKey();
            float amount = entry.getValue();

            totalAmount += amount;
            redSum += ((color >> 16) & 0xFF) * amount;
            greenSum += ((color >> 8) & 0xFF) * amount;
            blueSum += (color & 0xFF) * amount;
        }

        if (totalAmount == 0) {
            return new int[]{0xAA, 0xD5, 0xDB}; // Default color if total amount is 0
        }

        int red = Math.round(redSum / totalAmount);
        int green = Math.round(greenSum / totalAmount);
        int blue = Math.round(blueSum / totalAmount);

        return new int[]{red, green, blue};
    }
}
