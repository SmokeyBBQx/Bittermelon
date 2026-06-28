package com.site21.bittermelon.common.content.entities.scp815snake;

import com.site21.bittermelon.init.neoforge.BitterItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
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

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (!level().isClientSide()) {
            player.addItem(new ItemStack(BitterItems.SCP_815_SNAKE_HAND.get()));
            discard();
        }
        return InteractionResult.SUCCESS;
    }
}
