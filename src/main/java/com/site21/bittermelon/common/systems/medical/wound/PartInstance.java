package com.site21.bittermelon.common.systems.medical.wound;

import net.minecraft.core.Holder;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartInstance {
    private final BodyPart bodyPart;
    private final Map<Vec3, PartInstance> attachedParts;
    private PartInstance parent;
    private final List<Wound> wounds;
    private LimbSlot limbSlot;

    public PartInstance(BodyPart bodyPart, LimbSlot limbSlot) {
        this.bodyPart = bodyPart;
        this.limbSlot = limbSlot;
        this.attachedParts = new HashMap<>();
        this.wounds = new ArrayList<>();
    }

    public BodyPart getBodyPart() {
        return bodyPart;
    }

    public Map<Vec3, PartInstance> getAttachedParts() {
        return attachedParts;
    }

    public void attachPart(Vec3 attachmentPoint, PartInstance part) {
        if (!bodyPart.attachmentPoints().contains(attachmentPoint)) {
            throw new IllegalArgumentException("Attachment point is not valid for this body part.");
        }

        attachedParts.put(attachmentPoint, part);
    }

    public PartInstance getParent() {
        return parent;
    }

    public void setParent(PartInstance parent) {
        this.parent = parent;
    }

    public List<Wound> getWounds() {
        return wounds;
    }

    public Holder<BodyPart> getPartHolder() {
        return bodyPart.builtInRegistryHolder();
    }

    public LimbSlot getLimbSlot() {
        return limbSlot;
    }

    public void setLimbSlot(LimbSlot limbSlot) {
        this.limbSlot = limbSlot;
    }
}
