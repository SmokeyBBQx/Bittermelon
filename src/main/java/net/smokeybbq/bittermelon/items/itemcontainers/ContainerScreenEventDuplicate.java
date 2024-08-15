/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.smokeybbq.bittermelon.items.itemcontainers;

import net.minecraft.client.gui.GuiGraphics;
import net.smokeybbq.bittermelon.items.itemcontainers.AbstractContainerScreenDuplicate;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired for hooking into {@link AbstractContainerScreenDuplicate} events.
 * See the subclasses to listen for specific events.
 *
 * <p>These events are fired on the {@linkplain MinecraftForge#EVENT_BUS main Forge event bus},
 * only on the {@linkplain LogicalSide#CLIENT logical client}.</p>
 *
 * @see Render.Foreground
 * @see Render.Background
 */
public abstract class ContainerScreenEventDuplicate extends Event
{
    private final AbstractContainerScreenDuplicate<?> containerScreen;

    @ApiStatus.Internal
    protected ContainerScreenEventDuplicate(AbstractContainerScreenDuplicate<?> containerScreen)
    {
        this.containerScreen = containerScreen;
    }

    /**
     * {@return the container screen}
     */
    public AbstractContainerScreenDuplicate<?> getContainerScreen()
    {
        return containerScreen;
    }

    /**
     * Fired every time an {@link AbstractContainerScreenDuplicate} renders.
     * See the two subclasses to listen for foreground or background rendering.
     *
     * <p>These events are fired on the {@linkplain MinecraftForge#EVENT_BUS main Forge event bus},
     * only on the {@linkplain LogicalSide#CLIENT logical client}.</p>
     *
     * @see Foreground
     * @see Background
     */
    public static abstract class Render extends ContainerScreenEventDuplicate
    {
        private final GuiGraphics guiGraphics;
        private final int mouseX;
        private final int mouseY;

        @ApiStatus.Internal
        protected Render(AbstractContainerScreenDuplicate<?> guiContainer, GuiGraphics guiGraphics, int mouseX, int mouseY)
        {
            super(guiContainer);
            this.guiGraphics = guiGraphics;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        }

        /**
         * {@return the gui graphics used for rendering}
         */
        public GuiGraphics getGuiGraphics()
        {
            return guiGraphics;
        }

        /**
         * {@return the X coordinate of the mouse pointer}
         */
        public int getMouseX()
        {
            return mouseX;
        }

        /**
         * {@return the Y coordinate of the mouse pointer}
         */
        public int getMouseY()
        {
            return mouseY;
        }

        /**
         * Fired after the container screen's foreground layer and elements are drawn, but
         * before rendering the tooltips and the item stack being dragged by the player.
         *
         * <p>This can be used for rendering elements that must be above other screen elements, but
         * below tooltips and the dragged stack, such as slot or item stack specific overlays.</p>
         *
         * <p>This event is not {@linkplain Cancelable cancellable}, and does not {@linkplain HasResult have a result}.</p>
         *
         * <p>This event is fired on the {@linkplain MinecraftForge#EVENT_BUS main Forge event bus},
         * only on the {@linkplain LogicalSide#CLIENT logical client}.</p>
         */
        public static class Foreground extends Render
        {
            @ApiStatus.Internal
            public Foreground(AbstractContainerScreenDuplicate<?> guiContainer, GuiGraphics guiGraphics, int mouseX, int mouseY)
            {
                super(guiContainer, guiGraphics, mouseX, mouseY);
            }
        }

        /**
         * Fired after the container screen's background layer and elements are drawn.
         * This can be used for rendering new background elements.
         *
         * <p>This event is not {@linkplain Cancelable cancellable}, and does not {@linkplain HasResult have a result}.</p>
         *
         * <p>This event is fired on the {@linkplain MinecraftForge#EVENT_BUS main Forge event bus},
         * only on the {@linkplain LogicalSide#CLIENT logical client}.</p>
         */
        public static class Background extends Render
        {
            @ApiStatus.Internal
            public Background(AbstractContainerScreenDuplicate<?> guiContainer, GuiGraphics guiGraphics, int mouseX, int mouseY)
            {
                super(guiContainer, guiGraphics, mouseX, mouseY);
            }
        }
    }
}
