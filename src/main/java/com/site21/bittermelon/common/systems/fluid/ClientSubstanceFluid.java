package com.site21.bittermelon.common.systems.fluid;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4f;

public class ClientSubstanceFluid implements IClientFluidTypeExtensions {
    @Override
    public @NotNull ResourceLocation getStillTexture() {
        return ResourceLocation.withDefaultNamespace("block/water_still");
    }

    @Override
    public @NotNull ResourceLocation getStillTexture(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
//        if (getter.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity fluidBE) {
//            float volume = fluidBE.getVolume();
//            if (volume < 1f) {
//                return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_1");
//            } else if (volume < 2f) {
//                return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid");
//            } else {
//                return this.getStillTexture();
//            }
//
//        }
        return this.getStillTexture();
    }

    @Override
    public @NotNull ResourceLocation getFlowingTexture() {
        return ResourceLocation.withDefaultNamespace("block/water_flowing");
    }


    @Override
    public int getTintColor(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
        if (getter.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity fluidBE) {
            int color = fluidBE.getColor();
            if (color == 0xFFAAD5DB) {
                for (Direction dir : Direction.values()) {
                    BlockPos neighborPos = pos.relative(dir);
                    if (getter.getBlockEntity(neighborPos) instanceof SubstanceFluidBlockEntity neighborBE) {
                        int neighborColor = neighborBE.getColor();
                        if (neighborColor != 0xFFAAD5DB) {
                            color = neighborBE.getColor();
                            break;
                        }
                    }
                }
            }

            return color;
        }
        return this.getTintColor();
    }

    @Override
    public @NotNull Vector4f modifyFogColor(@NotNull Camera camera, float partialTick, @NotNull ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
        if (level.getBlockEntity(camera.getBlockPosition()) instanceof SubstanceFluidBlockEntity fluidBE) {
            int color = fluidBE.getColor();
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;
            return new Vector4f(r, g, b, 255f);
        }
        return fluidFogColor;
    }

}
