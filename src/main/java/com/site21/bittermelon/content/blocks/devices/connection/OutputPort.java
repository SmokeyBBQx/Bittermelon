package com.site21.bittermelon.content.blocks.devices.connection;

import java.util.function.Supplier;

public record OutputPort<T>(String id, Supplier<T> output) {
}
