package com.site21.bittermelon.common.systems.medical.wound;

import net.minecraft.util.StringRepresentable;

public enum LimbSlot implements StringRepresentable {
    NONE("none"),
    ROOT("root"),
    HEAD("head"),
    BODY("body"),
    LEFT_ARM("left_arm"),
    RIGHT_ARM("right_arm"),
    LEFT_LEG("left_leg"),
    RIGHT_LEG("right_leg"),
    LEFT_HAND("left_hand"),
    RIGHT_HAND("right_hand");

    public final String identifier;

    LimbSlot(String identifier) {
        this.identifier = identifier;
    }

    public static LimbSlot fromIdentifier(String identifier) {
        for (LimbSlot slot : LimbSlot.values()) {
            if (slot.identifier.equals(identifier)) {
                return slot;
            }
        }
        return null;
    }

    @Override
    public String getSerializedName() {
        return identifier;
    }
}
