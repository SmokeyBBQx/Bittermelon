package com.site21.bittermelon.common.systems.medical.wound;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Holder;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class BodyPartModels {
    public static final Map<Holder<BodyPart>, ModelPart> MODELS = new HashMap<>();
    public static final Map<Holder<BodyPart>, Vec3> REST_OFFSETS = new HashMap<>();

    public ModelPart get(Holder<BodyPart> holder) {
        return MODELS.get(holder);
    }

    public static void registerBodyPartModel(Holder<BodyPart> bodyPart, ModelPart modelPart) {
        MODELS.put(bodyPart, modelPart);
        REST_OFFSETS.put(bodyPart, new Vec3(modelPart.x, modelPart.y, modelPart.z));
    }
}
