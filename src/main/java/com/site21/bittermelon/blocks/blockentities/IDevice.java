package com.site21.bittermelon.blocks.blockentities;

import org.jetbrains.annotations.NotNull;

public interface IDevice {
    default @NotNull String generateAddress(String prefix) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder();

        id.append(prefix).append("-");

        for (int i = 0; i < 4; i++) {
            int index = (int)(Math.random() * characters.length());
            id.append(characters.charAt(index));
        }

        id.append("-");

        for (int i = 0; i < 4; i++) {
            int index = (int)(Math.random() * characters.length());
            id.append(characters.charAt(index));
        }

        return id.toString();
    }

    String getAddress();
}
