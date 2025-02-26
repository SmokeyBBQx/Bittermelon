package com.site21.bittermelon.content.blocks.devices.wiring;

public record Signal(Object value) {
    public boolean asBoolean() {
        if (value instanceof Boolean b) return b;
        if (value instanceof Number n) return n.doubleValue() >= 1;
        return true;
    }
}
