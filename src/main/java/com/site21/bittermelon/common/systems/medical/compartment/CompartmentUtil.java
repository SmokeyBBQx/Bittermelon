package com.site21.bittermelon.common.systems.medical.compartment;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.LAYERS;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.MEDICAL_ATTRIBUTES;

@SuppressWarnings("LoggingSimilarMessage")
public class CompartmentUtil {

    /**
     * Get layer data from compartment instance
     * @param compartment CompartmentInstance
     * @param layerIndex Layer index
     * @return LayerData or null if not found
     */
    public static @Nullable LayerData getLayer(@NotNull CompartmentInstance compartment, int layerIndex) {
        List<LayerData> layers = compartment.getOrDefault(LAYERS, List.of());
        if (layers.isEmpty()) {
            Bittermelon.LOGGER.error("Compartment {} has no layers!", compartment.getId());
            return null;
        }

        LayerData layer = layers.get(layerIndex);
        if (layer == null)
            Bittermelon.LOGGER.error("Compartment {} has no layer at index {}!", compartment.getId(), layerIndex);

        return layer;
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

    /**
     * Get all layers from compartment instance
     * @param compartment CompartmentInstance
     * @return List of LayerData
     */
    public static @NotNull List<LayerData> getLayers(@NotNull CompartmentInstance compartment) {
        return compartment.getOrDefault(LAYERS, List.of());
    }

    /**
     * Get the top layer from compartment instance
     * @param compartment CompartmentInstance
     * @return Top LayerData or null if not found
     */
    public static @Nullable LayerData getTopLayer(@NotNull CompartmentInstance compartment) {
        return getLayer(compartment, 0);
    }

    /**
     * Set medical attribute value in compartment instance
     * @param compartment CompartmentInstance
     * @param attribute MedicalAttribute
     * @param value Value to set
     */
    public static void setAttribute(@NotNull CompartmentInstance compartment, @NotNull MedicalAttribute attribute,
                                    float value) {
        EnumMap<MedicalAttribute, Float> attributes = compartment.getOrDefault(MEDICAL_ATTRIBUTES,
                new EnumMap<>(MedicalAttribute.class));
        attributes.put(attribute, value);
        compartment.set(MEDICAL_ATTRIBUTES, attributes);
    }

    /**
     * Insert a compartment into a parent compartment at specified layer and position
     * @param parent Parent CompartmentInstance
     * @param child Child CompartmentInstance to insert
     * @param layer Layer index to insert into
     * @param x X position in the layer grid
     * @param y Y position in the layer grid
     * @return true if insertion was successful, false otherwise
     */
    public static boolean insertCompartment(@NotNull CompartmentInstance parent, @NotNull CompartmentInstance child,
                                            int layer, int x, int y) {
        LayerData targetLayer = getLayer(parent, layer);
        if (targetLayer == null) {
            Bittermelon.LOGGER.error("Failed to insert compartment {} into parent {}: target layer {} not found!",
                    child.getId(), parent.getId(), layer);
            return false;
        }

        if(!targetLayer.tryToPlace(x, y, child)) return false;

        updateLayer(parent, layer, targetLayer);
        return true;
    }

    /**
     * Extract a compartment from a parent compartment at specified layer
     * @param parent Parent CompartmentInstance
     * @param child Child CompartmentInstance to extract
     * @param layer Layer index to extract from
     * @return true if extraction was successful, false otherwise
     */
    public static boolean extractCompartment(@NotNull CompartmentInstance parent, @NotNull CompartmentInstance child,
                                             int layer) {
        LayerData targetLayer = getLayer(parent, layer);
        if (targetLayer == null) {
            Bittermelon.LOGGER.error("Failed to extract compartment {} from parent {}: target layer {} not found!",
                    child.getId(), parent.getId(), layer);
            return false;
        }

        targetLayer.removeInstance(child.getId());
        updateLayer(parent, layer, targetLayer);
        return true;
    }

    /**
     * Extract a compartment from a parent compartment at specified layer by child ID
     * @param parent Parent CompartmentInstance
     * @param childId UUID of Child CompartmentInstance to extract
     * @param layer Layer index to extract from
     * @return true if extraction was successful, false otherwise
     */
    public static boolean extractCompartment(@NotNull CompartmentInstance parent, @NotNull UUID childId,
                                             int layer) {
        LayerData targetLayer = getLayer(parent, layer);
        if (targetLayer == null) {
            Bittermelon.LOGGER.error("Failed to extract compartment {} from parent {}: target layer {} not found!",
                    childId, parent.getId(), layer);
            return false;
        }

        targetLayer.removeInstance(childId);
        updateLayer(parent, layer, targetLayer);
        return true;
    }

    /**
     * Move a compartment from one parent compartment to another at specified layers and position
     * @param from Source CompartmentInstance
     * @param to Destination CompartmentInstance
     * @param target CompartmentInstance to move
     * @param fromLayerIndex Layer index in source compartment
     * @param toLayerIndex Layer index in destination compartment
     * @param toX X position in destination layer grid
     * @param toY Y position in destination layer grid
     * @return true if move was successful, false otherwise
     */
    public static boolean moveCompartment(@NotNull CompartmentInstance from, CompartmentInstance to, CompartmentInstance target,
                                          int fromLayerIndex, int toLayerIndex, int toX, int toY) {
        if (!extractCompartment(from, target, fromLayerIndex)) return false;
        return insertCompartment(to, target, toLayerIndex, toX, toY);
    }

    /**
     * Update layer data in compartment instance
     * @param compartment CompartmentInstance
     * @param layerIndex Layer index to update
     * @param newLayer New LayerData
     */
    public static void updateLayer(@NotNull CompartmentInstance compartment, int layerIndex, @NotNull LayerData newLayer) {
        List<LayerData> layers = new ArrayList<>(getLayers(compartment));
        if (layerIndex > layers.size()) {
            Bittermelon.LOGGER.error("Failed to update layer {} in compartment {}: layer index out of bounds!",
                    layerIndex, compartment.getId());
            return;
        }
        compartment.set(LAYERS, layers);
    }
}
