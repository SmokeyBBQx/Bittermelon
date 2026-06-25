package com.site21.bittermelon.common.content.blocks.electronics.thermometer.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.phys.Vec3;

public class ThermometerRenderState extends BlockEntityRenderState {
    String temperature;
    Vec3 offset;
    float rotation;
}
