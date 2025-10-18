package com.site21.bittermelon.common.systems.character.client.charactereditor.roleselection;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.roles.Role;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@OnlyIn(Dist.CLIENT)
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
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                screen.setSelectedRole(this);
                return true;
            }

            return false;
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean isMouseOver, float partialTick)  {
            y = top;

            int color = isMouseOver ? brightenColor(role.color) : role.color;
            guiGraphics.fill(left, top, left + entryWidth, top + entryHeight, color);
            renderText(guiGraphics, left + 5, top + 4, entryWidth - 40);

            int playerX = left + entryWidth - 24;
            int playerY = top + 8;
            float playerScale = 1.25f;
            guiGraphics.fill(left + entryWidth - 35, top, left + entryWidth, top + entryHeight, 0xFF000000);
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().scale(playerScale, playerScale);
            renderPlayer(guiGraphics, (int) (playerX / playerScale), (int) (playerY / playerScale));
            guiGraphics.pose().popMatrix();
        }

        private void renderText(GuiGraphics guiGraphics, int x, int y, int maxWidth) {
            String quote = role.quote;

            if (minecraft.font.width(quote) > maxWidth) {
                quote = minecraft.font.plainSubstrByWidth(quote, maxWidth - minecraft.font.width("...")) + "...";
            }

            guiGraphics.drawString(minecraft.font, role.name, x, y, 0xFFFFFFFF);
            guiGraphics.drawString(minecraft.font, quote, x, y + 16, 0xFFFFFFFF, false);
        }

        private void renderPlayer(@NotNull GuiGraphics guiGraphics, int startX, int startY) {
            ResourceLocation playerTexture = DefaultPlayerSkin.get(minecraft.player.getUUID()).texture();

            Character character = screen.getCharacter();
            if (character != null && character.getPlayerInfo().isPresent()) {
                playerTexture = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getUUID());
            }

            ResourceLocation texture = role.skinLocation;
            if (texture == null) return;

            int textureSize = 64;
            int lowerBodyStartY = startY + 8;

            int headSize = 8;

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX, startY, 8, 8, headSize, headSize, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX, startY, 40, 8, headSize, headSize, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX, startY, 8, 8, headSize, headSize, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX, startY, 40, 8, headSize, headSize, textureSize, textureSize);

            int bodyWidth = 8;
            int bodyHeight = 12;

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX, lowerBodyStartY, 20, 20, bodyWidth, bodyHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX, lowerBodyStartY, 20, 36, bodyWidth, bodyHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX, lowerBodyStartY, 20, 20, bodyWidth, bodyHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX, lowerBodyStartY, 20, 36, bodyWidth, bodyHeight, textureSize, textureSize);

            int armWidth = 4;
            int armHeight = 12;

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX - 4, lowerBodyStartY, 44, 20, armWidth, armHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX - 4, lowerBodyStartY, 44, 36, armWidth, armHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX + 8, lowerBodyStartY, 36, 52, armWidth, armHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, startX + 8, lowerBodyStartY, 52, 52, armWidth, armHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX - 4, lowerBodyStartY, 44, 20, armWidth, armHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX - 4, lowerBodyStartY, 44, 36, armWidth, armHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX + 8, lowerBodyStartY, 36, 52, armWidth, armHeight, textureSize, textureSize);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, startX + 8, lowerBodyStartY, 52, 52, armWidth, armHeight, textureSize, textureSize);
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
