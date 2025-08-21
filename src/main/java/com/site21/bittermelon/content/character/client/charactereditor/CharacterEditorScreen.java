package com.site21.bittermelon.content.character.client.charactereditor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.character.client.charactereditor.roleselection.RoleSelectionScreen;
import com.site21.bittermelon.content.character.networking.SwitchCharacter;
import com.site21.bittermelon.content.character.networking.UpdateCharacter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventory;

@OnlyIn(Dist.CLIENT)
public class CharacterEditorScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_background");
    private static final ResourceLocation BUTTON = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/button");
    private static final ResourceLocation BUTTON_HIGHLIGHTED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/button_highlighted");
    private static final int MARGIN = 15;
    private static final int TOP_MARGIN = 20;

    private Character character;
    private final Screen previousScreen;

    private int x;
    private int y;
    private int screenWidth;
    private int screenHeight;

    private EditBox nameField;
    private EditBox emoteColorField;
    private EditBox urlField;
    private MultiLineEditBox descriptionField;

    private float rotationX = 0;

    public CharacterEditorScreen(Character character, Screen previousScreen) {
        super(Component.literal("Character Editor"));
        this.character = character;
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        screenWidth = 384;
        screenHeight = 384;
        x = width / 2 - screenWidth / 2;
        y = height / 2 - screenHeight / 4;

        int labelLength = font.width("Emote Color");
        int startX = x + MARGIN * 2 + labelLength + 120;
        int startY = y + MARGIN;
        int fieldWidth = (int) (screenWidth / 2.5);
        int fieldHeight = font.lineHeight * 2;
        int fieldSpacing = fieldHeight + 5;

        nameField = new EditBox(font, startX, startY, fieldWidth, fieldHeight, Component.literal("Name"));
        startY += fieldSpacing;
        emoteColorField = new EditBox(font, startX, startY, fieldWidth, fieldHeight, Component.literal("Emote Color"));
        emoteColorField.setMaxLength(7);
        emoteColorField.setFilter(this::isValidHexInput);

        startY += fieldSpacing;
        urlField = new EditBox(font, startX, startY, fieldWidth, fieldHeight, Component.literal("Skin URL"));
        startY += fieldSpacing;
        descriptionField = new MultiLineEditBox(font, startX, startY, fieldWidth, (int) (fieldHeight * 4.5), Component.literal(""), Component.literal("Description"));

        if (character != null) {
            nameField.setValue(character.getName());
            emoteColorField.setValue(String.format("#%06X", character.getEmoteColor()));
            urlField.setValue("");
            descriptionField.setValue(character.getDescription());
        }

        addRenderableWidget(nameField);
        addRenderableWidget(emoteColorField);
        addRenderableWidget(urlField);
        addRenderableWidget(descriptionField);

        int buttonWidth = 60;
        int buttonHeight = 20;
        int buttonY = y + screenHeight / 2 - MARGIN;

        Button confirmButton = Button.builder(Component.literal("Confirm"), this::onConfirm)
                .bounds((x + screenWidth) - (buttonWidth * 2 + 28), buttonY, buttonWidth, buttonHeight)
                .build();

        Button cancelButton = Button.builder(Component.literal("Cancel"), this::onCancel)
                .bounds((x + screenWidth) - (buttonWidth + MARGIN + 8), buttonY, buttonWidth, buttonHeight)
                .build();

        addRenderableWidget(confirmButton);
        addRenderableWidget(cancelButton);
    }

    private boolean isValidHexInput(@NotNull String input) {
        if (input.isEmpty()) return true;

        if (!input.startsWith("#")) return false;

        String hexPart = input.substring(1);
        return hexPart.matches("[0-9A-Fa-f]*");
    }

    private void onConfirm(Button button) {
        String name = nameField.getValue();
        String description = descriptionField.getValue();
        int emoteColor;

        try {
            emoteColor = Integer.decode(emoteColorField.getValue());
        } catch (NumberFormatException e) {
            return;
        }

        if (name.isEmpty()) return;

        boolean goToRoleSelection = false;

        if (character == null) {
            character = new Character(minecraft.player.getUUID(), name, description, emoteColor);
            CharacterManager.get(minecraft.level).addCharacter(character);
            goToRoleSelection = true;
        } else {
            character.setName(name);
            character.setEmoteColor(emoteColor);
            character.setDescription(description);
        }

        PacketDistributor.sendToServer(new UpdateCharacter(character));
        assert minecraft != null;
        if (goToRoleSelection) {
            minecraft.setScreen(new RoleSelectionScreen(previousScreen, character));
        } else {
            minecraft.setScreen(previousScreen);
        }
    }

    private void onCancel(Button button) {
        assert minecraft != null;
        minecraft.setScreen(previousScreen);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderPlayer(guiGraphics);

        int startX = x + MARGIN + 120;
        int startY = y + MARGIN + 5;
        int textColor = 0xFFFFFF;
        int spacing = font.lineHeight * 2 + 5;

        guiGraphics.drawString(font, "Name:", startX, startY, textColor, false);
        startY += spacing;
        guiGraphics.drawString(font, "Emote Color:", startX, startY, textColor, false);
        startY += spacing;
        guiGraphics.drawString(font, "Skin URL:", startX, startY, textColor, false);
        startY += spacing;
        guiGraphics.drawString(font, "Description:", startX, startY, textColor, false);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        RenderSystem.enableBlend();
        guiGraphics.blitSprite(BACKGROUND, x, y, screenWidth, screenHeight);
        RenderSystem.disableBlend();
    }

    private void renderPlayer(@NotNull GuiGraphics guiGraphics) {
        int playerX = x + MARGIN + 5;
        int playerY = y + MARGIN;
        int playerWidth = 90;
        int playerHeight = 160;

        assert Minecraft.getInstance().player != null;
        renderEntityInInventoryFollowsAngle(
                guiGraphics,
                playerX,
                playerY,
                playerX + playerWidth,
                playerY + playerHeight,
                80,
                0f, rotationX, 0f,
                Minecraft.getInstance().player
        );
    }

    public static void renderEntityInInventoryFollowsAngle(@NotNull GuiGraphics graphics, int leftX, int topY, int rightX, int bottomY, int scale, float yOffset, float horizontalRotation, float verticalRotation, @NotNull LivingEntity entity) {
        float centerX = (float) (leftX + rightX) / 2.0F;
        float centerY = (float) (topY + bottomY) / 2.0F;

        graphics.enableScissor(leftX, topY, rightX, bottomY);

        Quaternionf baseRotation = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf verticalTiltRotation = (new Quaternionf()).rotateX(verticalRotation * 20.0F * ((float) Math.PI / 180F));
        baseRotation.mul(verticalTiltRotation);

        float originalBodyRotation = entity.yBodyRot;
        float originalYaw = entity.getYRot();
        float originalPitch = entity.getXRot();
        float originalHeadRotationOld = entity.yHeadRotO;
        float originalHeadRotation = entity.yHeadRot;

        float newBodyRotation = 180.0F + horizontalRotation * 20.0F;
        float newYawRotation = 180.0F + horizontalRotation * 20.0F;
        float newPitchRotation = -verticalRotation * 20.0F;

        entity.yBodyRot = newBodyRotation;
        entity.setYRot(newYawRotation);
        entity.setXRot(newPitchRotation);
        entity.yHeadRot = newBodyRotation;
        entity.yHeadRotO = newBodyRotation;

        float entityScale = entity.getScale();
        Vector3f translation = new Vector3f(0.0F, entity.getBbHeight() / 2.0F + yOffset * entityScale, 0.0F);
        float adjustedScale = (float) scale / entityScale;

        renderEntityInInventory(graphics, centerX, centerY, adjustedScale, translation, baseRotation, verticalTiltRotation, entity);

        entity.yBodyRot = originalBodyRotation;
        entity.setYRot(originalYaw);
        entity.setXRot(originalPitch);
        entity.yHeadRotO = originalHeadRotationOld;
        entity.yHeadRot = originalHeadRotation;

        graphics.disableScissor();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        int playerX = x + MARGIN + 5;
        int playerY = y + MARGIN;
        int playerWidth = 90;
        int playerHeight = 160;

        if (mouseX >= playerX &&
                mouseY >= playerY &&
                mouseX <= playerX + playerWidth &&
                mouseY <= playerY + playerHeight) {

            rotationX -= (float) dragX * 0.5f;

            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
