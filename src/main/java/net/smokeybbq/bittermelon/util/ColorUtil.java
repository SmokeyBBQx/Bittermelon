package net.smokeybbq.bittermelon.util;

import java.util.List;
import java.util.Map;

public class ColorUtil {
    public static int mixColors(List<Integer> colors) {
        int red = 0, green = 0, blue = 0;
        for (int color : colors) {
            red += (color >> 16) & 0xFF;
            green += (color >> 8) & 0xFF;
            blue += color & 0xFF;
        }
        int count = colors.size();
        return (red / count << 16) | (green / count << 8) | (blue / count);
    }

    public static int mixColors(int... colors) {
        int red = 0, green = 0, blue = 0;
        for (int color : colors) {
            red += (color >> 16) & 0xFF;
            green += (color >> 8) & 0xFF;
            blue += color & 0xFF;
        }
        int count = colors.length;
        return (red / count << 16) | (green / count << 8) | (blue / count);
    }

    public static int mixColors(Map<Integer, Integer> colors) {
        float totalAmount = 0;
        float redSum = 0, greenSum = 0, blueSum = 0;

        for (Map.Entry<Integer, Integer> entry : colors.entrySet()) {
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

}
