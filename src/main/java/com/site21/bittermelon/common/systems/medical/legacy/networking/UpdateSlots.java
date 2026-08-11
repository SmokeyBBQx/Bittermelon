package com.site21.bittermelon.common.systems.medical.legacy.networking;

import com.site21.bittermelon.common.systems.medical.legacy.compartment.layer.LayerData;

import java.util.List;
import java.util.UUID;

public record UpdateSlots(UUID characterId, UUID compartmentId, int layer, List<LayerData.SlotData> slots) {
}
