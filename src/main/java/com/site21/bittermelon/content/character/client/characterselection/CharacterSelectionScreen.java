package com.site21.bittermelon.content.character.client.characterselection;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.character.networking.SwitchCharacter;
import com.site21.bittermelon.networking.server.AddEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BooleanSupplier;

import static net.minecraft.world.effect.MobEffects.CONFUSION;

@OnlyIn(Dist.CLIENT)
public class CharacterSelectionScreen extends Screen {
    private static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "");
    private final CharacterManager characterManager;
    private int maxCharacters;
    private int leftX;
    private int topY;
    private int screenWidth;
    private int screenHeight;
    private static final int MARGIN = 10;

    public CharacterSelectionScreen() {
        super(Component.literal("Character Selection"));
        assert Minecraft.getInstance().level != null;
        characterManager = CharacterManager.get(Minecraft.getInstance().level);
        maxCharacters = 5;
    }

    @Override
    protected void init() {
        int widgetWidth = 100;
        int widgetHeight = 100;
        int widgetSpacing = 2;
        topY = height / 3;
        leftX = width / 2 - (widgetWidth * maxCharacters + widgetSpacing * (maxCharacters - 1)) / 2;
        int startY = topY + MARGIN;
        screenWidth = widgetWidth * maxCharacters + widgetSpacing * (maxCharacters - 1) + MARGIN;
        screenHeight = widgetHeight + MARGIN;

        assert Minecraft.getInstance().player != null;
        List<Character> characters = characterManager.getCharactersByEntityUUID(Minecraft.getInstance().player.getUUID());

        for (int i = 0; i < characters.size(); i++) {
            addRenderableWidget(new CharacterWidget(
                    leftX + (widgetWidth + widgetSpacing) * i,
                    startY,
                    widgetWidth,
                    widgetHeight,
                    characters.get(i),
                    this
            ));
        }

        int emptySlotX = leftX + (widgetWidth + widgetSpacing) * characters.size();

        if (characters.size() < maxCharacters) {
            for (int i = 0; i < maxCharacters - characters.size(); i++) {
                addRenderableWidget(new CharacterWidget(
                        emptySlotX + (widgetWidth + widgetSpacing) * i,
                        startY,
                        widgetWidth,
                        widgetHeight,
                        null,
                        this
                ));
            }
        }

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void switchCharacter(@NotNull Character character) {
        if (minecraft == null || minecraft.player == null) return;
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PORTAL_TRAVEL, 1));
        minecraft.player.addEffect(new MobEffectInstance(CONFUSION, 160, 255));

        PacketDistributor.sendToServer(new SwitchCharacter(character.getEntityUUID(), character.getUUID()));
        characterManager.setActiveCharacter(minecraft.player, character.getUUID());
        minecraft.player.sendSystemMessage(Component.literal("Switched to: " + character.getName()).withStyle(ChatFormatting.GREEN));

        onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
