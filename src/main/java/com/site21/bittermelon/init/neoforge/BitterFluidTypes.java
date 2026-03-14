package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.fluid.SubstanceFluidType;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class BitterFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Bittermelon.MOD_ID);

    public static final Supplier<FluidType> SUBSTANCE_FLUID_TYPE = FLUID_TYPES.register("substance_fluid_type", () ->
            new SubstanceFluidType(FluidType.Properties.create()
                    .canExtinguish(true)
                    .supportsBoating(true)
                    .isWaterLike(true)
            )
    );
}
