package com.site21.bittermelon.content.medical.compartments;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.UUID;

public class LayerCompartment extends LayerData {

    public LayerCompartment(ResourceLocation backgroundTexture, String name, List<UUID> compartments) {
        super(backgroundTexture, name, compartments);
    }
}
