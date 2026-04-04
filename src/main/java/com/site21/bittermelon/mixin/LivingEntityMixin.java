package com.site21.bittermelon.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.site21.bittermelon.init.neoforge.BitterFluids.SUBSTANCE_FLUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(
            method = "travelInFluid(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V",
                    ordinal = 0
            ),
            ordinal = 1
    )
    private float bittermelon$scaleFluidTravelSpeed(float vanillaSpeed, Vec3 travelVector, FluidState unused) {
        // TODO: Doesn't work
        FluidState state = level().getFluidState(blockPosition());
        if (!state.is(SUBSTANCE_FLUID.get())) return vanillaSpeed;
        return state.getAmount() < 4 ? 0 : vanillaSpeed;
    }
}
