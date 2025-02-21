package com.site21.bittermelon.content.blocks.devices.connection;

import java.util.function.Consumer;

public record InputPort<T>(String id, Consumer<T> action) {
}
