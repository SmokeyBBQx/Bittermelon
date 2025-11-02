package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.fluid.SubstanceFluid;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BitterFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Bittermelon.MOD_ID);

    public static final Supplier<SubstanceFluid> SUBSTANCE_FLUID = FLUIDS.register("substance_fluid", () ->
            new SubstanceFluid(BitterItems.SUBSTANCE_FLUID_BUCKET)
    );
}
