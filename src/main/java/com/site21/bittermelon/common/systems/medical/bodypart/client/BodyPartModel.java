package com.site21.bittermelon.common.systems.medical.bodypart.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public record BodyPartModel(ModelPart modelPart, Vec3 restOffset, Identifier texture) {
}
