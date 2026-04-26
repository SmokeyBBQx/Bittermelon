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
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.client.renderstate.RenderStateExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        if (activeWidget == compartmentSpace) {
            activeWidget = null;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (CompartmentWidget widget : compartmentWidgets) {
            if (widget != activeWidget) {
                widget.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        if (activeWidget != null) {
            activeWidget.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        renderHeldCompartment(guiGraphics, mouseX, mouseY);

        for (InstrumentWidget widget : instrumentWidgets) {
            widget.render(guiGraphics, mouseX, mouseY, partialTick);
            if (widget == heldTool) {
                // Renders at mouse position if held
                heldTool.renderTool(guiGraphics, mouseX, mouseY);
            }
        }

        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer entityrenderer = entityrenderdispatcher.getRenderer(entity);
        // Neo: use fresh render state to support multiple entities of the same type within a single frame
        EntityRenderState entityrenderstate = entityrenderer.createRenderState();
        entityrenderer.extractRenderState(entity, entityrenderstate, 1.0F);
        RenderStateExtensions.onUpdateEntityRenderState(entityrenderer, entity, entityrenderstate);

        if (!(entityrenderstate instanceof LivingEntityRenderState livingEntityRenderState)) return;

        AnatomyPictureInPictureRenderer.RenderState renderState = new AnatomyPictureInPictureRenderer.RenderState(
                livingEntityRenderState,
                entity,
                getModelPart(mouseX, mouseY, 150, 150, 80.0f),
                new Vector3f(),
                new Quaternionf(),
                null,
                0,
                0,
                300,
                300,
                80.0f,
                guiGraphics.peekScissorStack()
        );
        guiGraphics.submitPictureInPictureRenderState(renderState);

//        renderDebugPartBounds(guiGraphics, mouseX, mouseY);
    }

    private String getModelPart(int mouseX, int mouseY, int renderX, int renderY, float scale) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<?, ?> renderer = dispatcher.getRenderer(entity);
        if (!(renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) return null;

        LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?> castRenderer =
                (LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>) livingRenderer;
        EntityModel<LivingEntityRenderState> model =
                (EntityModel<LivingEntityRenderState>) castRenderer.getModel();

        Map<String, AABB> allBounds = new HashMap<>();
        Matrix4f root = new Matrix4f().identity();

        for (Map.Entry<String, ModelPart> entry : model.root().children.entrySet()) {
            collectPartBounds(entry.getValue(), root, entry.getKey(), scale, renderX, renderY, allBounds);
        }

        return getHoveredPart(mouseX, mouseY, allBounds);
    }

    private static @Nullable String getHoveredPart(int mouseX, int mouseY, Map<String, AABB> allBounds) {
        String best = null;
        double smallestArea = Double.MAX_VALUE;

        for (Map.Entry<String, AABB> entry : allBounds.entrySet()) {
            AABB bounds = entry.getValue();

            boolean hovered = mouseX >= bounds.minX && mouseX <= bounds.maxX
                    && mouseY >= bounds.minY && mouseY <= bounds.maxY;

            if (hovered) {
                double area = (bounds.maxX - bounds.minX) * (bounds.maxY - bounds.minY);
                if (area < smallestArea) {
                    smallestArea = area;
                    best = entry.getKey();
                }
            }
        }
        return best;
    }

    private void collectPartBounds(ModelPart part, Matrix4f parentTransform, String name, float scale, int renderX,
                                   int renderY, Map<String, AABB> boundsOut) {
        Matrix4f local = new Matrix4f(parentTransform);
        local.translate(part.x / 16.0f, part.y / 16.0f, part.z / 16.0f);
        local.rotateZYX(part.zRot, part.yRot, part.xRot);

        for (ModelPart.Cube cube : part.cubes) {
            AABB bounds = projectCubeToScreen(cube, local, renderX, renderY, scale);
            if (bounds == null) continue;

            boundsOut.merge(name, bounds, (a, b) -> new AABB(
                    Math.min(a.minX, b.minX), Math.min(a.minY, b.minY), 0,
                    Math.max(a.maxX, b.maxX), Math.max(a.maxY, b.minX), 0
            ));
        }
    }

    private AABB projectCubeToScreen(ModelPart.Cube cube, Matrix4f transform,
                                        int renderX, int renderY, float scale) {
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE;

        float[] xs = { cube.minX / 16.0f, cube.maxX / 16.0f };
        float[] ys = { cube.minY / 16.0f, cube.maxY / 16.0f };
        float[] zs = { cube.minZ / 16.0f, cube.maxZ / 16.0f };

        Vector4f corner = new Vector4f();
        for (float x : xs) {
            for (float y : ys) {
                for (float z : zs) {
                    corner.set(x, y, z, 1.0f);
                    transform.transform(corner);

                    float screenX = renderX + corner.x * scale;
                    float screenY = renderY + corner.y * scale;

                    minX = Math.min(minX, screenX);
                    minY = Math.min(minY, screenY);
                    maxX = Math.max(maxX, screenX);
                    maxY = Math.max(maxY, screenY);
                }
            }
        }

        if (maxX <= minX || maxY <= minY) return null;
        return new AABB(minX, minY, 0, maxX, maxY, 0);
    }


    private void renderDebugPartBounds(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<?, ?> renderer = dispatcher.getRenderer(entity);
        if (!(renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) return;

        LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?> castRenderer =
                (LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>) livingRenderer;
        EntityModel<LivingEntityRenderState> model =
                (EntityModel<LivingEntityRenderState>) castRenderer.getModel();

        float renderX = 150, renderY = 150, scale = 80.0f;

        Map<String, AABB> allBounds = new HashMap<>();
        Matrix4f root = new Matrix4f().identity();

        for (Map.Entry<String, ModelPart> entry : model.root().children.entrySet()) {
            collectPartBounds(entry.getValue(), root, entry.getKey(), scale, (int) renderX, (int) renderY, allBounds);
        }

        for (Map.Entry<String, AABB> entry : allBounds.entrySet()) {
            AABB bounds = entry.getValue();
            int minX = (int) bounds.minX, minY = (int) bounds.minY;
            int maxX = (int) bounds.maxX, maxY = (int) bounds.maxY;

            boolean hovered = mouseX >= minX && mouseX <= maxX
                    && mouseY >= minY && mouseY <= maxY;

            guiGraphics.fill(minX, minY, maxX, maxY,
                    hovered ? 0x4400FF00 : 0x44FF0000);
            guiGraphics.renderOutline(minX, minY, maxX - minX, maxY - minY,
                    hovered ? 0xFF00FF00 : 0x80FF0000);
            if (hovered) {
                guiGraphics.drawString(font, entry.getKey(), minX, minY - 10, 0xFF00FF00);
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

        if (activeWidget != null) {
            if (compartmentWidgetClick(activeWidget, previouslyHeld, mouseX, mouseY, button)) {
                return true;
            }
        }

        for (CompartmentWidget widget : compartmentWidgets.reversed()) {
            if (widget == activeWidget) continue;
            if (compartmentWidgetClick(widget, previouslyHeld, mouseX, mouseY, button)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean compartmentWidgetClick(@NotNull CompartmentWidget widget, CompartmentInstance previouslyHeld,
                                           double mouseX, double mouseY, int button) {
        if (widget.mouseClicked(mouseX, mouseY, button)) {
            // Ensure that removed compartments are not re-added as active
            if (compartmentWidgets.contains(widget)) {
                activeWidget = widget;
            }

            if (previouslyHeld != null) {
                if (widget.tryToPlace((int) mouseX, (int) mouseY, previouslyHeld)) {
                    heldCompartment = null;
                }
            }
            return true;
        }
        return false;
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

