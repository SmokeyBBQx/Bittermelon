package com.site21.bittermelon.content.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.base.structuralblock.StructuralBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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
    }

    private static double calculateBreakProgress(@NotNull Vec3 center, BlockPos pos, float radius) {
        double distance = Math.sqrt(center.distanceToSqr(Vec3.atCenterOf(pos)));
        double breakProgress = Math.max(0, 1.0 - (distance / radius));

        return Math.min(1.0, breakProgress);
    }
}
