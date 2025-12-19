package com.site21.bittermelon.common.systems.medical.compartment;

import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.LAYERS;

public class CompartmentUtil {

    /**
     * Get layer data from compartment instance
     * @param compartment CompartmentInstance
     * @param layer Layer index
     * @return LayerData or null if not found
     */
    public static @Nullable LayerData getLayer(@NotNull CompartmentInstance compartment, int layer) {
        List<LayerData> layers = compartment.getOrDefault(LAYERS, List.of());
        if (layers.isEmpty()) return null;
        return layers.get(layer);
    }

    /**
     * Get layer grid from compartment instance
     * @param compartment CompartmentInstance
     * @param layer Layer index
     * @return LayerSlot[][] grid or null if not found
     */
    public static @Nullable LayerSlot[][] getLayerGrid(@NotNull CompartmentInstance compartment, int layer) {
        LayerData layerData = getLayer(compartment, layer);
        if (layerData == null) return null;

        return layerData.getGrid();
    }

    public static @NotNull List<LayerData> getLayers(@NotNull CompartmentInstance compartment) {
        return compartment.getOrDefault(LAYERS, List.of());
    }

    public static @Nullable LayerData getTopLayer(@NotNull CompartmentInstance compartment) {
        List<LayerData> layers = getLayers(compartment);
        if (layers.isEmpty()) return null;
        return layers.getFirst();
    }
}
