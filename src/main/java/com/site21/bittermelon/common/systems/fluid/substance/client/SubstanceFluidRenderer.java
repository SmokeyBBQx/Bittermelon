package com.site21.bittermelon.common.systems.fluid.substance.client;

import com.site21.bittermelon.common.systems.substance.Substance;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.CustomFluidRenderer;

import static net.minecraft.client.renderer.BiomeColors.WATER_COLOR_RESOLVER;

public class SubstanceFluidRenderer implements CustomFluidRenderer {
    @Override
    public boolean renderFluid(FluidRenderer fluidRenderer, FluidState fluidState, BlockAndTintGetter getter,
                               BlockPos pos, FluidRenderer.Output output, BlockState blockState) {
        return getter.getBlockTint(pos, WATER_COLOR_RESOLVER) == Substance.DEFAULT_COLOR;
    }
}
