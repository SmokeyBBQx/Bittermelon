package com.site21.bittermelon.common.content.blocks.electronics.intercom.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class PhoneCordRenderState extends BlockEntityRenderState {
    boolean isPhonePickedUp;
    Vec3 playerPos;
    Direction facing;
    public int startBlockLight = 0;
    public int endBlockLight = 0;
    public int startSkyLight = 15;
    public int endSkyLight = 15;
}
