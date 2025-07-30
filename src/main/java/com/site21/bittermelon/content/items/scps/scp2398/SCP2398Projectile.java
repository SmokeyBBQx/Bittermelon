package com.site21.bittermelon.content.items.scps.scp2398;

import com.site21.bittermelon.content.blocks.base.structuralblock.StructuralBlockEntity;
import com.site21.bittermelon.content.entities.miscellaneous.ThrownItemProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SCP2398Projectile extends ThrownItemProjectile {
    private static final Logger log = LoggerFactory.getLogger(SCP2398Projectile.class);
    private final ParticleOptions trailParticle = ParticleTypes.DUST_PLUME;

    public SCP2398Projectile(EntityType<? extends SCP2398Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public SCP2398Projectile(Level level, LivingEntity player, ItemStack stack) {
        super(level, player, stack, 0.7f, 50);
    }

    public SCP2398Projectile(Level pLevel, double pX, double pY, double pZ, ItemStack stack) {
        super(pLevel, pX, pY, pZ, stack, 0.7f, 50);
    }

    @Override
    public void tick() {
        super.tick();

        int particleFrequency = 2;
        if (!level().isClientSide && tickCount % particleFrequency == 0) {
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();

            for (int i = 0; i < 3; i++) {
                float particleSpread = 0.05f;
                double offsetX = (random.nextDouble() - 0.5) * particleSpread;
                double offsetY = (random.nextDouble() - 0.5) * particleSpread;
                double offsetZ = (random.nextDouble() - 0.5) * particleSpread;

                ((net.minecraft.server.level.ServerLevel) level()).sendParticles(
                        trailParticle,
                        x + offsetX,
                        y + offsetY,
                        z + offsetZ,
                        1,
                        0, 0, 0,
                        0.01
                );
            }
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (level().isClientSide) return;

        BlockPos pos = result.getBlockPos();
        ItemEntity itemEntity = new ItemEntity(level(), pos.getX(), pos.getY(), pos.getZ(), getItem());

        ((net.minecraft.server.level.ServerLevel) level()).sendParticles(
                ParticleTypes.DUST_PLUME,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                10,
                0.5, 0.5, 0.5,
                0.1
        );

        BlockState state = level().getBlockState(pos);
        level().playSound(null, getOnPos(), state.getSoundType(level(), pos, this).getBreakSound(), SoundSource.PLAYERS, 1, 1);

        if (state.getBlock() instanceof BellBlock block) {
            block.attemptToRing(level(), pos, result.getDirection());
        } else if (state.getBlock() instanceof ButtonBlock block) {
            block.press(state, level(), pos, null);
        }

        if (state.is(Tags.Blocks.GLASS_BLOCKS) || state.is(Tags.Blocks.GLASS_PANES)) {
            level().destroyBlock(pos, false, this);
        } else {
            if (level().getBlockEntity(pos) instanceof StructuralBlockEntity blockEntity) {
                blockEntity.setBreakProgress(blockEntity.getBreakProgress() + 0.4f);
            }
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
            level().addFreshEntity(itemEntity);
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        if (level().isClientSide) return;
        Entity entity = result.getEntity();

        if (entity instanceof ItemEntity || entity instanceof SCP2398Projectile) return;

        ((net.minecraft.server.level.ServerLevel) level()).sendParticles(
                ParticleTypes.CRIT,
                entity.getX(),
                entity.getY() + entity.getBbHeight() / 2.0,
                entity.getZ(),
                15,
                0.5, 0.5, 0.5,
                0.1
        );

        float dmg = 15;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);
        this.level().broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }
}
