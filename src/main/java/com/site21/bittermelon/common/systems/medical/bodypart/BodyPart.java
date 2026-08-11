package com.site21.bittermelon.common.systems.medical.bodypart;

import com.google.common.collect.ImmutableList;
import com.site21.bittermelon.common.systems.medical.bodypart.client.FaceUV;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.BODY_PART_REGISTRY;

public record BodyPart(
        int texW,
        int texH,
        Map<Direction, FaceUV> uvs,
        TissueType tissueType,
        int bloodColor,
        ImmutableList<Vec3> attachmentPoints,
        Vec3 offset,
        AABB boundingBox
) {
    public PartInstance toInstance(LimbSlot limbSlot) {
        return new PartInstance(this, false, limbSlot);
    }

    public PartInstance toInstance() {
        return toInstance(LimbSlot.NONE);
    }

    public Holder<BodyPart> builtInRegistryHolder() {
        return BODY_PART_REGISTRY.wrapAsHolder(this);
    }
}
