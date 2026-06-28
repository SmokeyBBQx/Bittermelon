package com.site21.bittermelon.common.content.entities.cage.client;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CageRenderState extends EntityRenderState {
    BlockPos pos;
    List<BlockInfo> blocks = new ArrayList<>();
    Map<BlockState, MovingBlockRenderState> movingBlocks = new HashMap<>();
}
