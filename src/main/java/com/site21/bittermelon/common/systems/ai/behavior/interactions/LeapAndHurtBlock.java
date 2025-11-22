package com.site21.bittermelon.common.systems.ai.behavior.interactions;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageHelper;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

public class LeapAndHurtBlock<E extends Mob> extends DelayedBehaviour<E> {
    protected BiFunction<E, BlockPos, Float> verticalJumpStrength = (entity, target) -> 0.3f;
    protected BiFunction<E, BlockPos, Float> jumpStrength = (entity, target) -> 0.4f;
    protected BiFunction<E, BlockPos, Float> moveSpeedContribution = (entity, target) -> 0.2f;
    protected ToIntFunction<E> breakInterval = entity -> 120;
    private BlockPos breakTarget = null;

    public LeapAndHurtBlock(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MemoryTest.builder()
                .hasMemory(BitterMemoryTypes.BREAK_TARGET.get())
                .noMemory(MemoryModuleType.ATTACK_COOLING_DOWN);
    }

    @Override
    protected void start(@NotNull E entity) {
        breakTarget = BrainUtil.getMemory(entity, BitterMemoryTypes.BREAK_TARGET.get());
        entity.lookAt(EntityAnchorArgument.Anchor.EYES, breakTarget.getCenter());
    }

    @Override
    protected void doDelayedAction(E entity) {
        BrainUtil.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, breakInterval.applyAsInt(entity));
        leapAtTarget(entity, breakTarget);
    }

    @Override
    protected void stop(@NotNull E entity) {
        Level level = entity.level();

        int damage = (int) entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
        BlockDamageHelper.addDamage(level, breakTarget, damage);

        BrainUtil.clearMemory(entity, BitterMemoryTypes.BREAK_TARGET.get());

        entity.level().playSound(null, breakTarget, level.getBlockState(breakTarget).getSoundType().getBreakSound(),
                entity.getSoundSource(), 1.0f, entity.getRandom().nextFloat() * 0.4f + 0.8f);
    }

    private void leapAtTarget(@NotNull E entity, @NotNull BlockPos target) {
        Vec3 velocity = new Vec3(target.getX() - entity.getX(), 0, target.getZ() - entity.getZ());

        if (velocity.lengthSqr() > 1.0E-7)
            velocity = velocity.normalize().scale(jumpStrength.apply(entity, target)).add(entity.getDeltaMovement().scale(moveSpeedContribution.apply(entity, target)));

        entity.setDeltaMovement(velocity.x, verticalJumpStrength.apply(entity, target), velocity.z);
    }
}
