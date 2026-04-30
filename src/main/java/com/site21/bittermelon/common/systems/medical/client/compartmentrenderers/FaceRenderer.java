package com.site21.bittermelon.common.systems.medical.client.compartmentrenderers;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class FaceRenderer implements SpecialCompartmentRenderer {

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, int width, int height, Entity entity) {
        if (!(entity instanceof Player player)) return;

        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        Character character = CharacterManager.get(mc.level).getActiveCharacter(entity);

        ResourceLocation playerTexture;

        if (character != null && character.getPlayerInfo().isPresent()) {
            playerTexture = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + character.getId());
        } else {
            playerTexture = DefaultPlayerSkin.get(player.getUUID()).texture();
        }

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale((float) width / 8, (float) height / 8);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, playerTexture, 0, 0, 8, 8, 8,
                8, 64, 64);
        guiGraphics.pose().popMatrix();
    }
}
