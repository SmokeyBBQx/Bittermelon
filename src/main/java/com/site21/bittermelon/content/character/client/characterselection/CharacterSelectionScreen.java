package com.site21.bittermelon.content.character.client.characterselection;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.character.networking.SwitchCharacter;
import com.site21.bittermelon.networking.server.AddEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.world.effect.MobEffects.CONFUSION;

@OnlyIn(Dist.CLIENT)
public class CharacterSelectionScreen extends Screen {
    private static final int WIDGET_SIZE = 100;
    private static final int WIDGET_SPACING = 2;
    private static final int MARGIN = 10;

    private final CharacterManager characterManager;
    private final int maxCharacters;

    public CharacterSelectionScreen(int maxCharacters) {
        super(Component.literal("Character Selection"));
        assert Minecraft.getInstance().level != null;
        characterManager = CharacterManager.get(Minecraft.getInstance().level);
        this.maxCharacters = maxCharacters;
    }

    @Override
    protected void init() {
        int topY = height / 3;
        int leftX = width / 2 - (WIDGET_SIZE * maxCharacters + WIDGET_SPACING * (maxCharacters - 1)) / 2;
        int startY = topY + MARGIN;

        assert Minecraft.getInstance().player != null;
        Character activeCharacter = characterManager.getActiveCharacter(minecraft.player);
        List<Character> characters = characterManager.getCharactersByEntityUUID(Minecraft.getInstance().player.getUUID());
        characters = characters.reversed();

        for (int i = 0; i < characters.size(); i++) {
            addRenderableWidget(new CharacterWidget(
                    leftX + (WIDGET_SIZE + WIDGET_SPACING) * i,
                    startY,
                    WIDGET_SIZE,
                    WIDGET_SIZE,
                    characters.get(i),
                    this,
                    characters.get(i).equals(activeCharacter)
            ));
        }

        int emptySlotX = leftX + (WIDGET_SIZE + WIDGET_SPACING) * characters.size();

        if (characters.size() < maxCharacters) {
            for (int i = 0; i < maxCharacters - characters.size(); i++) {
                addRenderableWidget(new CharacterWidget(
                        emptySlotX + (WIDGET_SIZE + WIDGET_SPACING) * i,
                        startY,
                        WIDGET_SIZE,
                        WIDGET_SIZE,
                        null,
                        this,
                        false
                ));
            }
        }

    }

    public void switchCharacter(@NotNull Character character) {
        if (minecraft == null || minecraft.player == null) return;
        if (character == characterManager.getActiveCharacter(minecraft.player)) return;

        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PORTAL_TRAVEL, 1));
        PacketDistributor.sendToServer(new AddEffect(new MobEffectInstance(CONFUSION, 160, 255), minecraft.player.getId()));

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
