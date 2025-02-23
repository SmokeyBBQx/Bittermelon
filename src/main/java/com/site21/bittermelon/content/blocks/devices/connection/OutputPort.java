package com.site21.bittermelon.content.blocks.devices.connection;

import net.minecraft.core.BlockPos;

import java.util.function.Supplier;

public record OutputPort<T>(String id, Supplier<T> output, BlockPos pos) {
}
