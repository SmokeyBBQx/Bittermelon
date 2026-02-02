package com.site21.bittermelon.common.content.entities.cage.client;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public record BlockInfo(BlockState state, Vec3i offset) {
}
