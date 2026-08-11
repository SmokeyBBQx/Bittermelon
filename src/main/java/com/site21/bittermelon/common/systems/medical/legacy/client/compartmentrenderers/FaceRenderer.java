package com.site21.bittermelon.common.systems.medical.legacy.client.compartmentrenderers;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class FaceRenderer implements SpecialCompartmentRenderer {

    @Override
    public void extract(GuiGraphicsExtractor GuiGraphicsExtractor, int x, int y, int width, int height, Entity entity) {
        if (!(entity instanceof Player player)) return;

        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        Character character = CharacterManager.get(mc.level).getActiveCharacter(entity);

        Identifier playerTexture;

        if (character != null && character.getPlayerInfo().isPresent()) {
            playerTexture = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getId());
        } else {
            playerTexture = DefaultPlayerSkin.get(player.getUUID()).body().texturePath();
        }

        GuiGraphicsExtractor.pose().pushMatrix();
        GuiGraphicsExtractor.pose().translate(x, y);
        GuiGraphicsExtractor.pose().scale((float) width / 8, (float) height / 8);
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, playerTexture, 0, 0, 8, 8, 8,
                8, 64, 64);
        GuiGraphicsExtractor.pose().popMatrix();
    }
}
