package com.site21.bittermelon.common.systems.fluid.simple;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientSimpleFluid implements IClientFluidTypeExtensions {
    @Override
    public @NotNull Identifier getStillTexture() {
        return Identifier.withDefaultNamespace("block/water_still");
    }

    @Override
    public @NotNull Identifier getStillTexture(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
//        if (getter.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity fluidBE) {
//            float volume = fluidBE.getVolume();
//            if (volume < 1f) {
//                return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_1");
//            } else if (volume < 2f) {
//                return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid");
//            } else {
//                return this.getStillTexture();
//            }
//
//        }
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
}
