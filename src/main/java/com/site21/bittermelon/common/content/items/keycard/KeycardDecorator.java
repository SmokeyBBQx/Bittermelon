package com.site21.bittermelon.common.content.items.keycard;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelRegistry;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jetbrains.annotations.NotNull;

public class KeycardDecorator implements IItemDecorator {
    @Override
    public boolean render(GuiGraphicsExtractor GuiGraphicsExtractor, Font font, @NotNull ItemStack stack, int xOffset, int yOffset) {
        if (!stack.has(BitterDataComponents.ID_NUMBER)) return false;

        Minecraft mc = Minecraft.getInstance();
        PersonnelEntry entry = PersonnelRegistry.get(mc.level).getEntry(stack.get(BitterDataComponents.ID_NUMBER));
        if (entry == null) return false;
        Identifier texture = DefaultPlayerSkin.get(entry.getPlayerUUID()).body().texturePath();

        Character character = CharacterManager.get(mc.level).getCharacter(entry.getCharacterUUID());
        if (character != null && character.getPlayerInfo().isPresent()) {
            texture = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getId());
        }

        GuiGraphicsExtractor.pose().pushMatrix();
        GuiGraphicsExtractor.pose().translate(xOffset + 3, yOffset + 9);
        GuiGraphicsExtractor.pose().scale(0.5f, 0.5f);
        GuiGraphicsExtractor.pose().rotate(-44.8f);

        int textureSize = 64;
        int headSize = 8;

        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 8, 8, headSize, headSize, textureSize, textureSize);
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 40, 8, headSize, headSize, textureSize, textureSize);

        GuiGraphicsExtractor.pose().popMatrix();

        return true;
    }
}
