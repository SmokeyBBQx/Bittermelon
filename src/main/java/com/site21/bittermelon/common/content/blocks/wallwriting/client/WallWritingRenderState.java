package com.site21.bittermelon.common.content.blocks.wallwriting.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class WallWritingRenderState extends BlockEntityRenderState {
    public SignText text;
    public Direction facing;
    public AttachFace face;
}
