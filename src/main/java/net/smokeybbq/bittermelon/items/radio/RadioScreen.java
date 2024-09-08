package net.smokeybbq.bittermelon.items.radio;

import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import net.smokeybbq.bittermelon.items.radio.networking.RadioSetActiveFrequencyC2SPacket;
import net.smokeybbq.bittermelon.items.radio.networking.RadioTogglePresetC2SPacket;
import net.smokeybbq.bittermelon.items.radio.networking.*;
import net.smokeybbq.bittermelon.networking.PacketHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RadioScreen extends Screen {
    private final ItemStack radioStack;
    private EditBox frequencyInput;
    private EditBox presetNameInput;
    private Button addPresetButton;
    private Button confirmAddPresetButton;
    private Button cancelAddPresetButton;
    private PresetListPanel presetListPanel;
    private boolean showAddPresetFields = false;
    private RadioItem.Preset editingPreset = null;
    private Component statusMessage = Component.empty();
    private long statusMessageTime = 0;
    private static final long STATUS_MESSAGE_DURATION = 3000;
    private static final long FADE_DURATION = 1000;

    public RadioScreen(ItemStack radioItem) {
        super(Component.literal(radioItem.getDisplayName().toString()));
        this.radioStack = radioItem;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = width / 2;
        int centerY = height / 2;

        addPresetButton = addRenderableWidget(Button.builder(Component.literal("Add Preset"), (button) -> {
            showAddPresetFields = true;
            editingPreset = null;
            updateVisibility();
        }).bounds(centerX - 100, centerY, 200, 20).build());

        frequencyInput = addRenderableWidget(new EditBox(font, centerX - 100, centerY + 25, 200, 20, Component.literal("Frequency")));

        presetNameInput = addRenderableWidget(new EditBox(font, centerX - 100, centerY + 50, 200, 20, Component.literal("Preset Name")));

        confirmAddPresetButton = addRenderableWidget(Button.builder(Component.literal("Confirm"), (button) -> {
            try {
                float frequency = Float.parseFloat(frequencyInput.getValue());
                RadioItem radioItem = (RadioItem) radioStack.getItem();
                if (frequency < radioItem.getMinFrequency() || frequency > radioItem.getMaxFrequency()) {
                    setStatusMessage("Frequency not within bounds");
                    return;
                }
                String presetName = presetNameInput.getValue();
                if (presetName.isEmpty()) {
                    setStatusMessage("Preset name cannot be empty");
                    return;
                }
                if (editingPreset != null) {
                    PacketHandler.INSTANCE.sendToServer(new RadioEditPresetC2SPacket(frequency, editingPreset.frequency, presetName));
                    setStatusMessage("Updated preset: " + presetName + " (" + frequency + ")");
                } else {
                    PacketHandler.INSTANCE.sendToServer(new RadioAddPresetC2SPacket(frequency, presetName));
                    setStatusMessage("Added preset: " + presetName + " (" + frequency + ")");
                }
                showAddPresetFields = false;
                editingPreset = null;
                frequencyInput.setValue("");
                presetNameInput.setValue("");
                updateVisibility();
            } catch (NumberFormatException e) {
                setStatusMessage("Invalid frequency format");
            }
        }).bounds(centerX - 100, centerY + 95, 95, 20).build());

        cancelAddPresetButton = addRenderableWidget(Button.builder(Component.literal("Cancel"), (button) -> {
            showAddPresetFields = false;
            editingPreset = null;
            frequencyInput.setValue("");
            presetNameInput.setValue("");
            updateVisibility();
        }).bounds(centerX + 5, centerY + 95, 95, 20).build());

        presetListPanel = new PresetListPanel(minecraft, 200, 100, centerY + 20, centerX - 100);

        updateVisibility();
        updatePresetList();
    }

    public void updateRendering(List<RadioItem.Preset> presets, String activeChannel) {
        updatePresets(presets);
        presetListPanel.setActiveChannel(activeChannel);
    }

    private void updateVisibility() {
        addPresetButton.visible = !showAddPresetFields;
        frequencyInput.visible = showAddPresetFields;
        presetNameInput.visible = showAddPresetFields;
        confirmAddPresetButton.visible = showAddPresetFields;
        cancelAddPresetButton.visible = showAddPresetFields;

        if (!showAddPresetFields) {
            addRenderableWidget(presetListPanel);
            updatePresetList();
        } else {
            removeWidget(presetListPanel);
            updatePresetList();
        }
    }

    private void setStatusMessage(String message) {
        statusMessage = Component.literal(message);
        statusMessageTime = System.currentTimeMillis();
    }

    public void updatePresets(List<RadioItem.Preset> presets) {
        presetListPanel.updatePresets(presets);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        RadioItem radioItem = (RadioItem) radioStack.getItem();

        guiGraphics.drawCenteredString(font, radioStack.getDisplayName(), width / 2, 40, 0xFFFFFF);


        guiGraphics.drawString(font, "Freq. Range: " + radioItem.getMinFrequency() + " - " + radioItem.getMaxFrequency() + " MHz",width / 2 - 100, 60, 0xFFFFFF);
        guiGraphics.drawString(font, "Two-Way: " + radioItem.canTransmit(),width / 2 - 100, 80, 0xFFFFFF);
        if (radioItem.canTransmit()) {
            guiGraphics.drawString(font, "Range: " + radioItem.getTransmitRange() + "m", width / 2 - 100, 100, 0xFFFFFF);
        }

        guiGraphics.drawCenteredString(font, "Channels", width / 2, 155, 0xFFFFFF);

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - statusMessageTime;

        if (elapsedTime < STATUS_MESSAGE_DURATION + FADE_DURATION) {
            int alpha = calculateAlpha(elapsedTime);
            int color = (alpha << 24) | 0xFFFFFF;
            guiGraphics.drawCenteredString(font, statusMessage, width / 2, height - 20, color);
        }
    }

    private int calculateAlpha(long elapsedTime) {
        if (elapsedTime < STATUS_MESSAGE_DURATION) {
            return 255;
        } else {
            long fadeTime = elapsedTime - STATUS_MESSAGE_DURATION;
            return Math.max(0, 255 - (int) (255 * fadeTime / FADE_DURATION));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (System.currentTimeMillis() - statusMessageTime >= STATUS_MESSAGE_DURATION + FADE_DURATION) {
            statusMessage = Component.empty();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void updatePresetList() {
        PacketHandler.INSTANCE.sendToServer(new RadioGetPresetsC2SPacket());
    }

    private class PresetListPanel extends ScrollPanel {
        private List<RadioItem.Preset> presets = new ArrayList<>();
        private String activeChannel = "";

        public PresetListPanel(Minecraft minecraft, int width, int height, int top, int left) {
            super(minecraft, width, height, top, left);
        }

        @Override
        protected int getContentHeight() {
            return presets.size() * 24;
        }

        @Override
        protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
            int y = (int) (mouseY - this.top) + (int) this.scrollDistance;
            int index = y / 24;

            for (int i = 0; i < presets.size(); i++) {
                RadioItem.Preset preset = presets.get(i);
                int yPos = relativeY + i * 24;
                if (index == i) {
                    guiGraphics.drawString(font, preset.name + " (" + preset.frequency + " MHz) " + "<<", left + 5, yPos + 6, 0xFFFFFF);
                } else {
                    guiGraphics.drawString(font, preset.name + " (" + preset.frequency + " MHz) ", left + 5, yPos + 6, 0xFFFFFF);
                }

                if (activeChannel.equals(String.valueOf(preset.frequency))) {
                    guiGraphics.drawString(font, preset.active ? "Selected" : "Inactive", left + 5, yPos + 16, preset.active ? 0x00FF00 : 0xFF0000);
                } else {
                    guiGraphics.drawString(font, preset.active ? "Active" : "Inactive", left + 5, yPos + 16, preset.active ? 0x26870B : 0xFF0000);
                }
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!isMouseOver(mouseX, mouseY)) {
                return false;
            }

            int relativeY = (int) (mouseY - this.top) + (int) this.scrollDistance;
            int index = relativeY / 24;
            if (index >= 0 && index < presets.size()) {
                RadioItem.Preset clickedPreset = presets.get(index);
                if (button == 0) {  // Left click
                    if (Minecraft.getInstance().player != null) {
                        PacketHandler.INSTANCE.sendToServer(new RadioTogglePresetC2SPacket(clickedPreset.frequency));
                        setStatusMessage("Toggled preset: " + clickedPreset.name);
                    }
                } else if (button == 1) {  // Right click
                    if (clickedPreset.active) {
                        PacketHandler.INSTANCE.sendToServer(new RadioSetActiveFrequencyC2SPacket(clickedPreset.frequency));
                        setStatusMessage("Set active frequency: " + clickedPreset.name);
                    } else {
                        setStatusMessage("Channel not active: " + clickedPreset.name);
                    }
                } else if (button == 2) {  // Middle click
                    editingPreset = clickedPreset;
                    frequencyInput.setValue(String.valueOf(clickedPreset.frequency));
                    presetNameInput.setValue(clickedPreset.name);
                    showAddPresetFields = true;
                    updateVisibility();
                    setStatusMessage("Editing preset: " + clickedPreset.name);
                }
                return true;
            }
            return false;
        }

        public void updatePresets(List<RadioItem.Preset> newPresets) {
            this.presets.clear();
            this.presets = newPresets;
        }

        public void setActiveChannel(String activeChannel) {
            this.activeChannel = activeChannel;
        }

        @Override
        public @NotNull NarrationPriority narrationPriority() {
            return NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

        }
    }
}
