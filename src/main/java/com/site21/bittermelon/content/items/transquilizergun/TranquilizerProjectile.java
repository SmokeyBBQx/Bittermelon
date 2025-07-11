package com.site21.bittermelon.content.items.transquilizergun;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TranquilizerProjectile extends AbstractArrow {
    protected TranquilizerProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super((EntityType<? extends AbstractArrow>) entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return null;
    }


}
