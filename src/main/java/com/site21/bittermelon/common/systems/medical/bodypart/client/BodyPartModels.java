package com.site21.bittermelon.common.systems.medical.bodypart.client;

import com.site21.bittermelon.common.systems.medical.bodypart.BodyPart;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class BodyPartModels {
    public static final Map<Holder<BodyPart>, BodyPartModel> MODELS = new HashMap<>();

    public BodyPartModel get(Holder<BodyPart> holder) {
        return MODELS.get(holder);
    }

    public static void registerBodyPartModel(Holder<BodyPart> bodyPart, ModelPart modelPart, Identifier texture) {
        MODELS.put(bodyPart, new BodyPartModel(modelPart, new Vec3(modelPart.x, modelPart.y, modelPart.z), texture));
    }

    public static void registerBodyPartModel(Holder<BodyPart> bodyPart, ModelPart modelPart) {
        registerBodyPartModel(bodyPart, modelPart, DefaultPlayerSkin.getDefaultTexture());
//        var texture = new ClientAsset.ResourceTexture(Bittermelon.identifier("entity/player/test"));
//        registerBodyPartModel(bodyPart, modelPart, texture.texturePath());
    }
}
