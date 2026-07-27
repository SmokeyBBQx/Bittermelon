package com.site21.bittermelon.common.systems.medical.wound;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public record ClientWound(Wound wound, DynamicTexture texture, Identifier identifier) {

}
