package com.site21.bittermelon.common.systems.character.client.charactereditor.roleselection;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.roles.Role;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


public class RoleListWidget extends ObjectSelectionList<RoleListWidget.Entry> {
    private final RoleSelectionScreen screen;

    public RoleListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight, RoleSelectionScreen screen) {
        super(minecraft, width, height, y, itemHeight);
        this.screen = screen;
    }

    public void refreshList(@NotNull Collection<DeferredHolder<Role, ? extends Role>> roles) {
        clearEntries();
        for (DeferredHolder<Role, ? extends Role> role : roles) {
            addEntry(new RoleListWidget.Entry(role));
        }
    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        private final Role role;
        private final Holder<Role> roleHolder;
        public int y = 0;

        public Entry(@NotNull Holder<Role> role) {
            this.role = role.value();
            this.roleHolder = role;
        }

        public Role getRole() {
            return role;
        }

        public Holder<Role> getRoleHolder() {
            return roleHolder;
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if (event.button() == 0) {
                screen.setSelectedRole(this);
                return true;
            }

            return false;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            int color = isMouseOver(mouseX, mouseY) ? brightenColor(role.color) : role.color;
            graphics.fill(getContentX(), getContentY(), getContentRight(), getContentBottom(), color);
            extractText(graphics, getContentX() + 5, getContentY() + 4, getContentWidth() - 40);

            int playerX = getContentRight() - 24;
            int playerY = getContentY() + 8;
            float playerScale = 1.25f;
            graphics.fill(getContentRight() - 35, getContentY(), getContentRight(), getContentBottom(), 0xFF000000);
            graphics.pose().pushMatrix();
            graphics.pose().scale(playerScale, playerScale);
            extractPlayer(graphics, (int) (playerX / playerScale), (int) (playerY / playerScale));
            graphics.pose().popMatrix();
        }

        private void extractText(GuiGraphicsExtractor graphics, int x, int y, int maxWidth) {
            String quote = role.quote;

            if (minecraft.font.width(quote) > maxWidth) {
                quote = minecraft.font.plainSubstrByWidth(quote, maxWidth - minecraft.font.width("...")) + "...";
            }

            graphics.text(minecraft.font, role.name, x, y, 0xFFFFFFFF);
            graphics.text(minecraft.font, quote, x, y + 16, 0xFFFFFFFF, false);
        }

        private void extractPlayer(@NotNull GuiGraphicsExtractor graphics, int startX, int startY) {
            Identifier playerTexture = DefaultPlayerSkin.get(minecraft.player.getUUID()).body().texturePath();

            Character character = screen.getCharacter();
            if (character != null && character.getPlayerInfo().isPresent()) {
                playerTexture = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getId());
            }

            Identifier texture = role.skinLocation;
            if (texture == null) return;

            int textureSize = 64;
            int lowerBodyStartY = startY + 8;

            int headSize = 8;

            graphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX, startY, 8, 8, headSize, headSize, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX, startY, 40, 8, headSize, headSize, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX, startY, 8, 8, headSize, headSize, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX, startY, 40, 8, headSize, headSize, textureSize, textureSize);

            int bodyWidth = 8;
            int bodyHeight = 12;

            graphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX, lowerBodyStartY, 20, 20, bodyWidth, bodyHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX, lowerBodyStartY, 20, 36, bodyWidth, bodyHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX, lowerBodyStartY, 20, 20, bodyWidth, bodyHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX, lowerBodyStartY, 20, 36, bodyWidth, bodyHeight, textureSize, textureSize);

            int armWidth = 4;
            int armHeight = 12;

            graphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX - 4, lowerBodyStartY, 44, 20, armWidth, armHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX - 4, lowerBodyStartY, 44, 36, armWidth, armHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX + 8, lowerBodyStartY, 36, 52, armWidth, armHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX + 8, lowerBodyStartY, 52, 52, armWidth, armHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX - 4, lowerBodyStartY, 44, 20, armWidth, armHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX - 4, lowerBodyStartY, 44, 36, armWidth, armHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX + 8, lowerBodyStartY, 36, 52, armWidth, armHeight, textureSize, textureSize);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX + 8, lowerBodyStartY, 52, 52, armWidth, armHeight, textureSize, textureSize);
        }

        private int brightenColor(int color) {
            int a = (color >> 24) & 0xFF;
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            float factor = 1.3f;
            r = Math.min(255, (int)(r * factor));
            g = Math.min(255, (int)(g * factor));
            b = Math.min(255, (int)(b * factor));

            return (a << 24) | (r << 16) | (g << 8) | b;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(role.name);
        }
    }
}
