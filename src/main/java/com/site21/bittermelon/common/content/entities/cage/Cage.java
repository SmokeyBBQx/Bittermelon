package com.site21.bittermelon.common.content.entities.cage;

import com.site21.bittermelon.common.content.entities.cage.client.BlockInfo;
import com.site21.bittermelon.init.neoforge.BitterDataSerializers;
import com.site21.bittermelon.init.neoforge.BitterEntities;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Cage extends Entity {
    private static final EntityDataAccessor<List<BlockInfo>> BLOCK_INFO = SynchedEntityData.defineId(
            Cage.class,
            BitterDataSerializers.BLOCK_INFO.get()
    );

    private AABB cachedBoundingBox = null;

    public Cage(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public static Cage create(Level level, List<BlockInfo> blocks) {
        Cage cage = new Cage(BitterEntities.CAGE.get(), level);
        cage.entityData.set(BLOCK_INFO, blocks);
        cage.refreshDimensions();
        return cage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCK_INFO, List.of());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        entityData.set(BLOCK_INFO, input.read("blocks", BlockInfo.CODEC.listOf()).orElse(List.of()));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.store("blocks", BlockInfo.CODEC.listOf(), getBlocks());
    }

    public List<BlockInfo> getBlocks() {
        return entityData.get(BLOCK_INFO);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity entity) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        applyGravity();
        move(MoverType.SELF, getDeltaMovement());

        if (onGround()) {
            setDeltaMovement(getDeltaMovement().multiply(0.7, 0.0, 0.7));
        }
    }

    @Override
    public void playerTouch(@NotNull Player player) {
        if (isVehicle() || !isPushable() || !player.isShiftKeyDown()) {
            return;
        }

        double dx = player.getX() - this.getX();
        double dz = player.getZ() - this.getZ();
        double distSq = dx * dx + dz * dz;

        if (distSq >= 0.0001) {
            double dist = Math.sqrt(distSq);
            double strength = Math.min(1.0 / dist, 1.0) * 0.05;
            push(-dx / dist * strength, 0.0, -dz / dist * strength);
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.02;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        level().playSound(null, pos, BitterSounds.DRAG.value(), getSoundSource(), 0.25f, 0.1f + random.nextFloat() * 0.2f);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        int width = 0;
        int height = 0;

        for (BlockInfo info : getBlocks()) {
            if (info.offset().getX() + 1 > width) {
                width = info.offset().getX() + 1;
            }
            if (info.offset().getY() + 1 > height) {
                height = info.offset().getY() + 1;
            }
        }

        return EntityDimensions.fixed(width, height);
    }

    @Override
    protected AABB makeBoundingBox(Vec3 position) {
        int minX = 0, maxX = 0;
        int minY = 0, maxY = 0;
        int minZ = 0, maxZ = 0;

        for (BlockInfo info : getBlocks()) {
            minX = Math.min(minX, info.offset().getX());
            maxX = Math.max(maxX, info.offset().getX() + 1);
            minY = Math.min(minY, info.offset().getY());
            maxY = Math.max(maxY, info.offset().getY() + 1);
            minZ = Math.min(minZ, info.offset().getZ());
            maxZ = Math.max(maxZ, info.offset().getZ() + 1);
        }

        return new AABB(
                position.x + minX, position.y + minY, position.z + minZ,
                position.x + maxX, position.y + maxY, position.z + maxZ
        );
    }

}
