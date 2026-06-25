package com.site21.bittermelon.common.content.blocks.electronics.slidingdoor.client;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.phys.Vec3;

public class SlidingDoorState extends BlockEntityRenderState {
    Vec3 offset;
    MovingBlockRenderState movingBlock;
}
