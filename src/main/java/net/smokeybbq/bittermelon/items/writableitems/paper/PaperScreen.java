package net.smokeybbq.bittermelon.items.writableitems.paper;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.Collections;
import java.util.List;

public class PaperScreen extends Screen {
    private static final ResourceLocation PAPER_LOCATION = new ResourceLocation("bittermelon:textures/gui/paper.png");
    private Component pageMsg = CommonComponents.EMPTY;
    private List<FormattedCharSequence> cachedPageComponents = Collections.emptyList();
    protected PaperScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        int i = (this.width - 192) / 2;
        int j = 2;
        pGuiGraphics.blit(PAPER_LOCATION, i, 2, 0, 0, 192, 192);

        int i1 = this.font.width(this.pageMsg);
        pGuiGraphics.drawString(this.font, this.pageMsg, i - i1 + 192 - 44, 18, 0, false);
        int k = Math.min(128 / 9, this.cachedPageComponents.size());

        for(int l = 0; l < k; ++l) {
            FormattedCharSequence formattedcharsequence = this.cachedPageComponents.get(l);
            pGuiGraphics.drawString(this.font, formattedcharsequence, i + 36, 32 + l * 9, 0, false);
        }

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    protected void closeScreen() {
        this.minecraft.setScreen((Screen)null);
    }


}
