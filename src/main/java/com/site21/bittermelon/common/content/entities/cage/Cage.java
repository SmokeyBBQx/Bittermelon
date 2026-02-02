package com.site21.bittermelon.common.content.entities.cage;

import com.site21.bittermelon.common.content.entities.cage.client.BlockInfo;
import com.site21.bittermelon.init.neoforge.BitterEntities;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Cage extends Entity {
    private final List<BlockInfo> blocks;

    public Cage(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocks = new ArrayList<>();
    }

    public static Cage create(Level level, List<BlockInfo> blocks) {
        Cage cage = new Cage(BitterEntities.CAGE.get(), level);
        cage.blocks.addAll(blocks);
        return cage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }

    public List<BlockInfo> getBlocks() {
        return blocks;
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
    public boolean isNoGravity() {
        return false; // Ensure gravity is enabled
    }

    @Override
    protected double getDefaultGravity() {
        return 0.02;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
//        int width = 0;
//        int height = 0;
//
//        for (BlockInfo info : blocks) {
//            if (info.offset().getX() + 1 > width) {
//                width = info.offset().getX() + 1;
//            }
//            if (info.offset().getY() + 1 > height) {
//                height = info.offset().getY() + 1;
//            }
//        }
//
//        return EntityDimensions.scalable(width, height);
        return EntityDimensions.fixed(4, 4);
    }
}
