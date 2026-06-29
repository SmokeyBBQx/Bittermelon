package com.site21.bittermelon.common.systems.fluid.substance.client;

import com.site21.bittermelon.common.systems.fluid.substance.SubstanceFluidBlockEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Vector4f;

public class ClientSubstanceFluid implements IClientFluidTypeExtensions {
    @Override
    public void modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
        if (level.getBlockEntity(BlockPos.containing(camera.position())) instanceof SubstanceFluidBlockEntity fluidBE) {
            int color = fluidBE.getColor();
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;
            fluidFogColor.set(r, g, b, 255f);
        }
    }
}
