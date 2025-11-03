package com.site21.bittermelon.common.systems.fluid;

import com.site21.bittermelon.init.neoforge.BitterBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterFluidTypes.SUBSTANCE_FLUID_TYPE;

public class SubstanceFluidType extends FluidType {
    public SubstanceFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public double motionScale(@NotNull Entity entity) {
        // TODO: Doesn't work

        if (entity.getFluidTypeHeight(SUBSTANCE_FLUID_TYPE.get()) <= 0.4) {
            return 5;
        }
        return super.motionScale(entity);
    }

    @Override
    public boolean canSwim(@NotNull Entity entity) {
        if (entity.getFluidTypeHeight(SUBSTANCE_FLUID_TYPE.get()) <= 0.4) {
            return false;
        }

        return super.canSwim(entity);
    }
}
