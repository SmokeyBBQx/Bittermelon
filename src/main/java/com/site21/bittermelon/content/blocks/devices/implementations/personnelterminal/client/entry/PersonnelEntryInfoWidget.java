package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.entry;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.BitterButton;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.PersonnelTerminalScreen;
import com.site21.bittermelon.content.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.content.personnel.registry.networking.RemovePersonnelEntry;
import com.site21.bittermelon.content.personnel.registry.networking.UpdatePersonnelEntry;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PersonnelEntryInfoWidget extends AbstractWidget {
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

    public PersonnelEntryInfoWidget(int x, int y, int width, int height, @NotNull PersonnelEntry entry, PersonnelTerminalScreen screen) {
        super(x, y, width, height, Component.literal(entry.getName()));
        this.entry = entry;
        this.screen = screen;
        fields = new ArrayList<>();
        buttons = new ArrayList<>();
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
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/write_button"),
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/locked_button"),
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/write_button_highlighted"),
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/locked_button_highlighted")
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
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys_button"),
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys_button_disabled"),
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys_button_highlighted")
                ))
                .bounds(getX() + 10, getY() + getHeight() - 25, 20, 20)
                .tooltip(Tooltip.create(Component.literal("View Privileges")))
                .build();
        buttons.add(privilegesButton);

        deleteButton = BitterButton.builder(Component.literal(""), this::deleteEntry, new WidgetSprites(
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button"),
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button_disabled"),
                        ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button_highlighted")
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

        PacketDistributor.sendToServer(new UpdatePersonnelEntry(entry));
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
        PacketDistributor.sendToServer(new RemovePersonnelEntry(entry.getId()));
        screen.setActiveWidget(null);
        screen.refreshContent();
    }

    public PersonnelEntry getEntry() {
        return entry;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/generic_background"),
                getX(), getY(), getWidth(), getHeight());

        RenderSystem.enableBlend();
        guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/scp_logo"),
                    x + width - 100, y, 100, 100);
        RenderSystem.disableBlend();

        Font font = Minecraft.getInstance().font;

        guiGraphics.pose().pushPose();
        int pictureStartX = getX() + 30;
        int pictureStartY = getY() + 10;
        float pictureScale = 4;

        guiGraphics.pose().scale(pictureScale, pictureScale, 0);

        int playerWidth = 13;
        int playerHeight = 19;
        int bgX = (int) (pictureStartX / pictureScale) - 5;
        int bgY = (int) (pictureStartY / pictureScale) - 1;
        int bgWidth = playerWidth + 5;
        int bgHeight = playerHeight + 2;

        ResourceLocation texture = ResourceLocation.withDefaultNamespace("textures/block/light_gray_concrete_powder.png");
        guiGraphics.blit(texture, bgX, bgY, 0, 0, bgWidth, bgHeight, 16, 16);

        renderPlayer(guiGraphics, (int) (pictureStartX / pictureScale), (int) (pictureStartY / pictureScale));
        guiGraphics.pose().popPose();

        int startX = getX() + 10;
        int startY = getY() + pictureStartY + 40;

        guiGraphics.pose().pushPose();

        int titleStartX = getX() + 90;
        int titleStartY = getY() + 10;
        float scale = 1.5f;

        guiGraphics.pose().scale(scale, scale, 0);

        guiGraphics.drawString(font, entry.getName(), (int) (titleStartX / scale), (int) (titleStartY / scale), 0xFFFFFF);
        guiGraphics.pose().popPose();

        String[] labels = {"ID: ", "Name:", "Position:", "Department:", "Notes:"};
        int maxLabelWidth = 0;
        for (String label : labels) {
            maxLabelWidth = Math.max(maxLabelWidth, font.width(label));
        }
        int fieldX = startX + maxLabelWidth + 5;
        int color = 0x545454;

        guiGraphics.drawString(font, "ID: " + entry.getId(), titleStartX, titleStartY + 20, color, false);
        guiGraphics.drawString(font, "Name:", startX, startY, color, false);
        guiGraphics.drawString(font, "Position:", startX, startY + 20, color, false);
        guiGraphics.drawString(font, "Department:", startX, startY + 40, color, false);
        guiGraphics.drawString(font, "Notes:", startX, startY + 60, color, false);

        privilegesButton.render(guiGraphics, mouseX, mouseY, partialTick);

        if (editMode && canEdit) {
            fields.forEach(field -> field.render(guiGraphics, mouseX, mouseY, partialTick));

            deleteButton.render(guiGraphics, mouseX, mouseY, partialTick);
            saveButton.render(guiGraphics, mouseX, mouseY, partialTick);
            cancelButton.render(guiGraphics, mouseX, mouseY, partialTick);
        } else {
            int textColor = 0;

            guiGraphics.drawString(font, entry.getName(), fieldX, startY, textColor, false);
            guiGraphics.drawString(font, entry.getOccupation(), fieldX, startY + 20, textColor, false);
            guiGraphics.drawString(font, entry.getDepartment(), fieldX, startY + 40, textColor, false);
            guiGraphics.drawWordWrap(font, FormattedText.of(entry.getNotes()), fieldX, startY + 60, 200, textColor);

            if (canEdit) {
                editButton.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }
    }

    private void renderPlayer(@NotNull GuiGraphics guiGraphics, int startX, int startY) {
        PlayerSkin skinLocation = DefaultPlayerSkin.get(entry.getPlayerUUID());
        ;
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            PlayerInfo playerInfo = connection.getPlayerInfo(entry.getPlayerUUID());
            if (playerInfo != null) {
                skinLocation = playerInfo.getSkin();
            }
        }
        ResourceLocation texture = skinLocation.texture();
        int textureSize = 64;
        int lowerBodyStartY = startY + 8;

        int headSize = 8;

        guiGraphics.blit(texture, startX, startY, 8, 8, headSize, headSize, textureSize, textureSize);
        guiGraphics.blit(texture, startX, startY, 40, 8, headSize, headSize, textureSize, textureSize);

        int bodyWidth = 8;
        int bodyHeight = 12;

        guiGraphics.blit(texture, startX, lowerBodyStartY, 20, 20, bodyWidth, bodyHeight, textureSize, textureSize);
        guiGraphics.blit(texture, startX, lowerBodyStartY, 20, 36, bodyWidth, bodyHeight, textureSize, textureSize);

        int armWidth = 4;
        int armHeight = 12;

        guiGraphics.blit(texture, startX - 4, lowerBodyStartY, 44, 20, armWidth, armHeight, textureSize, textureSize);
        guiGraphics.blit(texture, startX - 4, lowerBodyStartY, 44, 36, armWidth, armHeight, textureSize, textureSize);
        guiGraphics.blit(texture, startX + 8, lowerBodyStartY, 36, 52, armWidth, armHeight, textureSize, textureSize);
        guiGraphics.blit(texture, startX + 8, lowerBodyStartY, 52, 52, armWidth, armHeight, textureSize, textureSize);
    }

    private void renderPlayerOld(GuiGraphics guiGraphics) {
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
                    guiGraphics,
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Button buttonWidget : buttons) {
            if (buttonWidget.mouseClicked(mouseX, mouseY, button)) return true;
        }

        for (EditBox field : fields) {
            if (field.mouseClicked(mouseX, mouseY, button)) {
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (EditBox field : fields) {
            if (field.keyPressed(keyCode, scanCode, modifiers)) return true;
        }

        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for (EditBox field : fields) {
            if (field.charTyped(codePoint, modifiers)) return true;
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
