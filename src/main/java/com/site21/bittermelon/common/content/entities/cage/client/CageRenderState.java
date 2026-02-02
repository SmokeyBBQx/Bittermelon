package com.site21.bittermelon.common.content.entities.cage.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.EmptyBlockAndTintGetter;

import java.util.ArrayList;
import java.util.List;

public class CageRenderState extends EntityRenderState {
    public BlockPos pos;
    public List<BlockInfo> blocks = new ArrayList<>();
    public BlockAndTintGetter level = EmptyBlockAndTintGetter.INSTANCE;
}
