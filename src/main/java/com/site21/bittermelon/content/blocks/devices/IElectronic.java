package com.site21.bittermelon.content.blocks.devices;

import com.site21.bittermelon.content.blocks.devices.connection.InputPort;
import com.site21.bittermelon.content.blocks.devices.connection.OutputPort;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IElectronic {
    Map<String, OutputPort<?>> getOutputPorts();
    Map<String, InputPort<?>> getInputPorts();

    @SuppressWarnings("unchecked")
    default <T> OutputPort<T> findOutputPort(String id) {
        return (OutputPort<T>) getOutputPorts().get(id);
    }

    @SuppressWarnings("unchecked")
    default <T> InputPort<T> findInputPort(String id) {
        return (InputPort<T>) getInputPorts().get(id);
    }

    default @NotNull String generateAddress(String prefix) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder();

        id.append(prefix).append("-");

        for (int i = 0; i < 4; i++) {
            int index = (int)(Math.random() * characters.length());
            id.append(characters.charAt(index));
        }

        return id.toString();
    }

    String getAddress();
}
