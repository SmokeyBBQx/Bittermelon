package com.site21.bittermelon.content.character.client.characterselection;

import com.mojang.authlib.GameProfile;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CharacterWidget extends AbstractWidget {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box");
    private static final ResourceLocation BACKGROUND_HOVERED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box_hovered");
    private static final ResourceLocation ADD_ICON = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/add_icon");
    private static final ResourceLocation ADD_ICON_HOVERED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/add_icon_hovered");

    private final Character character;
    private final CharacterSelectionScreen screen;

    public CharacterWidget(int x, int y, int width, int height, Character character, CharacterSelectionScreen screen) {
        super(x, y, width, height, Component.literal(character != null ? character.getName() : "Create Character"));
        this.character = character;
        this.screen = screen;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isHovered) {
            guiGraphics.blitSprite(BACKGROUND_HOVERED, x, y, width, height);
        } else {
            guiGraphics.blitSprite(BACKGROUND, x, y, width, height);
        }

        if (character == null) {
            renderCreateCharacter(guiGraphics);
        } else {
            renderCharacter(guiGraphics);
        }
    }

    private void renderCharacter(@NotNull GuiGraphics guiGraphics) {
        Font font = Minecraft.getInstance().font;
        String displayName = character.getName();
        int maxWidth = width - 10;

        if (font.width(displayName) > maxWidth) {
            displayName = font.plainSubstrByWidth(displayName, maxWidth - font.width("...")) + "...";
        }

        guiGraphics.drawCenteredString(font, displayName, x + width / 2, y + 5, 0xFFFFFF);

//        GameProfile profile = new GameProfile(Minecraft.getInstance().player.getUUID(), character.getName());
//        AbstractClientPlayer fakePlayer = new AbstractClientPlayer(Minecraft.getInstance().level, profile) {
//            @Override
//            public boolean isSpectator() {
//                return false;
//            }
//
//            @Override
//            public boolean isCreative() {
//                return false;
//            }
//        };

        InventoryScreen.renderEntityInInventoryFollowsAngle(
                guiGraphics,
                getX(),
                getY() + 20,
                getX() + getWidth(),
                getY() + 90,
                35,
                0.0f,
                0.0f,
                0.0f,
                Minecraft.getInstance().player
        );
    }

    private void renderCreateCharacter(@NotNull GuiGraphics guiGraphics) {
        if (isHovered) {
            guiGraphics.blitSprite(ADD_ICON_HOVERED, x, y, width, height);
        } else {
            guiGraphics.blitSprite(ADD_ICON, x, y, width, height);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHovered) {
            if (character != null) {
                screen.switchCharacter(character);
            }

            return true;
        }

        return false;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE,
                Component.literal(character != null ? character.getName() : "Create Character"));
    }
}
