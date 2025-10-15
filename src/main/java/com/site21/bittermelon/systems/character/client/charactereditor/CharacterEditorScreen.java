package com.site21.bittermelon.systems.character.client.charactereditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.character.Character;
import com.site21.bittermelon.systems.character.CharacterManager;
import com.site21.bittermelon.systems.character.PlayerInfo;
import com.site21.bittermelon.systems.character.client.charactereditor.roleselection.RoleSelectionScreen;
import com.site21.bittermelon.systems.character.client.characterselection.CharacterSelectionScreen;
import com.site21.bittermelon.systems.character.networking.UpdateCharacter;
import com.site21.bittermelon.systems.character.skin.SkinManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.site21.bittermelon.systems.character.skin.SkinUtil.getAbstractClientPlayer;
import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventory;

@OnlyIn(Dist.CLIENT)
public class CharacterEditorScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/character_background");
    private static final int MARGIN = 15;
    private static final int SCREEN_WIDTH = 384;
    private static final int SCREEN_HEIGHT = 384;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PLAYER_RENDER_WIDTH = 90;
    private static final int PLAYER_RENDER_HEIGHT = 160;
    private static final int RENDER_SCALE = 80;
    private static final float ROTATION_SENSITIVITY = 0.5f;
    private static final int TEXT_COLOR = 0xFFFFFF;

    private Character character;
    private final Screen previousScreen;

    private int screenX;
    private int screenY;

    private EditBox nameField;
    private EditBox emoteColorField;
    private EditBox urlField;
    private MultiLineEditBox descriptionField;
    private ModelButton modelButton;

    private float rotationX = 0;
    private boolean isWideModel;
    private ResourceLocation skin;

    public CharacterEditorScreen(Character character, Screen previousScreen) {
        super(Component.literal("Character Editor"));
        this.character = character;
        this.previousScreen = previousScreen;
        isWideModel = true;
    }

    @Override
    protected void init() {
        screenX = (width - SCREEN_WIDTH) / 2;
        screenY = (height - SCREEN_HEIGHT / 2) / 2;
        skin = DefaultPlayerSkin.get(minecraft.player.getUUID()).texture();

        initializeFields();
        addConfirmAndCancelButtons();
    }

    private void initializeFields() {
        int labelLength = font.width("Emote Color");
        int startX = screenX + MARGIN * 2 + labelLength + 120;
        int startY = screenY + MARGIN;
        int fieldWidth = (int) (SCREEN_WIDTH / 2.5);
        int fieldHeight = font.lineHeight * 2;
        int fieldSpacing = fieldHeight + 5;

        nameField = new EditBox(font, startX, startY, fieldWidth, fieldHeight,
                Component.literal("Name"));

        startY += fieldSpacing;
        emoteColorField = new EditBox(font, startX, startY, fieldWidth, fieldHeight,
                Component.literal("Emote Color"));
        emoteColorField.setMaxLength(7);
        emoteColorField.setFilter(this::isValidHexInput);

        startY += fieldSpacing;
        urlField = new EditBox(font, startX, startY, fieldWidth, fieldHeight,
                Component.literal("Skin URL"));
        urlField.setResponder(url -> skin = SkinManager.loadSkin(url, "temp" + url.hashCode()));

        startY += fieldSpacing;
        descriptionField = MultiLineEditBox.builder()
                .setX(startX)
                .setY(startY)
                .build(font, fieldWidth, (int) (fieldHeight * 4.5f), Component.literal("Description"));

        modelButton = new ModelButton(screenX + MARGIN + 43, screenY + SCREEN_HEIGHT / 2 - MARGIN,
                16, 16, button -> isWideModel = button.isWide());
        modelButton.setTooltip(Tooltip.create(Component.literal("Toggle Wide/Slim Model")));

        populateFieldsFromCharacter();

        addRenderableWidget(nameField);
        addRenderableWidget(emoteColorField);
        addRenderableWidget(urlField);
        addRenderableWidget(descriptionField);
        addRenderableWidget(modelButton);
    }

    private void populateFieldsFromCharacter() {
        if (character == null) return;

        nameField.setValue(character.getName());
        emoteColorField.setValue(String.format("#%06X", character.getEmoteColor()));
        descriptionField.setValue(character.getDescription());

        character.getPlayerInfo().ifPresent(info -> {
            skin = SkinManager.loadSkin(info.getSkinURL(), character.getUUID().toString());
            urlField.setValue(info.getSkinURL());
            boolean isWide = info.getModel().equals(PlayerInfo.SkinModel.WIDE);
            modelButton.setWide(isWide);
            isWideModel = isWide;
        });
    }

    private void addConfirmAndCancelButtons() {
        int buttonY = screenY + SCREEN_HEIGHT / 2 - MARGIN;
        int confirmButtonX = (screenX + SCREEN_WIDTH) - (BUTTON_WIDTH * 2 + 28);
        int cancelButtonX = (screenX + SCREEN_WIDTH) - (BUTTON_WIDTH + MARGIN + 8);

        Button confirmButton = Button.builder(Component.literal("Confirm"), this::onConfirm)
                .bounds(confirmButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();

        Button cancelButton = Button.builder(Component.literal("Cancel"), button -> minecraft.setScreen(previousScreen))
                .bounds(cancelButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
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
        String name = nameField.getValue().trim();
        if (name.isEmpty()) return;

        String skinURL = urlField.getValue().trim();
        String description = descriptionField.getValue();
        PlayerSkin.Model model = isWideModel ? PlayerSkin.Model.WIDE : PlayerSkin.Model.SLIM;

        int emoteColor;
        try {
            emoteColor = Integer.decode(emoteColorField.getValue());
        } catch (NumberFormatException e) {
            return;
        }

        boolean isNewCharacter = (character == null);
        updateOrCreateCharacter(isNewCharacter, name, description, emoteColor, skinURL, model);

        ClientPacketDistributor.sendToServer(new UpdateCharacter(character));
        navigateToNextScreen(isNewCharacter);
    }

    private void updateOrCreateCharacter(boolean isNewCharacter, String name, String description, int emoteColor,
                                         String skinURL, PlayerSkin.Model model) {
        if (isNewCharacter) {
            character = new Character(minecraft.player.getUUID(), name, description, emoteColor);
            CharacterManager.get(minecraft.level).addCharacter(character);
        } else {
            character.setName(name);
            character.setEmoteColor(emoteColor);
            character.setDescription(description);
        }

        SkinManager.loadSkin(skinURL, character.getUUID().toString());

        character.getPlayerInfo().ifPresentOrElse(
                info -> {
                    info.setSkinURL(skinURL);
                    info.setModel(PlayerInfo.SkinModel.fromMinecraftModel(model));
                },
                () -> character.setPlayerInfo(new PlayerInfo(skinURL, PlayerInfo.SkinModel.fromMinecraftModel(model)))
        );
    }

    private void navigateToNextScreen(boolean isNewCharacter) {
        if (minecraft == null) return;

        if (isNewCharacter) {
            minecraft.setScreen(new RoleSelectionScreen(previousScreen, character));
        } else {
            if (previousScreen instanceof CharacterSelectionScreen selectionScreen) {
                CharacterManager characterManager = CharacterManager.get(minecraft.level);
                Character activeCharacter = characterManager.getActiveCharacter(minecraft.player);

                if (activeCharacter != null && activeCharacter.getUUID().equals(character.getUUID())) {
                    selectionScreen.switchCharacter(character);
                }
            }

            minecraft.setScreen(previousScreen);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderPlayer(guiGraphics);
        renderLabels(guiGraphics);
    }

    private void renderLabels(@NotNull GuiGraphics guiGraphics) {
        int startX = screenX + MARGIN + 120;
        int startY = screenY + MARGIN + 5;
        int spacing = font.lineHeight * 2 + 5;

        guiGraphics.drawString(font, "Name:", startX, startY, TEXT_COLOR, false);
        startY += spacing;
        guiGraphics.drawString(font, "Emote Color:", startX, startY, TEXT_COLOR, false);
        startY += spacing;
        guiGraphics.drawString(font, "Skin URL:", startX, startY, TEXT_COLOR, false);
        startY += spacing;
        guiGraphics.drawString(font, "Description:", startX, startY, TEXT_COLOR, false);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.blitSprite(RenderPipelines.GUI, BACKGROUND, screenX, screenY, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void renderPlayer(@NotNull GuiGraphics guiGraphics) {
        int playerX = screenX + MARGIN + 6;
        int playerY = screenY + MARGIN;

        AbstractClientPlayer fakePlayer = getAbstractClientPlayer(minecraft.player.getUUID(), nameField.getValue(),
                skin, isWideModel ? PlayerSkin.Model.WIDE : PlayerSkin.Model.SLIM);

        renderEntityInInventoryFollowsAngle(
                guiGraphics,
                playerX,
                playerY,
                playerX + PLAYER_RENDER_WIDTH,
                playerY + PLAYER_RENDER_HEIGHT,
                RENDER_SCALE,
                0f, rotationX, 0f,
                fakePlayer
        );
    }

    public static void renderEntityInInventoryFollowsAngle(@NotNull GuiGraphics graphics, int x1, int y1, int x2, int y2, int scale, float yOffset, float horizontalRotation, float verticalRotation, @NotNull LivingEntity entity) {
        float centerX = (float) (x1 + x2) / 2.0F;
        float centerY = (float) (y1 + y2) / 2.0F;

        graphics.enableScissor(x1, y1, x2, y2);

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

        renderEntityInInventory(graphics, x1, y1, x2, y2, adjustedScale, translation, baseRotation, verticalTiltRotation, entity);

        entity.yBodyRot = originalBodyRotation;
        entity.setYRot(originalYaw);
        entity.setXRot(originalPitch);
        entity.yHeadRotO = originalHeadRotationOld;
        entity.yHeadRot = originalHeadRotation;

        graphics.disableScissor();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isMouseInPlayerRenderArea(mouseX, mouseY)) {
            rotationX -= (float) dragX * ROTATION_SENSITIVITY;
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private boolean isMouseInPlayerRenderArea(double mouseX, double mouseY) {
        int playerX = screenX + MARGIN + 5;
        int playerY = screenY + MARGIN;

        return mouseX >= playerX &&
                mouseY >= playerY &&
                mouseX <= playerX + PLAYER_RENDER_WIDTH &&
                mouseY <= playerY + PLAYER_RENDER_HEIGHT;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
