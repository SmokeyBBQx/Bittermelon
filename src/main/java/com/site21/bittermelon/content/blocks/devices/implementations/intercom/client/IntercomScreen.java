package com.site21.bittermelon.content.blocks.devices.implementations.intercom.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.networking.IntercomIDUpdate;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.networking.IntercomMicUpdate;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.networking.IntercomSpeakerUpdate;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.networking.IntercomTargetUpdate;
import com.site21.bittermelon.content.telecomms.intercom.IntercomManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class IntercomScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/intercom.png");

    private final IntercomBlockEntity intercom;
    private final boolean canEdit;
    private EditBox intercomIDField;
    private EditBox targetIDField;
    private EditBox searchField;
    private Button speakerToggle;
    private Button micToggle;
    private IntercomList intercomList;
    private static final int ELEMENT_WIDTH = 120;
    private static final int ELEMENT_HEIGHT = 20;
    private static final int LIST_WIDTH = 150;
    private static final int LIST_HEIGHT = 140;

    public IntercomScreen(IntercomBlockEntity intercom, boolean canEdit) {
        super(Component.translatable("screen.bittermelon.intercom"));
        this.intercom = intercom;
        this.canEdit = canEdit;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = width / 2;
        int centerY = height / 2;
        int startY = centerY - 93;

        int leftColumnX = centerX - 105;

        intercomIDField = new EditBox(font,
                leftColumnX,
                startY - 8,
                ELEMENT_WIDTH,
                ELEMENT_HEIGHT,
                Component.literal("ID")
        );
        intercomIDField.setValue(intercom.getIntercomID());
        intercomIDField.setMaxLength(16);
        intercomIDField.setResponder(this::onIntercomIDChanged);
        intercomIDField.setEditable(canEdit);
        addRenderableWidget(intercomIDField);

        targetIDField = new EditBox(font,
                leftColumnX,
                startY + 28,
                ELEMENT_WIDTH,
                ELEMENT_HEIGHT,
                Component.literal("Target ID")
        );
        targetIDField.setValue(intercom.getTargetID());
        targetIDField.setMaxLength(32);
        targetIDField.setResponder(this::onTargetIDChanged);
        addRenderableWidget(targetIDField);

//        speakerToggle = Button.builder(
//                getSpeakerButtonText(),
//                button -> toggleSpeaker()
//        ).pos(
//                leftColumnX,
//                startY + 65
//        ).size(ELEMENT_WIDTH, ELEMENT_HEIGHT).build();
//        addRenderableWidget(speakerToggle);
//
//        micToggle = Button.builder(
//                getMicButtonText(),
//                button -> toggleMic()
//        ).pos(
//                leftColumnX,
//                startY + 90
//        ).size(ELEMENT_WIDTH, ELEMENT_HEIGHT).build();
//        addRenderableWidget(micToggle);

        searchField = new EditBox(font,
                width / 2 - 15,
                startY - 10,
                ELEMENT_WIDTH,
                ELEMENT_HEIGHT,
                Component.literal("Search")
        );
        searchField.setMaxLength(32);
        searchField.setResponder(this::updateSearch);
        addRenderableWidget(searchField);

        intercomList = new IntercomList(
                minecraft,
                ELEMENT_WIDTH,
                LIST_HEIGHT,
                startY,
                12
        );
        addRenderableWidget(intercomList);
        updateIntercomList("");
    }

    private void updateSearch(@NotNull String searchTerm) {
        updateIntercomList(searchTerm.toLowerCase());
    }

    private void updateIntercomList(String searchTerm) {
        if (intercom.getLevel() == null) return;
        IntercomManager manager = IntercomManager.get(intercom.getLevel());
        Set<String> addedIDs = new HashSet<>();

        intercomList.clearEntries();
        intercomList.allIntercomIDs.clear();

        for (String id : manager.getIntercomIDs().values()) {
            if (!addedIDs.contains(id)) {
                addedIDs.add(id);
                intercomList.addIntercomID(id, this);
            }
        }

        if (!searchTerm.isEmpty()) {
            intercomList.updateSearch(searchTerm, this);
        }
    }
    private void onIntercomIDChanged(String newID) {
        PacketDistributor.sendToServer(new IntercomIDUpdate(newID, intercom.getBlockPos()));
        intercom.setIntercomID(newID);
    }

    private void onTargetIDChanged(String newID) {
        PacketDistributor.sendToServer(new IntercomTargetUpdate(newID, intercom.getBlockPos()));
    }

    @Contract(" -> new")
    private @NotNull Component getSpeakerButtonText() {
        return Component.literal("SPEAKER: ").append(intercom.isSpeakerOn() ? "ON" : "OFF");
    }

    @Contract(" -> new")
    private @NotNull Component getMicButtonText() {
        return Component.literal("MIC: ").append(intercom.isMicOn() ? "ON" : "OFF");
    }

    private void toggleSpeaker() {
        boolean newState = !intercom.isSpeakerOn();
        PacketDistributor.sendToServer(new IntercomSpeakerUpdate(newState, intercom.getBlockPos()));
        intercom.setSpeakerOn(newState);
        speakerToggle.setMessage(getSpeakerButtonText());
    }

    private void toggleMic() {
        boolean newState = !intercom.isMicOn();
        PacketDistributor.sendToServer(new IntercomMicUpdate(newState, intercom.getBlockPos()));
        intercom.setMicOn(newState);
        micToggle.setMessage(getMicButtonText());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        graphics.blit(
                TEXTURE,
                width / 2 - 125,
                height / 7,
                0,
                0,
                255,
                256
        );

        int centerX = width / 2;
        int leftColumnX = centerX - 120;
        int rightColumnX = centerX + 20;
//
//        graphics.drawString(
//                font,
//                Component.literal("ID"),
//                leftColumnX,
//                height / 2 - 72,
//                0xA0A0A0
//        );
//
//        graphics.drawString(
//                font,
//                Component.literal("Target"),
//                leftColumnX,
//                height / 2 - 37,
//                0xA0A0A0
//        );
//
//        graphics.drawString(
//                font,
//                Component.literal("Search"),
//                rightColumnX,
//                height / 2 - 72,
//                0xA0A0A0
//        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class IntercomList extends ObjectSelectionList<IntercomList.Entry> {
        private final List<String> allIntercomIDs = new ArrayList<>();

        public IntercomList(net.minecraft.client.Minecraft minecraft, int width, int height, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);
            this.setX(minecraft.getWindow().getGuiScaledWidth() / 2 - 15);
        }

        public void addIntercomID(String id, IntercomScreen screen) {
            allIntercomIDs.add(id);
            addEntry(new Entry(id, screen));
        }

        public void updateSearch(@NotNull String searchTerm, IntercomScreen screen) {
            clearEntries();

            for (String id : allIntercomIDs) {
                if (id.toLowerCase().contains(searchTerm.toLowerCase())) {
                    addEntry(new Entry(id, screen));
                }
            }
        }

        @Override
        public int getRowWidth() {
            return LIST_WIDTH - 20;
        }

        @Override
        public int addEntry(@NotNull IntercomList.Entry entry) {
            return super.addEntry(entry);
        }

        public void clearEntries() {
            super.clearEntries();
        }

        public static class Entry extends ObjectSelectionList.Entry<Entry> {
            private final String id;
            private final IntercomScreen screen;

            public Entry(String id, IntercomScreen screen) {
                this.id = id;
                this.screen = screen;
            }

            @Override
            public Component getNarration() {
                return Component.literal(id);
            }

            @Override
            public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
                int textColor = hovering ? 0xFFFF55 : 0x0a1928;
                graphics.drawString(screen.font, "> " + id, left + 5, top + 4, textColor);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 0) {
                    screen.targetIDField.setValue(id);
                    screen.onTargetIDChanged(id);
                    return true;
                }
                return false;
            }
        }
    }
}
