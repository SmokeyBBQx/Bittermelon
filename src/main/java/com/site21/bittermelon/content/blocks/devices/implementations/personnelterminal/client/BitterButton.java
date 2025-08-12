package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class BitterButton extends Button {
    protected final WidgetSprites sprites;

    protected BitterButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration, WidgetSprites sprites) {
        super(x, y, width, height, message, onPress, createNarration);
        this.sprites = sprites;
    }

    public BitterButton(@NotNull Builder builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress,
                builder.createNarration, builder.sprites);
        if (builder.tooltip != null) {
            this.setTooltip(builder.tooltip);
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blitSprite(sprites.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.getFGColor();
        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    public static BitterButton.@NotNull Builder builder(Component message, OnPress onPress, WidgetSprites sprites) {
        return new BitterButton.Builder(message, onPress, sprites);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final Component message;
        private final OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private CreateNarration createNarration;
        private WidgetSprites sprites;

        public Builder(Component message, OnPress onPress, WidgetSprites sprites) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.message = message;
            this.onPress = onPress;
            this.sprites = sprites;
        }

        public BitterButton.Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public BitterButton.Builder width(int width) {
            this.width = width;
            return this;
        }

        public BitterButton.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public BitterButton.Builder bounds(int x, int y, int width, int height) {
            return this.pos(x, y).size(width, height);
        }

        public BitterButton.Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public BitterButton.Builder createNarration(CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public BitterButton build() {
            return this.build(BitterButton::new);
        }

        public BitterButton build(@NotNull Function<BitterButton.Builder, BitterButton> builder) {
            return builder.apply(this);
        }
    }
}
