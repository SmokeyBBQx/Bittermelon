package com.site21.bittermelon.common.systems.medical.client;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.component.medical.MedicalInstrument;
import com.site21.bittermelon.common.systems.medical.client.tool.InstrumentWidget;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.common.systems.medical.networking.OpenHealthScreenC2S;
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

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.VISUAL_DATA;

public class HealthScreen extends Screen {
    private final UUID characterId;
    private final List<CompartmentWidget> compartmentWidgets;
    private final List<InstrumentWidget> instrumentWidgets;
    private CompartmentWidget activeWidget = null;
    private CompartmentInstance heldCompartment = null;
    private InstrumentWidget heldTool = null;

    public HealthScreen(@NotNull Character character) {
        super(Component.literal(character.getName()));
        this.characterId = character.getId();
        this.compartmentWidgets = new ArrayList<>();
        this.instrumentWidgets = new ArrayList<>();
        initTools();
    }

    @Override
    protected void init() {
        MedicalStats medicalStats = getMedicalStats();
        CompartmentInstance mainCompartment = medicalStats.getMainCompartment();
        LayerData layer = CompartmentUtil.getTopLayer(mainCompartment);
        if (layer == null) return;

        compartmentWidgets.add(new CompartmentWidget(20, 20, layer.getWidth() * 10, layer.getHeight() * 10, medicalStats.getMainCompartment(), this));
    }

    private void initTools() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        player.getInventory().iterator().forEachRemaining(stack ->
                stack.getComponents().iterator().forEachRemaining(component -> {
                    if (component.value() instanceof MedicalInstrument instrument) {
                        instrumentWidgets.add(instrument.createWidget(stack, 10, 10, 32, 32, this));
                    }
                }));
    }

    public boolean addCompartmentSpace(@NotNull CompartmentInstance instance) {
        // Only open compartments that have layers
        LayerData layer = CompartmentUtil.getTopLayer(instance);
        if (layer == null) return false;
        if (compartmentWidgets.stream().anyMatch(widget -> widget.getCompartment().equals(instance))) return false;

        compartmentWidgets.add(new CompartmentWidget(20, 20, layer.getWidth() * 20, layer.getHeight() * 20, instance, this));

        return true;
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

        renderHeldCompartment(guiGraphics, mouseX, mouseY);

        for (InstrumentWidget widget : instrumentWidgets) {
            widget.render(guiGraphics, mouseX, mouseY, partialTick);
            if (widget == heldTool) {
                // Renders at mouse position if held
                heldTool.renderTool(guiGraphics, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBlurredBackground(@NotNull GuiGraphics guiGraphics) {
    }

    private void renderHeldCompartment(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (heldCompartment == null) return;
        if (heldCompartment.has(VISUAL_DATA)) return;

        VisualData visualData = heldCompartment.get(VISUAL_DATA);

        if (visualData.icon() != null) {
            float scaleFactor = visualData.scale();
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(mouseX + visualData.x(), mouseY + visualData.y());
            guiGraphics.pose().scale(scaleFactor, scaleFactor);

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, visualData.icon(), 0, 0, 0, 0, 20,
                    20, 20, 20);

            guiGraphics.pose().popMatrix();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        CompartmentInstance previouslyHeld = heldCompartment;

        if (heldTool != null) {
            if (!heldTool.mouseClicked(mouseX, mouseY, button)) {
                heldTool = null;
            }
        } else {
            for (InstrumentWidget widget : instrumentWidgets) {
                if (widget.mouseClicked(mouseX, mouseY, button)) {
                    heldTool = widget;
                    return true;
                }
            }
        }

        for (CompartmentWidget widget : compartmentWidgets.reversed()) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                activeWidget = widget;
                if (previouslyHeld != null) {
                    if (widget.tryToPlace((int) mouseX, (int) mouseY, previouslyHeld)) {
                        heldCompartment = null;
                    }
                }
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (heldTool != null) {
            return heldTool.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        if (activeWidget != null) {
            return activeWidget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (heldTool != null) {
            return heldTool.mouseReleased(mouseX, mouseY, button);
        }

        if (activeWidget != null) {
            boolean result = activeWidget.mouseReleased(mouseX, mouseY, button);
            activeWidget = null;
            return result;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    public CompartmentWidget getHoveredCompartmentWidget(double mouseX, double mouseY) {
        for (CompartmentWidget widget : compartmentWidgets.reversed()) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                return widget;
            }
        }
        return null;
    }

    /**
     * Does a fresh lookup of the character to ensure we have the latest data from the server.
     */
    public Character getCharacter() {
        return CharacterManager.get(minecraft.level).getCharacter(characterId);
    }

    public UUID getCharacterId() {
        return characterId;
    }

    public MedicalStats getMedicalStats() {
        return getCharacter().getMedicalStats();
    }

    public CompartmentInstance getHeldCompartment() {
        return heldCompartment;
    }

    public void setHeldCompartment(CompartmentInstance heldCompartment) {
        this.heldCompartment = heldCompartment;
    }

    public void onLayerChanged(CompartmentWidget widget) {
        for (InstrumentWidget tool : instrumentWidgets) {
            tool.onLayerChanged(widget);
        }
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

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

