package com.site21.bittermelon.mixin;

import com.site21.bittermelon.common.systems.chat.AlphaContainer;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
    @Shadow
    @Final
    private List<GuiMessage> allMessages;
    @Shadow
    @Final
    public Minecraft minecraft;
    @Shadow
    @Final
    private List<GuiMessage.Line> trimmedMessages;
    @Shadow
    private int chatScrollbarPos;
    @Shadow
    private boolean newMessageSinceScroll;

    @Shadow
    public abstract void refreshTrimmedMessages();

    @Shadow
    protected abstract boolean isChatHidden();

    @Shadow
    public abstract int getLinesPerPage();

    @Shadow
    public abstract double getScale();

    @Shadow
    public abstract int getWidth();

    @Shadow
    protected abstract int getLineHeight();

    @Shadow
    protected abstract double screenToChatX(double x);

    @Shadow
    protected abstract double screenToChatY(double y);

    @Shadow
    protected abstract int getMessageEndIndexAt(double x, double y);

    @Shadow
    protected abstract int forEachLine(int linesPerPage, int tickCount, boolean focused, int bottomY, ChatComponent.LineConsumer action);

    @Unique
    private int bittermelon$lastMessageCount = 1;

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V", at = @At("HEAD"), cancellable = true)
    private void onAddMessage(Component chatComponent, @Nullable MessageSignature headerSignature, @Nullable GuiMessageTag tag, CallbackInfo info) {
        if (allMessages.isEmpty()) return;

        String newMessage = chatComponent.getString() + (bittermelon$lastMessageCount > 1 ? " (x" + bittermelon$lastMessageCount + ")" : "");

        if (allMessages.getFirst().content().getString().equals(newMessage)) {
            bittermelon$lastMessageCount++;

            GuiMessage guiMessage = new GuiMessage(
                    minecraft.gui.getGuiTicks(),
                    chatComponent.copy().append(" (x" + bittermelon$lastMessageCount + ")"),
                    headerSignature,
                    tag
            );
            allMessages.set(0, guiMessage);
            refreshTrimmedMessages();

            info.cancel();
        } else {
            bittermelon$lastMessageCount = 1;
        }
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(GuiGraphicsExtractor GuiGraphicsExtractor, int tickCount, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        // Just copied from ChatComponent#render - couldn't find a better way to modify the individual line alpha

        if (this.isChatHidden()) return;

        int i = this.getLinesPerPage();
        int j = this.trimmedMessages.size();
        if (j <= 0) return;

        ci.cancel();

        ProfilerFiller profilerfiller = Profiler.get();
        profilerfiller.push("chat");
        float f = (float) this.getScale();
        int k = Mth.ceil(this.getWidth() / f);
        int l = GuiGraphicsExtractor.guiHeight();
        GuiGraphicsExtractor.pose().pushMatrix();
        GuiGraphicsExtractor.pose().scale(f, f);
        GuiGraphicsExtractor.pose().translate(4.0F, 0.0F);
        int i1 = Mth.floor((l - 40) / f);
        int j1 = this.getMessageEndIndexAt(this.screenToChatX(mouseX), this.screenToChatY(mouseY));
        float f1 = this.minecraft.options.chatOpacity().get().floatValue() * 0.9F + 0.1F;
        float f2 = this.minecraft.options.textBackgroundOpacity().get().floatValue();
        double d0 = this.minecraft.options.chatLineSpacing().get();
        int k1 = (int) Math.round(-8.0 * (d0 + 1.0) + 4.0 * d0);

        this.forEachLine(i, tickCount, focused, i1, (x, startY, endY, line, index, fade) -> {
            GuiGraphicsExtractor.fill(x - 4, startY, x + k + 4 + 4, endY, ARGB.color(fade * f2, -16777216));
            GuiMessageTag tag = line.tag();
            if (tag != null) {
                GuiGraphicsExtractor.fill(x - 4, startY, x - 2, endY, ARGB.color(fade * f1, tag.indicatorColor()));
            }
        });

        int l1 = this.forEachLine(i, tickCount, focused, i1, (x, startY, endY, line, index, fade) -> {
            int textY = endY + k1;
            float distanceAlpha = 1.0f;
            if (index < allMessages.size()) {
                distanceAlpha = ((AlphaContainer) allMessages.get(index).content().getStyle()).bittermelon$getAlpha();
            }
            GuiGraphicsExtractor.drawString(this.minecraft.font, line.content(), x, textY, ARGB.color(distanceAlpha * f1 * fade, -1));
        });

        long i2 = this.minecraft.getChatListener().queueSize();
        if (i2 > 0L) {
            int j2 = (int) (128.0F * f1);
            int k2 = (int) (255.0F * f2);
            GuiGraphicsExtractor.pose().pushMatrix();
            GuiGraphicsExtractor.pose().translate(0.0F, i1);
            GuiGraphicsExtractor.fill(-2, 0, k + 4, 9, k2 << 24);
            GuiGraphicsExtractor.drawString(this.minecraft.font, Component.translatable("chat.queue", i2), 0, 1, ARGB.color(j2, -1));
            GuiGraphicsExtractor.pose().popMatrix();
        }

        if (focused) {
            int j4 = this.getLineHeight();
            int k4 = j * j4;
            int l2 = l1 * j4;
            int i3 = chatScrollbarPos * l2 / j - i1;
            int j3 = l2 * l2 / k4;
            if (k4 != l2) {
                int k3 = i3 > 0 ? 170 : 96;
                int l3 = newMessageSinceScroll ? 13382451 : 3355562;
                int i4 = k + 4;
                GuiGraphicsExtractor.fill(i4, -i3, i4 + 2, -i3 - j3, ARGB.color(k3, l3));
                GuiGraphicsExtractor.fill(i4 + 2, -i3, i4 + 1, -i3 - j3, ARGB.color(k3, 13421772));
            }
        }

        GuiGraphicsExtractor.pose().popMatrix();
        profilerfiller.pop();
    }
}
