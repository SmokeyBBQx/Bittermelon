package com.site21.bittermelon.common.systems.medical.legacy.client.interaction;

import com.site21.bittermelon.common.systems.medical.legacy.compartment.layer.Point;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class InteractionWidget extends AbstractWidget {

    public InteractionWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    protected double getDistance(@NotNull Point a, @NotNull Point b) {
        return Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2));
    }
}
