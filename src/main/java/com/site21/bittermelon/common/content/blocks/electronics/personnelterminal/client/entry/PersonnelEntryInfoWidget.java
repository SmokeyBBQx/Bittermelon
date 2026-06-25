package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.entry;

import com.mojang.authlib.GameProfile;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.BitterButton;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.PersonnelTerminalScreen;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.networking.RemovePersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.networking.UpdatePersonnelEntry;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PersonnelEntryInfoWidget extends AbstractWidget {
    private static final Identifier BACKGROUND = Bittermelon.identifier("retro/generic_background");
    private static final Identifier LOGO =  Bittermelon.identifier("retro/scp_logo");
    private static final Identifier AVATAR_BACKGROUND = Identifier.withDefaultNamespace("textures/block/light_gray_concrete_powder.png");

    private final PersonnelEntry entry;
    private final PersonnelTerminalScreen screen;

    private boolean editMode = false;
    private boolean canEdit = false;

    private EditBox nameField;
    private EditBox occupationField;
    private EditBox departmentField;
    private EditBox notesField;

    private BitterButton editButton;
    private BitterButton saveButton;
    private BitterButton cancelButton;
    private BitterButton privilegesButton;
    private BitterButton deleteButton;

    private final List<EditBox> fields;
    private final List<Button> buttons;
    private final Font font;

    public PersonnelEntryInfoWidget(int x, int y, int width, int height, @NotNull PersonnelEntry entry, PersonnelTerminalScreen screen) {
        super(x, y, width, height, Component.literal(entry.getName()));
        this.entry = entry;
        this.screen = screen;
        fields = new ArrayList<>();
        buttons = new ArrayList<>();
        font = Minecraft.getInstance().font;
        init();
    }

    private void init() {
        int fieldX = getX() + 70;
        int fieldY = getY() + 95;
        int fieldWidth = getWidth() - 90;
        int fieldHeight = 20;

        nameField = new EditBox(Minecraft.getInstance().font, fieldX, fieldY, fieldWidth, fieldHeight, Component.literal("Name"));
        occupationField = new EditBox(Minecraft.getInstance().font, fieldX, fieldY + fieldHeight, fieldWidth, fieldHeight, Component.literal("Occupation"));
        departmentField = new EditBox(Minecraft.getInstance().font, fieldX, fieldY + fieldHeight * 2, fieldWidth, fieldHeight, Component.literal("Department"));
        notesField = new EditBox(Minecraft.getInstance().font, fieldX, fieldY + fieldHeight * 3, fieldWidth, fieldHeight * 2, Component.literal("Notes"));
        fields.add(nameField);
        fields.add(occupationField);
        fields.add(departmentField);
        fields.add(notesField);

        editButton = BitterButton.builder(Component.literal(""), this::toggleEditMode, new WidgetSprites(
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/write_button"),
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/locked_button"),
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/write_button_highlighted"),
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/locked_button_highlighted")
                ))
                .bounds(getX() + getWidth() - 30, getY() + getHeight() - 25, 20, 20)
                .tooltip(Tooltip.create(Component.literal("Edit")))
                .build();
        buttons.add(editButton);

        saveButton = BitterButton.builder(Component.literal("Save"), this::saveChanges, PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(getX() + getWidth() - 110, getY() + getHeight() - 25, 45, 20)
                .build();
        buttons.add(saveButton);

        cancelButton = BitterButton.builder(Component.literal("Cancel"), this::cancelEdit, PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(getX() + getWidth() - 60, getY() + getHeight() - 25, 50, 20)
                .build();
        buttons.add(cancelButton);

        privilegesButton = BitterButton.builder(Component.literal(""), this::viewPrivileges, new WidgetSprites(
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys_button"),
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys_button_disabled"),
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys_button_highlighted")
                ))
                .bounds(getX() + 10, getY() + getHeight() - 25, 20, 20)
                .tooltip(Tooltip.create(Component.literal("View Privileges")))
                .build();
        buttons.add(privilegesButton);

        deleteButton = BitterButton.builder(Component.literal(""), this::deleteEntry, new WidgetSprites(
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button"),
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button_disabled"),
                        Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button_highlighted")
                ))
                .bounds(getX() + getWidth() - 135, getY() + getHeight() - 25, 20, 20)
                .tooltip(Tooltip.create(Component.literal("Delete Entry")))
                .build();
        buttons.add(deleteButton);

        updateFieldValues();
        updateFieldStates();
    }

    private void updateFieldValues() {
        nameField.setValue(entry.getName());
        occupationField.setValue(entry.getOccupation());
        departmentField.setValue(entry.getDepartment());
        notesField.setValue(entry.getNotes());
    }

    private void updateFieldStates() {
        boolean editable = editMode && canEdit;
        fields.forEach(field -> field.setEditable(editable));
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
        updateFieldStates();
    }

    private void toggleEditMode(Button button) {
        if (!canEdit) return;
        editMode = !editMode;
        updateFieldStates();
    }

    private void saveChanges(Button button) {
        entry.setName(nameField.getValue());
        entry.setOccupation(occupationField.getValue());
        entry.setDepartment(departmentField.getValue());
        entry.setNotes(notesField.getValue());

        editMode = false;
        updateFieldStates();

        ClientPacketDistributor.sendToServer(new UpdatePersonnelEntry(entry));
    }

    private void cancelEdit(Button button) {
        updateFieldValues();
        editMode = false;
        updateFieldStates();
    }

    private void viewPrivileges(Button button) {
        EntryPrivilegeEditorWidget privilegeWidget = new EntryPrivilegeEditorWidget(getX(), getY(), getWidth(),
                getHeight(), entry);
        screen.setActiveWidget(privilegeWidget);
    }

    private void deleteEntry(Button button) {
        ClientPacketDistributor.sendToServer(new RemovePersonnelEntry(entry.getId()));
        screen.setActiveWidget(null);
        screen.refreshContent();
    }

    public PersonnelEntry getEntry() {
        return entry;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, getX(), getY(), getWidth(), getHeight());
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, LOGO,x + width - 100, y, 100, 100);

        graphics.pose().pushMatrix();
        int pictureStartX = getX() + 30;
        int pictureStartY = getY() + 10;
        float pictureScale = 4;

        graphics.pose().scale(pictureScale, pictureScale);

        int playerWidth = 13;
        int playerHeight = 19;
        int bgX = (int) (pictureStartX / pictureScale) - 5;
        int bgY = (int) (pictureStartY / pictureScale) - 1;
        int bgWidth = playerWidth + 5;
        int bgHeight = playerHeight + 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, AVATAR_BACKGROUND, bgX, bgY, 0, 0, bgWidth, bgHeight, 16, 16);

        renderPlayer(graphics, (int) (pictureStartX / pictureScale), (int) (pictureStartY / pictureScale));
        graphics.pose().popMatrix();

        int startX = getX() + 10;
        int startY = getY() + pictureStartY + 40;

        graphics.pose().pushMatrix();

        int titleStartX = getX() + 90;
        int titleStartY = getY() + 10;
        float scale = 1.5f;

        graphics.pose().scale(scale, scale);

        graphics.text(font, entry.getName(), (int) (titleStartX / scale), (int) (titleStartY / scale), 0xFFFFFFFF);
        graphics.pose().popMatrix();

        String[] labels = {"ID: ", "Name:", "Position:", "Department:", "Notes:"};
        int maxLabelWidth = 0;
        for (String label : labels) {
            maxLabelWidth = Math.max(maxLabelWidth, font.width(label));
        }
        int fieldX = startX + maxLabelWidth + 5;
        int color = 0xFF545454;

        graphics.text(font, "ID: " + entry.getId(), titleStartX, titleStartY + 20, color, false);
        graphics.text(font, "Name:", startX, startY, color, false);
        graphics.text(font, "Position:", startX, startY + 20, color, false);
        graphics.text(font, "Department:", startX, startY + 40, color, false);
        graphics.text(font, "Notes:", startX, startY + 60, color, false);

        privilegesButton.extractRenderState(graphics, mouseX, mouseY, partialTick);

        if (editMode && canEdit) {
            fields.forEach(field -> field.extractRenderState(graphics, mouseX, mouseY, partialTick));

            deleteButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
            saveButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
            cancelButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        } else {
            int textColor = 0;

            graphics.text(font, entry.getName(), fieldX, startY, textColor, false);
            graphics.text(font, entry.getOccupation(), fieldX, startY + 20, textColor, false);
            graphics.text(font, entry.getDepartment(), fieldX, startY + 40, textColor, false);
            graphics.textWithWordWrap(font, FormattedText.of(entry.getNotes()), fieldX, startY + 60, 200, textColor);

            if (canEdit) {
                editButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
            }
        }
    }

    private void renderPlayer(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int startX, int startY) {
        Identifier texture = DefaultPlayerSkin.get(entry.getPlayerUUID()).body().texturePath();

        Character character = CharacterManager.get(Minecraft.getInstance().level).getCharacter(entry.getCharacterUUID());
        if (character != null && character.getPlayerInfo().isPresent()) {
             texture = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getId());
        }

        int textureSize = 64;
        int lowerBodyStartY = startY + 8;

        int headSize = 8;

        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, startX, startY, 8, 8, headSize, headSize, textureSize, textureSize);
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, startX, startY, 40, 8, headSize, headSize, textureSize, textureSize);

        int bodyWidth = 8;
        int bodyHeight = 12;

        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, startX, lowerBodyStartY, 20, 20, bodyWidth, bodyHeight, textureSize, textureSize);
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, startX, lowerBodyStartY, 20, 36, bodyWidth, bodyHeight, textureSize, textureSize);

        int armWidth = 4;
        int armHeight = 12;

        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, startX - 4, lowerBodyStartY, 44, 20, armWidth, armHeight, textureSize, textureSize);
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, startX - 4, lowerBodyStartY, 44, 36, armWidth, armHeight, textureSize, textureSize);
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, startX + 8, lowerBodyStartY, 36, 52, armWidth, armHeight, textureSize, textureSize);
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, startX + 8, lowerBodyStartY, 52, 52, armWidth, armHeight, textureSize, textureSize);
    }

    private void renderPlayerOld(GuiGraphicsExtractor GuiGraphicsExtractor) {
        if (Minecraft.getInstance().level != null) {
            GameProfile profile = new GameProfile(entry.getPlayerUUID(), entry.getName());
            AbstractClientPlayer fakePlayer = new AbstractClientPlayer(Minecraft.getInstance().level, profile) {
                @Override
                public boolean isSpectator() {
                    return false;
                }

                @Override
                public boolean isCreative() {
                    return false;
                }
            };

            InventoryScreen.renderEntityInInventoryFollowsAngle(
                    GuiGraphicsExtractor,
                    getX() + getWidth() - 60,
                    getY() + 20,
                    getX() + getWidth() - 10,
                    getY() + 90,
                    30,
                    0.0f,
                    0.0f,
                    0.0f,
                    fakePlayer
            );
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        for (Button buttonWidget : buttons) {
            if (buttonWidget.mouseClicked(event, doubleClick)) return true;
        }

        for (EditBox field : fields) {
            if (field.mouseClicked(event, doubleClick)) {
                for (EditBox field1 : fields) {
                    field1.setFocused(false);
                }
                field.setFocused(true);
                return true;
            }
        }

        for (EditBox field : fields) {
            field.setFocused(false);
        }

        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        for (EditBox field : fields) {
            if (field.keyPressed(event)) return true;
        }

        return false;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        for (EditBox field : fields) {
            if (field.charTyped(event)) return true;
        }

        return false;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
    }

    @Override
    public void playDownSound(@NotNull SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(BitterSounds.MOUSE_CLICK, 1f));
    }
}
