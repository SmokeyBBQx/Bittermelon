package com.site21.bittermelon.common.systems.fluid;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;

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
            return fluidBE.getColor();
        }
        return this.getTintColor();
    }


}
