package com.site21.bittermelon.common.content.entities.scp815snake;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class SCP815Snake extends LivingEntity {
    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
    public SCP815Snake(EntityType<? extends LivingEntity> entityType, net.minecraft.world.level.Level level) {
        super(entityType, level);
    }

    @Override protected SoundEvent getHurtSound(DamageSource source) { return null; }

    @Override protected SoundEvent getDeathSound() { return null; }
}
