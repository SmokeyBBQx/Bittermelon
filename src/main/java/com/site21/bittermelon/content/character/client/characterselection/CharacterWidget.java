package com.site21.bittermelon.content.character.client.characterselection;

import com.mojang.authlib.GameProfile;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.BitterButton;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.PlayerInfo;
import com.site21.bittermelon.content.character.client.charactereditor.CharacterEditorScreen;
import com.site21.bittermelon.content.character.skin.SkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.content.character.skin.SkinUtil.getAbstractClientPlayer;

@OnlyIn(Dist.CLIENT)
public class CharacterWidget extends AbstractWidget {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box");
    private static final ResourceLocation BACKGROUND_HOVERED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box_hovered");
    private static final ResourceLocation BACKGROUND_SELECTED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box_selected");
    private static final ResourceLocation BACKGROUND_SELECTED_HOVERED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box_selected_hovered");
    private static final ResourceLocation ADD_ICON = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/add_icon");
    private static final ResourceLocation ADD_ICON_HOVERED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/add_icon_hovered");

    private final Character character;
    private final CharacterSelectionScreen screen;
    private final boolean selected;

    private BitterButton editButton;

    public CharacterWidget(int x, int y, int width, int height, Character character, CharacterSelectionScreen screen, boolean selected) {
        super(x, y, width, height, Component.literal(character != null ? character.getName() : "Create Character"));
        this.character = character;
        this.screen = screen;
        this.selected = selected;

        int buttonSize = 16;
        editButton = BitterButton.builder(Component.literal(""), this::onEdit, new WidgetSprites(
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/edit_button"),
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/edit_button_highlighted")
                ))
                .bounds(getRight() - buttonSize - 4, getBottom() - buttonSize - 4, buttonSize, buttonSize)
                .build();
        editButton.setTooltip(Tooltip.create(Component.literal("Edit Character")));
        editButton.setClickSound(SoundEvents.UI_BUTTON_CLICK);

        if (character == null) {
            editButton.visible = false;
        }

        if (character != null) {
            character.getPlayerInfo().ifPresent(
                    info -> SkinManager.loadSkin(info.getSkinURL(), String.valueOf(character.getUUID())));
        }
    }

    private void onEdit(Button button) {
        Minecraft.getInstance().setScreen(new CharacterEditorScreen(character, screen));
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isHovered && !editButton.isHovered()) {
            guiGraphics.blitSprite(selected ? BACKGROUND_SELECTED_HOVERED : BACKGROUND_HOVERED, x, y, width, height);
        } else {
            guiGraphics.blitSprite(selected ? BACKGROUND_SELECTED : BACKGROUND, x, y, width, height);
        }

        if (character == null) {
            renderCreateCharacter(guiGraphics);
        } else {
            renderCharacter(guiGraphics);
        }

        editButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderCharacter(@NotNull GuiGraphics guiGraphics) {
        Font font = Minecraft.getInstance().font;
        String displayName = character.getName();
        int maxWidth = width - 10;

        if (font.width(displayName) > maxWidth) {
            displayName = font.plainSubstrByWidth(displayName, maxWidth - font.width("...")) + "...";
        }

        guiGraphics.drawCenteredString(font, displayName, x + width / 2, y + 5, 0xFFFFFF);

        AbstractClientPlayer fakePlayer = getAbstractClientPlayer(character);

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
                fakePlayer == null ? Minecraft.getInstance().player : fakePlayer
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
            if (editButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }

            if (character != null) {
                screen.switchCharacter(character);
            } else {
                Minecraft.getInstance().setScreen(new CharacterEditorScreen(null, screen));
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
