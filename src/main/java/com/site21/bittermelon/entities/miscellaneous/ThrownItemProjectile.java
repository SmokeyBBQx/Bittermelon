package com.site21.bittermelon.entities.miscellaneous;

import com.site21.bittermelon.items.base.BaseItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterEntities.THROWN_ITEM_PROJECTILE;

public class ThrownItemProjectile extends ThrowableItemProjectile {
    private final float BASE_GRAVITY = 0.03F;

    public ThrownItemProjectile(EntityType<? extends ThrownItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownItemProjectile(Level level, LivingEntity player, ItemStack itemStack) {
        super(THROWN_ITEM_PROJECTILE.get(), level);
        this.setItem(itemStack);
    }

    public ThrownItemProjectile(Level pLevel, double pX, double pY, double pZ) {
        super(THROWN_ITEM_PROJECTILE.get(), pX, pY, pZ, pLevel);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return this.getItem().getItem();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);

        if (!this.level().isClientSide) {
            if (this.getItem().getItem() instanceof BaseItem item) {
                item.projectileHitBlock(this.getItem(), this.level(), result.getBlockPos());
            } else {
                spawnAtLocation(this.getItem());
            }
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity (@NotNull EntityHitResult result){
        super.onHitEntity(result);
        Entity entity = result.getEntity();

        if (!this.level().isClientSide) {
            if (this.getItem().getItem() instanceof BaseItem item) {
                item.projectileHitEntity(this.getItem(), entity, this.damageSources(), this, this.getOwner());
            } else {
                float dmg = 1;
                dmg *= this.getItem().getCount();
                dmg /= this.getItem().getMaxStackSize() / 4f;
                entity.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        if (this.getItem().getItem() instanceof BaseItem item) {
            return BASE_GRAVITY + (float) item.getItemWeight().value / 100;
        }
        return BASE_GRAVITY;
    }
}

