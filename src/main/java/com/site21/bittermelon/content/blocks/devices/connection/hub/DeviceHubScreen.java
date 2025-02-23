package com.site21.bittermelon.content.blocks.devices.connection.hub;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DeviceHubScreen extends Screen {
    private final IDeviceHub deviceHub;

    protected DeviceHubScreen(IDeviceHub deviceHub) {
        super(Component.literal("Device Hub"));
        this.deviceHub = deviceHub;
    }

    @Override
    protected void init() {
        super.init();


    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

    }
}
