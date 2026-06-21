package com.site21.bittermelon.common.systems.character.client.characterselection;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.BitterButton;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.character.client.charactereditor.CharacterEditorScreen;
import com.site21.bittermelon.common.systems.character.skin.SkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.systems.character.skin.SkinUtil.getAbstractClientPlayer;


public class CharacterWidget extends AbstractWidget {
    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box");
    private static final Identifier BACKGROUND_HOVERED = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box_hovered");
    private static final Identifier BACKGROUND_SELECTED = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box_selected");
    private static final Identifier BACKGROUND_SELECTED_HOVERED = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_box_selected_hovered");
    private static final Identifier ADD_ICON = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/add_icon");
    private static final Identifier ADD_ICON_HOVERED = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/add_icon_hovered");

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
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/edit_button"),
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/edit_button_highlighted")
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
                    info -> SkinManager.loadSkin(info.getSkinURL(), String.valueOf(character.getId())));
        }
    }

    private void onEdit(Button button) {
        Minecraft.getInstance().setScreen(new CharacterEditorScreen(character, screen));
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        if (isHovered && !editButton.isHovered()) {
            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, selected ? BACKGROUND_SELECTED_HOVERED : BACKGROUND_HOVERED, x, y, width, height);
        } else {
            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, selected ? BACKGROUND_SELECTED : BACKGROUND, x, y, width, height);
        }

        if (character == null) {
            renderCreateCharacter(GuiGraphicsExtractor);
        } else {
            renderCharacter(GuiGraphicsExtractor);
        }

        editButton.render(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
    }

    private void renderCharacter(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor) {
        Font font = Minecraft.getInstance().font;
        String displayName = character.getName();
        int maxWidth = width - 10;

        if (font.width(displayName) > maxWidth) {
            displayName = font.plainSubstrByWidth(displayName, maxWidth - font.width("...")) + "...";
        }

        GuiGraphicsExtractor.drawCenteredString(font, displayName, x + width / 2, y + 5, 0xFFFFFF);

        AbstractClientPlayer fakePlayer = getAbstractClientPlayer(character);

        InventoryScreen.renderEntityInInventoryFollowsAngle(
                GuiGraphicsExtractor,
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

    private void renderCreateCharacter(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor) {
        if (isHovered) {
            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, ADD_ICON_HOVERED, x, y, width, height);
        } else {
            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, ADD_ICON, x, y, width, height);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHovered) {
            if (editButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }

            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));

            if (character != null) {
                CharacterManager characterManager = CharacterManager.get(Minecraft.getInstance().level);
                Character activeCharacter = characterManager.getActiveCharacter(Minecraft.getInstance().player);

                if (activeCharacter == null || !activeCharacter.getId().equals(character.getId())) {
                    screen.switchCharacter(character);
                }
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
