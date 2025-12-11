package com.site21.bittermelon.common.systems.medical.compartment;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LayerSlot {
    private UUID instanceId;
    private float visibility;
    private float bloodLevel;

    public LayerSlot() {
        this.instanceId = null;
        this.visibility = 0;
        this.bloodLevel = 0;
    }

    @Nullable
    public UUID getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(UUID instanceId) {
        this.instanceId = instanceId;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public float getBloodLevel() {
        return bloodLevel;
    }

    public void setBloodLevel(float bloodLevel) {
        this.bloodLevel = bloodLevel;
    }

    public boolean isOccupied() {
        return instanceId != null;
    }


}
