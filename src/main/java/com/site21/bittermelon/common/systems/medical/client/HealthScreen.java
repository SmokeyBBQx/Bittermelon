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
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.VISUAL_DATA;

public class HealthScreen extends Screen {
    private final LivingEntity entity;
    private final List<CompartmentWidget> compartmentWidgets;
    private final List<InstrumentWidget> instrumentWidgets;
    private CompartmentWidget activeWidget = null;
    private CompartmentInstance heldCompartment = null;
    private InstrumentWidget heldTool = null;

    public HealthScreen(@NotNull LivingEntity entity) {
        super(Component.literal("Health Screen"));
        this.entity = entity;
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

        CompartmentWidget widget = new CompartmentWidget(20, 20, layer.getWidth() * 20, layer.getHeight() * 20, instance, this);
        compartmentWidgets.add(widget);
        activeWidget = widget;

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
        assert visualData != null;

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
                if (widget.isHovered()) {
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
//            activeWidget = null;
            return result;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (activeWidget != null) {
            return activeWidget.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    public CompartmentWidget getHoveredCompartmentWidget(double mouseX, double mouseY) {
        for (CompartmentWidget widget : compartmentWidgets.reversed()) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                return widget;
            }
        }
        return null;
    }

    public MedicalStats getMedicalStats() {
        return entity.getData(BitterAttachmentTypes.MEDICAL_STATS);
    }

    public CompartmentInstance getHeldCompartment() {
        return heldCompartment;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public CompartmentWidget getMainCompartmentWidget() {
        return compartmentWidgets.getFirst();
    }

    public void setHeldCompartment(CompartmentInstance heldCompartment) {
        this.heldCompartment = heldCompartment;
    }

    private String getName(Entity entity) {
        if (entity == null) return "Unknown";

        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);
        if (character != null) {
            return character.getName();
        } else {
            return entity.getDisplayName().getString();
        }
    }

    public String getPlayerName() {
        return getName(Minecraft.getInstance().player);
    }

    public String getTargetName() {
        return getName(entity);
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

        if (mc.hitResult instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof LivingEntity entity) {
            mc.setScreen(new HealthScreen(entity));
        } else {
            mc.setScreen(new HealthScreen(player));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void sendMessage(String message) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        assert mc.player != null;

        Character character = CharacterManager.get(mc.level).getActiveCharacter(mc.player);
        if (character != null) {
            mc.player.connection.sendChat(message);
        }
    }
}

