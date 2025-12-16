package com.site21.bittermelon.common.systems.medical.client;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.client.networking.OpenHealthScreenC2S;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentData;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HealthScreen extends Screen {
    private final UUID characterUUID;
    private Character character;
    private final MedicalStats medicalStats;
    private final List<CompartmentWidget> compartmentWidgets;
    private CompartmentWidget activeWidget = null;
    private HeldItemData heldItemData = null;

    public HealthScreen(@NotNull Character character) {
        super(Component.literal(character.getName()));
        this.characterUUID = character.getUUID();
        this.character = character;
        this.medicalStats = character.getMedicalStats();
        this.compartmentWidgets = new ArrayList<>();
        // TODO: Implement client listener for medical stats
    }

    @Override
    protected void init() {
        CompartmentInstance mainCompartment = medicalStats.getMainCompartment();
        LayerData layer = mainCompartment.getLayers().getFirst();
        if (layer == null) return;

        compartmentWidgets.add(new CompartmentWidget(
                        20,
                        20,
                        layer.getWidth() * 10,
                layer.getHeight() * 10,
                        medicalStats.getMainCompartment(),
                this
                )
        );
    }

    public void addCompartmentSpace(@NotNull CompartmentInstance instance) {
        // Only open compartments that have layers
        LayerData layer = instance.getLayers().getFirst();
        if (layer == null) return;
        if (compartmentWidgets.stream().anyMatch(widget -> widget.getCompartment().equals(instance))) return;

        compartmentWidgets.add(new CompartmentWidget(
                20,
                20,
                layer.getWidth() * 20,
                layer.getHeight() * 20,
                instance,
                this
        ));
    }

    public void removeCompartmentSpace(CompartmentWidget compartmentSpace) {
        if (compartmentSpace == compartmentWidgets.getFirst()) {
            onClose();
            return;
        }
        compartmentWidgets.remove(compartmentSpace);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (CompartmentWidget widget : compartmentWidgets) {
            widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        renderHeldItem(guiGraphics, mouseX, mouseY);
    }

    private void renderHeldItem(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (heldItemData != null) {
            CompartmentData instance = heldItemData.heldItem().get(BitterDataComponents.COMPARTMENT);
            if (instance == null || instance.getVisualData().getIcon() == null) {
                guiGraphics.renderFakeItem(heldItemData.heldItem(), mouseX, mouseY);
            } else {
                VisualData visualData = instance.getVisualData();
                float scaleFactor = visualData.scale;
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().translate(mouseX + visualData.x, mouseY + visualData.y);
                guiGraphics.pose().scale(scaleFactor, scaleFactor);

                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, visualData.icon, 0, 0, 20, 20);

                guiGraphics.pose().popMatrix();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CompartmentWidget widget : compartmentWidgets.reversed()) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                activeWidget = widget;
//                if (heldItemData != null) {
//                    widget.handleCompartmentInteraction(mouseX, mouseY, button);
//                }
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (activeWidget != null) {
            return activeWidget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (activeWidget != null) {
            boolean result = activeWidget.mouseReleased(mouseX, mouseY, button);
            activeWidget = null;
            return result;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    public Character getCharacter() {
        return CharacterManager.get(minecraft.level).getCharacter(characterUUID);
    }

    public MedicalStats getMedicalStats() {
        return medicalStats;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public HeldItemData getHeldItemData() {
        return heldItemData;
    }

    public void setHeldItemData(HeldItemData heldItemData) {
        this.heldItemData = heldItemData;
    }

    public void refresh() {
//        for (CompartmentWidget widget : renderedCompartmentSpaces) {
//            CompartmentInstance updated = medicalStats.getCompartment(widget.getCompartment().getId());
//            widget.setCompartment(updated);
//            widget.refreshCompartmentNodes();
//
//            // TODO: Refresh specific widgets by mapping widgets to compartment UUIDs?
//        }
    }

    public static void openHealthScreen() {
        Minecraft mc = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        HitResult hitResult = mc.hitResult;
        if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) hitResult;
            ClientPacketDistributor.sendToServer(new OpenHealthScreenC2S(player.getUUID(), entityHit.getEntity().getUUID()));
        } else {
            ClientPacketDistributor.sendToServer(new OpenHealthScreenC2S(player.getUUID(), UUID.randomUUID()));
        }

    }
}

