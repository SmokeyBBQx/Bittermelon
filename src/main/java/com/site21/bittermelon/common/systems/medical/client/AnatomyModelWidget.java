package com.site21.bittermelon.common.systems.medical.client;

import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStatsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.renderstate.RenderStateExtensions;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

public class AnatomyModelWidget extends AbstractWidget {
    private final float scale;
    private final LivingEntity entity;
    private final HealthScreen screen;
    private final Model model;
    private float xRot;

    public AnatomyModelWidget(int x, int y, int width, int height, float scale, LivingEntity entity, HealthScreen screen) {
        super(x, y, width, height, entity.getName());
        this.scale = scale;
        this.entity = entity;
        this.screen = screen;
        this.model = getModel(entity);
    }

    private static Model getModel(LivingEntity entity) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<?, ?> renderer = dispatcher.getRenderer(entity);

        if (!(renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) {
            throw new IllegalStateException("Renderer for entity " + entity.getName() + " is not a LivingEntityRenderer.");
        }

        return livingRenderer.getModel();
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        AnatomyPictureInPictureRenderer.RenderState anatomyRenderState = new AnatomyPictureInPictureRenderer.RenderState(
                getLivingRenderState(),
                entity,
                getModelPart(mouseX, mouseY),
                new Vector3f(),
                new Quaternionf().rotationY(xRot),
                null,
                getX(),
                getY(),
                width,
                height,
                scale,
                guiGraphics.peekScissorStack()
        );

        guiGraphics.submitPictureInPictureRenderState(anatomyRenderState);
    }

    private void renderDebugBounds(GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY) {
        Map<String, AABB> bounds = new HashMap<>();
        Matrix4f root = new Matrix4f().identity().rotateY(xRot);

        for (Map.Entry<String, ModelPart> entry : model.root().children.entrySet()) {
            if (!screen.getMedicalStats().getAnatomyModel().getBodyParts().containsKey(entry.getKey())) continue;
            collectPartBounds(entry.getValue(), root, entry.getKey(), bounds);
        }

        String hoveredPart = getHoveredPart(mouseX, mouseY, bounds);

        for (Map.Entry<String, AABB> entry : bounds.entrySet()) {
            AABB b = entry.getValue();
            int color = entry.getKey().equals(hoveredPart) ? 0x8000FF00 : 0x80FF0000;
            GuiGraphicsExtractor.fill((int) b.minX, (int) b.minY, (int) b.maxX, (int) b.maxY, color);
            GuiGraphicsExtractor.drawString(Minecraft.getInstance().font, entry.getKey(), (int) b.minX + 2,
                    (int) b.minY + 2, 0xFFFFFFFF);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private LivingEntityRenderState getLivingRenderState() {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer renderer = dispatcher.getRenderer(entity);
        EntityRenderState renderState = renderer.createRenderState();
        renderer.extractRenderState(entity, renderState, 1.0f);
        RenderStateExtensions.onUpdateEntityRenderState(renderer, entity, renderState);

        if (!(renderState instanceof LivingEntityRenderState livingRenderState)) {
            throw new IllegalStateException("Render state for entity " + entity.getName() + " is not a LivingEntityRenderState.");
        }

        return livingRenderState;
    }

    public @Nullable String getModelPart(int mouseX, int mouseY) {
        Map<String, AABB> allBounds = new HashMap<>();
        Matrix4f root = new Matrix4f().identity().rotateY(xRot);

        for (Map.Entry<String, ModelPart> entry : model.root().children.entrySet()) {
            if (!screen.getMedicalStats().getAnatomyModel().getBodyParts().containsKey(entry.getKey())) continue;
            collectPartBounds(entry.getValue(), root, entry.getKey(), allBounds);
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
                double area = bounds.maxZ;
                if (area < smallestArea) {
                    smallestArea = area;
                    best = entry.getKey();
                }
            }
        }
        return best;
    }

    private void collectPartBounds(ModelPart part, Matrix4f parentTransform, String name, Map<String, AABB> boundsOut) {
        Matrix4f local = new Matrix4f(parentTransform);
        local.translate(part.x / 16.0f, part.y / 16.0f, part.z / 16.0f);
        local.rotateZYX(part.zRot, part.yRot, part.xRot);

        for (ModelPart.Cube cube : part.cubes) {
            AABB bounds = projectCubeToScreen(cube, local);
            if (bounds == null) continue;

            boundsOut.merge(name, bounds, (a, b) -> new AABB(
                    Math.min(a.minX, b.minX), Math.min(a.minY, b.minY), Math.min(a.minZ, b.minZ),
                    Math.max(a.maxX, b.maxX), Math.max(a.maxY, b.maxY), Math.max(a.maxZ, b.maxZ)
            ));
        }

        for (Map.Entry<String, ModelPart> child : part.children.entrySet()) {
            if (!screen.getMedicalStats().getAnatomyModel().getBodyParts().containsKey(child.getKey())) continue;
            collectPartBounds(child.getValue(), local, child.getKey(), boundsOut);
        }
    }

    private AABB projectCubeToScreen(ModelPart.Cube cube, Matrix4f transform) {
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE;
        float minZ = Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;

        float[] xs = {cube.minX / 16.0f, cube.maxX / 16.0f};
        float[] ys = {cube.minY / 16.0f, cube.maxY / 16.0f};
        float[] zs = {cube.minZ / 16.0f, cube.maxZ / 16.0f};

        Vector4f corner = new Vector4f();
        for (float x : xs) {
            for (float y : ys) {
                for (float z : zs) {
                    corner.set(x, y, z, 1.0f);
                    transform.transform(corner);

                    float screenX = getX() + (getWidth() / 2f) + corner.x * scale;
                    float screenY = getY() + (getHeight() / 2f) + corner.y * scale;

                    minX = Math.min(minX, screenX);
                    minY = Math.min(minY, screenY);
                    maxX = Math.max(maxX, screenX);
                    maxY = Math.max(maxY, screenY);
                    minZ = Math.min(minZ, corner.z);
                    maxZ = Math.max(maxZ, corner.z);
                }
            }
        }

        if (maxX <= minX || maxY <= minY) return null;
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        String part = getModelPart((int) mouseX, (int) mouseY);
        if (part != null) {
            screen.addCompartmentSpace(MedicalStatsUtil.getBodyPart(part, screen.getMedicalStats()));
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            xRot += (float) (dragX * -0.1);
            return true;
        }
        return false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
