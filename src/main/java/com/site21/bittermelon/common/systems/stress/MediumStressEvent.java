package com.site21.bittermelon.common.systems.stress;

import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public enum MediumStressEvent {
    EXAMPLE(player -> {

    });

    public final Consumer<Player> effect;

    MediumStressEvent(Consumer<Player> effect) {
        this.effect = effect;
    }
}
