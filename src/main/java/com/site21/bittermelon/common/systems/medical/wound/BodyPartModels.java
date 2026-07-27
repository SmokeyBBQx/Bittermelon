package com.site21.bittermelon.common.systems.medical.wound;

import com.site21.bittermelon.init.custom.BodyParts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.core.Holder;

import java.util.HashMap;
import java.util.Map;

public class BodyPartModels {
    public static final Map<Holder<BodyPart>, ModelPart> MODELS = new HashMap<>();

    public ModelPart get(Holder<BodyPart> holder) {
        return MODELS.get(holder);
    }

    public static void register() {
        EntityModelSet models = Minecraft.getInstance().getEntityModels();
        PlayerModel playerModel = new PlayerModel(models.bakeLayer(ModelLayers.PLAYER), false);
        MODELS.put(BodyParts.LEFT_ARM, playerModel.leftArm);
    }
}
