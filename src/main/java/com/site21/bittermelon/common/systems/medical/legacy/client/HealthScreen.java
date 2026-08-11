package com.site21.bittermelon.common.systems.medical.legacy.client;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.legacy.client.tool.InstrumentWidget;
import com.site21.bittermelon.common.systems.medical.legacy.client.tool.InstrumentWidgets;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.VisualData;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.legacy.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
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
    private final AnatomyModelWidget anatomyWidget;
    private CompartmentWidget activeWidget = null;
    private CompartmentInstance heldCompartment = null;
    private InstrumentWidget heldTool = null;

    public HealthScreen(@NotNull LivingEntity entity) {
        super(Component.literal("Health Screen"));
        this.entity = entity;
        this.compartmentWidgets = new ArrayList<>();
        this.instrumentWidgets = new ArrayList<>();
        this.anatomyWidget = new AnatomyModelWidget(width / 2, 0, 200, 300, 100.0f, entity, this);
        initTools();
    }

    private void initTools() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        player.getInventory().iterator().forEachRemaining(stack -> {
            if (stack.isEmpty()) return;

            for (var entry : InstrumentWidgets.INSTRUMENT_WIDGETS.entrySet()) {
                if (stack.has(entry.getKey())) {
                    instrumentWidgets.add(entry.getValue().create(stack, 10, 10, 32, 32, this));
                    break;
                }
            }
        });
    }

    public boolean addCompartmentSpace(@NotNull CompartmentInstance instance) {
        // Only open compartments that have layers
        LayerData layer = CompartmentUtil.getTopLayer(instance);
        if (layer == null) return false;
        if (compartmentWidgets.stream().anyMatch(widget -> widget.getCompartment().equals(instance)))
            return false;

        CompartmentWidget widget = new CompartmentWidget(20, 20, layer.getWidth() * 20,
                layer.getHeight() * 20, instance, this);
        compartmentWidgets.add(widget);
        activeWidget = widget;

        return true;
    }

    public void removeCompartmentSpace(CompartmentWidget compartmentSpace) {
        compartmentWidgets.remove(compartmentSpace);

        if (activeWidget == compartmentSpace) {
            activeWidget = null;
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        anatomyWidget.extractRenderState(graphics, mouseX, mouseY, partialTicks);

        for (CompartmentWidget widget : compartmentWidgets) {
            if (widget != activeWidget) {
                widget.extractRenderState(graphics, mouseX, mouseY, partialTicks);
            }
        }

        if (activeWidget != null) {
            activeWidget.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        }

        renderHeldCompartment(graphics, mouseX, mouseY);

        for (InstrumentWidget widget : instrumentWidgets) {
            widget.extractRenderState(graphics, mouseX, mouseY, partialTicks);
            if (widget == heldTool) {
                // Renders at mouse position if held
                heldTool.renderTool(graphics, mouseX, mouseY);
            }
        }
    }

    private void renderHeldCompartment(GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY) {
        if (heldCompartment == null) return;
        if (heldCompartment.has(VISUAL_DATA)) return;

        VisualData visualData = heldCompartment.get(VISUAL_DATA);
        assert visualData != null;

        if (visualData.icon() != null) {
            float scaleFactor = visualData.scale();
            GuiGraphicsExtractor.pose().pushMatrix();
            GuiGraphicsExtractor.pose().translate(mouseX + visualData.x(), mouseY + visualData.y());
            GuiGraphicsExtractor.pose().scale(scaleFactor, scaleFactor);

            GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, visualData.icon(), 0, 0, 0, 0, 20,
                    20, 20, 20);

            GuiGraphicsExtractor.pose().popMatrix();
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        CompartmentInstance previouslyHeld = heldCompartment;

        if (heldTool != null) {
            if (!heldTool.mouseClicked(event, doubleClick)) {
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

        if (activeWidget != null) {
            if (compartmentWidgetClick(activeWidget, previouslyHeld, event, doubleClick)) {
                return true;
            }
        }

        for (CompartmentWidget widget : compartmentWidgets.reversed()) {
            if (widget == activeWidget) continue;
            if (compartmentWidgetClick(widget, previouslyHeld, event, doubleClick)) {
                return true;
            }
        }

        if (anatomyWidget.mouseClicked(event, doubleClick)) {
            return true;
        }

        return super.mouseClicked(event, doubleClick);
    }

    private boolean compartmentWidgetClick(@NotNull CompartmentWidget widget, CompartmentInstance previouslyHeld,
                                           MouseButtonEvent event, boolean doubleClick) {
        if (widget.mouseClicked(event, doubleClick)) {
            // Ensure that removed compartments are not re-added as active
            if (compartmentWidgets.contains(widget)) {
                activeWidget = widget;
            }

            if (previouslyHeld != null) {
                if (widget.tryToPlace((int) event.x(), (int) event.y(), previouslyHeld)) {
                    heldCompartment = null;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (heldTool != null) {
            return heldTool.mouseDragged(event, dx, dy);
        }

        if (activeWidget != null && activeWidget.isMouseOver(event.x(), event.y())) {
            return activeWidget.mouseDragged(event, dx, dy);
        }

        anatomyWidget.mouseDragged(event, dx, dy);

        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (heldTool != null) {
            return heldTool.mouseReleased(event);
        }

        if (activeWidget != null) {
            boolean result = activeWidget.mouseReleased(event);
//            activeWidget = null;
            return result;
        }

        return super.mouseReleased(event);
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

