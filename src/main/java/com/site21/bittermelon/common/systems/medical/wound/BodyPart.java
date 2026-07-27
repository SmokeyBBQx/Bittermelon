package com.site21.bittermelon.common.systems.medical.wound;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.BODY_PART_REGISTRY;

public record BodyPart(
        int texW,
        int texH,
        int[][] uvs,
        TissueType tissueType,
        int bloodColor,
        ImmutableList<Vec3> attachmentPoints,
        Vec3 pivot,
        AABB boundingBox
) {
    public PartInstance toInstance(LimbSlot limbSlot) {
        return new PartInstance(this, limbSlot);
    }

    public PartInstance toInstance() {
        return toInstance(LimbSlot.NONE);
    }

    public Holder<BodyPart> builtInRegistryHolder() {
        return BODY_PART_REGISTRY.wrapAsHolder(this);
    }
}
