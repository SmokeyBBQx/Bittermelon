package com.site21.bittermelon.common.content.items.repairtool;

import com.site21.bittermelon.common.systems.blockdamage.BlockDamageUtil;
import com.site21.bittermelon.init.neoforge.BitterBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RepairToolItem extends Item {
    public static final ItemUseAnimation REPAIR_TOOL_ANIMATION = ItemUseAnimation.valueOf("BITTERMELON_REPAIR_TOOL");

    public RepairToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            player.startUsingItem(context.getHand());
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int remainingUseDuration) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (remainingUseDuration % 5 != 0) return;

        var hitResult = entity.pick(entity.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), 0.0F, false);
        if (hitResult.getType() != HitResult.Type.BLOCK) return;

        Vec3 pos = hitResult.getLocation();
        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
        spawnParticles(serverLevel, pos, entity.getLookAngle());
        level.playSound(null, blockPos, SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.PLAYERS, 0.25f, 2f);
        if (!level.getBlockState(blockPos).is(BitterBlockTags.UNREPAIRABLE)) {
            BlockDamageUtil.repairDamage(level, blockPos, 1);
        }
    }

    private void spawnParticles(@NotNull ServerLevel level, @NotNull Vec3 pos, @NotNull Vec3 angle) {
        level.sendParticles(
                ParticleTypes.ELECTRIC_SPARK,
                pos.x, pos.y, pos.z,
                10,
                angle.x * 0.2, angle.y * 0.2, angle.z * 0.2,
                0.25
        );
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return REPAIR_TOOL_ANIMATION;
    }
}
