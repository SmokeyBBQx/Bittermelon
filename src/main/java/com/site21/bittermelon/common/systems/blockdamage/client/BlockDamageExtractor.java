package com.site21.bittermelon.common.systems.blockdamage.client;

import com.site21.bittermelon.common.systems.blockdamage.BlockDamageData;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageUtil;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.BlockBreakingRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.IRenderableSection;

import java.util.ArrayList;
import java.util.List;

public class BlockDamageExtractor {

    public static void extractBlockDamageRenderStates(LevelRenderState renderState, LevelRenderer levelRenderer,
                                                      Frustum frustum, Level level) {
        List<BlockBreakingRenderState> breakingBlocks = new ArrayList<>();

        for (IRenderableSection section : levelRenderer.getRenderableSections()) {
            BlockDamageData data = BlockDamageUtil.getBlockDamageData(level, section.getRenderOrigin());
            for (BlockPos damagedPos : data.getBlockDamages().keySet()) {
                if (!section.getBoundingBox().contains(damagedPos.getX(), damagedPos.getY(), damagedPos.getZ())) continue;
                if (!frustum.isVisible(new AABB(damagedPos))) continue;

                BlockState blockState = level.getBlockState(damagedPos);
                int visualDamage = data.getVisualBlockDamage(damagedPos);
                breakingBlocks.add(new BlockBreakingRenderState(damagedPos, blockState, visualDamage));
            }
        }

        renderState.blockBreakingRenderStates.addAll(breakingBlocks);
    }
}
