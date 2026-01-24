package com.site21.bittermelon.common.systems.medical.damage;

import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;

import java.util.List;
import java.util.Random;

public class DamageGen {
    public static void placeRandomly(CompartmentInstance target, int layerIndex, CompartmentInstance injury) {
        LayerData layer = CompartmentUtil.getLayer(target, layerIndex);
        if (layer == null) throw new RuntimeException("Target compartment has no layer at index " + layerIndex);

        List<Point> shape = injury.getOrDefault(BitterDataComponents.SHAPE, List.of(new Point(0, 0)));
        int shapeWidth = shape.stream().mapToInt(Point::x).max().orElse(0) + 1;
        int shapeHeight = shape.stream().mapToInt(Point::y).max().orElse(0) + 1;

        int width = layer.getWidth() - shapeWidth;
        int height = layer.getHeight() - shapeHeight;

        Random random = new Random();
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int attempts = 0;

        while (!layer.canFit(x, y, shape) && attempts < 20) {
            x = random.nextInt(width);
            y = random.nextInt(height);
            attempts++;
        }

        CompartmentUtil.insertCompartment(target, injury, layerIndex, x, y);
    }

    public static void makeLaceration(CompartmentInstance target, int layerIndex) {
        LayerData layer = CompartmentUtil.getLayer(target, layerIndex);
        if (layer == null) throw new RuntimeException("Target compartment has no layer at index " + layerIndex);


    }
}
