package com.site21.bittermelon.common.content.items.wire.client;

import net.minecraft.world.phys.Vec3;

public class WireState {
    public Vec3 offset = Vec3.ZERO;
    public Vec3 playerPos = Vec3.ZERO;
    public Vec3 blockPos = Vec3.ZERO;
    public int startBlockLight = 0;
    public int endBlockLight = 0;
    public int startSkyLight = 15;
    public int endSkyLight = 15;
    public boolean slack = true;
    public float r = 0.25f;
    public float g = 0.25f;
    public float b = 0.25f;
    public float thickness = 0.025f;
}
