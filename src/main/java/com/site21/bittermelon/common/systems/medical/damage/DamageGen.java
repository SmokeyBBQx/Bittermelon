package com.site21.bittermelon.common.systems.medical.damage;

import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerSlot;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.compartment.layer.SlotType;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.custom.Compartments;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DamageGen {

    public static @Nullable Point findRandomPoint(CompartmentInstance target, RandomSource random, int layerIndex,
                                                  List<Point> shape, int width, int height) {
        LayerData layer = CompartmentUtil.getLayer(target, layerIndex);
        if (layer == null) throw new RuntimeException("Target compartment has no layer at index " + layerIndex);

        int searchWidth = layer.getWidth() - width;
        int searchHeight = layer.getHeight() - height;

        int x = random.nextInt(searchWidth);
        int y = random.nextInt(searchHeight);
        int attempts = 0;

        while (!layer.canFit(x, y, 0, shape) && attempts < 20) {
            x = random.nextInt(searchWidth);
            y = random.nextInt(searchHeight);
            attempts++;
        }

        if (attempts >= 20) return null;

        return new Point(x, y);
    }

    public static void placeRandomly(CompartmentInstance target, int layerIndex, CompartmentInstance injury) {
        List<Point> shape = injury.getOrDefault(BitterDataComponents.SHAPE, List.of(new Point(0, 0)));
        int shapeWidth = shape.stream().mapToInt(Point::x).max().orElse(0) + 1;
        int shapeHeight = shape.stream().mapToInt(Point::y).max().orElse(0) + 1;

        Point point = findRandomPoint(target, RandomSource.create(), layerIndex, shape, shapeWidth, shapeHeight);
        if (point == null) return;

        CompartmentUtil.insertCompartment(target, injury, 0, layerIndex, point.x(), point.y());
    }

    public static void makeLaceration(MedicalStats medicalStats, RandomSource random, CompartmentInstance target,
                                      int layerIndex, int length, int depth) {
        Point start = findRandomPoint(target, random, layerIndex, List.of(new Point(0, 0)), 1, 1);
        if (start == null) return;

        makeLaceration(medicalStats, random, target, layerIndex, length, depth, start.x(), start.y());
    }

    public static void makeLaceration(MedicalStats medicalStats, RandomSource random, CompartmentInstance target,
                                      int layerIndex, int length, int depth, int x, int y) {
        LayerData layer = CompartmentUtil.getLayer(target, layerIndex);
        if (layer == null) throw new RuntimeException("Target compartment has no layer at index " + layerIndex);

        while (length > 0) {
            y++;
            if (random.nextBoolean()) {
                x--;
            }

            if (x < 0 || y < 0 || x >= layer.getWidth() || y >= layer.getHeight()) break;

            CompartmentInstance injury = getInjury(layer, x, y);
            if (injury == null) continue;

            medicalStats.addCompartment(injury);

            if (!(CompartmentUtil.insertCompartment(target, injury, layerIndex, x, y, 0, depth))) break;
            length--;
        }
    }

    public static @Nullable CompartmentInstance getInjury(LayerData layer, int x, int y) {
        LayerSlot slot = layer.getGrid()[y][x];
        if (slot == null) return null;

        if (slot.getType() == SlotType.BONE) return null;
        return Compartments.CUT.get().toInstance();
    }
}
