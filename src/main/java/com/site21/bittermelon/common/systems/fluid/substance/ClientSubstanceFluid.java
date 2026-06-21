package com.site21.bittermelon.common.systems.fluid.substance;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.site21.bittermelon.common.systems.substance.Substance;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public class ClientSubstanceFluid implements IClientFluidTypeExtensions {
    @Override
    public @NotNull Identifier getStillTexture() {
        return Identifier.withDefaultNamespace("block/water_still");
    }

    @Override
    public @NotNull Identifier getStillTexture(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
        return this.getStillTexture();
    }

    @Override
    public @NotNull Identifier getFlowingTexture() {
        return Identifier.withDefaultNamespace("block/water_flow");
    }

    @Override
    public @Nullable Identifier getOverlayTexture() {
        return Identifier.withDefaultNamespace("block/water_overlay");
    }

    @Override
    public int getTintColor(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
        // TODO: This should be cached perhaps
//        if (getter.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity fluidBE) {
//            int color = fluidBE.getColor();
//            int alpha = ARGB.alpha(color);
//            int red = 0, green = 0, blue = 0, count = 0;
//
//            for (Direction direction : Direction.values()) {
//                BlockPos checkPos = pos.relative(direction);
//                if (getter.getBlockEntity(checkPos) instanceof SubstanceFluidBlockEntity be) {
//                    int c = be.getColor();
//                    if (c == Substance.DEFAULT_COLOR) continue;
//
//                    red += ARGB.red(c);
//                    green += ARGB.green(c);
//                    blue += ARGB.blue(c);
//                    count++;
//                }
//            }
//
//            if (count > 0 && color != Substance.DEFAULT_COLOR) {
//                red += ARGB.red(color);
//                green += ARGB.green(color);
//                blue += ARGB.blue(color);
//                count++;
//            }
//
//            return count > 0 ? ARGB.color(alpha, red / count, green / count, blue / count) : color;
//        }

        if (getter.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity fluidBE) {
            return fluidBE.getColor();
        }

        return getTintColor();
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

    @Override
    public boolean renderFluid(FluidState fluidState, BlockAndTintGetter getter, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState) {
        // Not really a fix but it prevents the blank fluids from rendering
        return IClientFluidTypeExtensions.of(fluidState).getTintColor(fluidState, getter, pos) == Substance.DEFAULT_COLOR;
    }
}
