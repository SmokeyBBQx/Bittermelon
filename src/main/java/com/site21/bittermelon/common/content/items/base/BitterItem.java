package com.site21.bittermelon.common.content.items.base;

import com.site21.bittermelon.common.content.entities.implementations.ThrownItemProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BitterItem extends Item {
    public BitterItem(Properties properties) {
        super(properties);
    }

    public void projectileHitEntity(ItemStack stack, @NotNull Entity entity, @NotNull DamageSources damageSources, ThrownItemProjectile thrownItemProjectile, Entity owner, Vec3 velocity) {

    }

    public void projectileHitBlock(ItemStack stack, @NotNull Level level, @NotNull BlockPos pos) {

    }
}
