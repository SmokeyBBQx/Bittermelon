package com.site21.bittermelon.content.blocks.devices.connection;

import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public record InputPort<T>(String id, Consumer<T> action, BlockPos pos) {
}
