package com.site21.bittermelon.common.systems.ai.behavior.attack;

import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

import static com.site21.bittermelon.init.neoforge.BitterSounds.WRESTLE;
import static net.minecraft.world.entity.ai.memory.MemoryModuleType.NEAREST_LIVING_ENTITIES;

public class CollectivePush<E extends Mob> extends AnimatableMeleeAttack<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemories(MemoryModuleType.ATTACK_TARGET, NEAREST_LIVING_ENTITIES)
            .noMemories(BitterMemoryTypes.COLLECTIVE_PUSH_COOLDOWN.get(), MemoryModuleType.ATTACK_COOLING_DOWN);

    protected final int minAccomplices;
    protected final double maxDistance;

    protected BiFunction<E, LivingEntity, Float> verticalJumpStrength = (entity, target) -> 0.3f;
    protected BiFunction<E, LivingEntity, Float> jumpStrength = (entity, target) -> 0.4f;
    protected BiFunction<E, LivingEntity, Float> moveSpeedContribution = (entity, target) -> 0.2f;
    protected ToIntFunction<E> pushIntervalSupplier = entity -> 120;

    protected List<LivingEntity> nearbyAccomplices;

    public CollectivePush(int minAccomplices, double maxDistance, int delayTicks) {
        super(delayTicks);
        this.minAccomplices = minAccomplices;
        this.maxDistance = maxDistance;
    }

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    /**
     * Set the jump strength for the leap, scaled by the distance to be jumped
     *
     * @param function The jump strength function
     * @return this
     */
    public CollectivePush<E> jumpStrength(BiFunction<E, LivingEntity, Float> function) {
        this.jumpStrength = function;

        return this;
    }

    /**
     * Set the additional vertical velocity added when leaping.<br>
     * This value is not normally scaled by the distance being lept
     *
     * @param function The vertical jump strength function
     * @return this
     */
    public CollectivePush<E> verticalJumpStrength(BiFunction<E, LivingEntity, Float> function) {
        this.verticalJumpStrength = function;

        return this;
    }

    /**
     * Set the amount that the entity's existing velocity contributes to the jump's velocity.<br>
     * The returned value here acts as a percentage of the entity's existing velocity.<br>
     * {@code 0.2f = 20% of the existing velocity added to the jump}
     *
     * @param function The velocity contribution function
     * @return this
     */
    public CollectivePush<E> moveSpeedContribution(BiFunction<E, LivingEntity, Float> function) {
        this.moveSpeedContribution = function;

        return this;
    }

    /**
     * Set the cooldown interval after performing the collective push
     *
     * @param function The cooldown interval function
     * @return this
     */
    public CollectivePush<E> pushInterval(ToIntFunction<E> function) {
        this.pushIntervalSupplier = function;

        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
        super.checkExtraStartConditions(level, entity);

        if (target == null || StumbleHandler.isStumbled(target) || StumbleHandler.isStumbled(entity)) return false;

        List<LivingEntity> nearbyEntities = BrainUtil.getMemory(entity, NEAREST_LIVING_ENTITIES);

        assert nearbyEntities != null;
        nearbyAccomplices = nearbyEntities.stream()
                .filter(e -> isValidAccomplice(e, entity))
                .toList();

        return nearbyAccomplices.size() >= minAccomplices;
    }

    private boolean isValidAccomplice(@NotNull LivingEntity entity, @NotNull E self) {
        AABB aabb = target.getBoundingBox().inflate(maxDistance, 0, maxDistance);
        return entity.getType() == self.getType()
                && aabb.intersects(entity.getOnPos())
                && !StumbleHandler.isStumbled(entity)
                && !BrainUtil.hasMemory(entity, BitterMemoryTypes.COLLECTIVE_PUSH_COOLDOWN.get());
    }

    @Override
    protected void doDelayedAction(E entity) {
        super.doDelayedAction(entity);

        applyMemoriesAndLeap(entity);
        nearbyAccomplices.forEach(accomplice -> applyMemoriesAndLeap((E) accomplice));

        entity.level().playSound(null, entity.getOnPos(), WRESTLE.value(), SoundSource.AMBIENT);
        StumbleHandler.stumble(target, entity.getLookAngle());
    }

    private void applyMemoriesAndLeap(@NotNull E entity) {
        BrainUtil.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, attackInterval.applyAsInt(entity, target));
        BrainUtil.setForgettableMemory(entity, BitterMemoryTypes.COLLECTIVE_PUSH_COOLDOWN.get(),  true, pushIntervalSupplier.applyAsInt(entity));
        leapAtTarget(entity);
    }

    private void leapAtTarget(@NotNull E entity) {
        Vec3 velocity = new Vec3(target.getX() - entity.getX(), 0, target.getZ() - entity.getZ());

        if (velocity.lengthSqr() > 1.0E-7)
            velocity = velocity.normalize().scale(jumpStrength.apply(entity, target)).add(entity.getDeltaMovement().scale(moveSpeedContribution.apply(entity, target)));

        entity.setDeltaMovement(velocity.x, verticalJumpStrength.apply(entity, target), velocity.z);
    }
}
