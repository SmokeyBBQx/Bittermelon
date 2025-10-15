package com.site21.bittermelon.systems.explosions;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.base.structuralblock.StructuralBlockEntity;
import com.site21.bittermelon.content.items.scps.scp2398.SCP2398;
import com.site21.bittermelon.systems.syncsound.SyncSoundEvent;
import com.site21.bittermelon.systems.syncsound.SyncSoundType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class ExplosionHandler {
    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.@NotNull Detonate event) {
        Explosion explosion = event.getExplosion();
        List<BlockPos> affectedBlocks = new ArrayList<>(event.getAffectedBlocks());
        event.getAffectedBlocks().clear();
        Level level = event.getLevel();

        for (BlockPos pos : affectedBlocks) {
            if (level.getBlockEntity(pos) instanceof StructuralBlockEntity structuralBlock) {
                double breakProgress = calculateBreakProgress(explosion.center(), pos, explosion.radius());
                structuralBlock.setBreakProgress((float) breakProgress + structuralBlock.getBreakProgress());
            }
        }

        List<Entity> affectedEntities = event.getAffectedEntities();
        affectedEntities.removeIf(entity -> {
            if (entity instanceof ItemEntity itemEntity) {
                ItemStack stack = itemEntity.getItem();
                return stack.getItem() instanceof SCP2398;
            }
            return false;
        });

        if (!event.getLevel().isClientSide) {
            NeoForge.EVENT_BUS.post(new SyncSoundEvent(level,
                    new BlockPos(
                            (int) explosion.center().x,
                            (int) explosion.center().y,
                            (int) explosion.center().z),
                    SyncSoundType.AMBIENT,
                    Component.literal("(explosion)")
                            .withColor(0xFF808080)
                            .withStyle(ChatFormatting.ITALIC),
                    (int) explosion.radius() * 4,
                    SoundEvents.GENERIC_EXPLODE.value()));
        }
    }

    private static double calculateBreakProgress(@NotNull Vec3 center, BlockPos pos, float radius) {
        double distance = Math.sqrt(center.distanceToSqr(Vec3.atCenterOf(pos)));
        double breakProgress = Math.max(0, 1.0 - (distance / radius));

        return Math.min(1.0, breakProgress);
    }
}
