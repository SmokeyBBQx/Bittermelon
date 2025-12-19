package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;

import java.util.List;
import java.util.UUID;

public record UpdateSlots(UUID characterId, UUID compartmentId, int layer, List<LayerData.SlotData> slots) {
}
