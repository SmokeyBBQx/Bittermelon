package com.site21.bittermelon.common.content.items.keycard;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelRegistry;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jetbrains.annotations.NotNull;

public class KeycardDecorator implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, @NotNull ItemStack stack, int xOffset, int yOffset) {
        if (!stack.has(BitterDataComponents.ID_NUMBER)) return false;

        Minecraft mc = Minecraft.getInstance();
        PersonnelEntry entry = PersonnelRegistry.get(mc.level).getEntry(stack.get(BitterDataComponents.ID_NUMBER));
        if (entry == null) return false;
        ResourceLocation texture = DefaultPlayerSkin.get(entry.getPlayerUUID()).texture();

        Character character = CharacterManager.get(mc.level).getCharacter(entry.getCharacterUUID());
        if (character != null && character.getPlayerInfo().isPresent()) {
            texture = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getUUID());
        }

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(xOffset + 3, yOffset + 9);
        guiGraphics.pose().scale(0.5f, 0.5f);
        guiGraphics.pose().rotate(-44.8f);

        int textureSize = 64;
        int headSize = 8;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 8, 8, headSize, headSize, textureSize, textureSize);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 40, 8, headSize, headSize, textureSize, textureSize);

        guiGraphics.pose().popMatrix();

        return true;
    }
}
