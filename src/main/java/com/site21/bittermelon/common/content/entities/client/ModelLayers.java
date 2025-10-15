package com.site21.bittermelon.common.content.entities.client;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelLayers {
    public static final ModelLayerLocation SCP_939_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp939_layer"), "main");
    public static final ModelLayerLocation CHICKEN_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "chicken_layer"), "main");
}
