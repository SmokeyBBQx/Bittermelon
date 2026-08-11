//package com.site21.bittermelon.common.systems.medical.client.minigame;
//
//import com.site21.bittermelon.common.systems.character.Character;
//import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphicsExtractor;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.Identifier;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.world.entity.player.Player;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//import org.jetbrains.annotations.NotNull;
//import org.lwjgl.glfw.GLFW;
//
//
//public class CPRScreen extends Screen {
//    private static final int CIRCLE_RADIUS = 40;
//    // TODO: New indicator texture
//    private static final Identifier INDICATOR_TEXTURE = Identifier.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/medical/cpr_indicator.png");
//    private static final int ICON_SIZE = 8;
//    private static final float SUCCESS_ZONE_SIZE = 50f;
//    private static final float GREAT_ZONE_SIZE = 10f;
//
//    private static final float LEFT_SUCCESS_ZONE_START = 180 - (SUCCESS_ZONE_SIZE / 2);
//    private static final float LEFT_SUCCESS_ZONE_END = LEFT_SUCCESS_ZONE_START + SUCCESS_ZONE_SIZE;
//    private static final float LEFT_GREAT_ZONE_START = LEFT_SUCCESS_ZONE_START + (SUCCESS_ZONE_SIZE - GREAT_ZONE_SIZE) / 2;
//    private static final float LEFT_GREAT_ZONE_END = LEFT_GREAT_ZONE_START + GREAT_ZONE_SIZE;
//
//    private static final float RIGHT_SUCCESS_ZONE_START = -(SUCCESS_ZONE_SIZE / 2);
//    private static final float RIGHT_SUCCESS_ZONE_END = RIGHT_SUCCESS_ZONE_START + SUCCESS_ZONE_SIZE;
//    private static final float RIGHT_GREAT_ZONE_START = RIGHT_SUCCESS_ZONE_START + (SUCCESS_ZONE_SIZE - GREAT_ZONE_SIZE) / 2;
//    private static final float RIGHT_GREAT_ZONE_END = RIGHT_GREAT_ZONE_START + GREAT_ZONE_SIZE;
//
//    private static final float THICKNESS_INCREASE = 0.5f;
//    private static final int PRESS_INDICATOR_TIME = 5;
//    private int pressTime = 0;
//    private float thicknessIncrease = 0;
//    private boolean keyPressed = false;
//
//    private float currentAngle = 0;
//    private float rotationSpeed = 300f;
//    private long lastTime = System.currentTimeMillis();
//
//    private static final float BPM_DECAY_RATE = 0.5f;
//    private static final float BPM_INCREASE_GREAT = 25f;
//    private static final float BPM_INCREASE_GOOD = 15f;
//    private float BPM = 0;
//
//    private final Character character;
//
//    public CPRScreen(Character character) {
//        super(Component.literal("Perform CPRScreen"));
//        this.character = character;
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        BPM = Math.max(0, BPM - BPM_DECAY_RATE);
//
//        if (keyPressed) {
//            pressTime++;
//            thicknessIncrease = THICKNESS_INCREASE;
//
//            if (pressTime >= PRESS_INDICATOR_TIME) {
//                pressTime = 0;
//                keyPressed = false;
//                thicknessIncrease = 0;
//            }
//        }
//
//        updateCharacter();
//    }
//
//    private void updateCharacter() {
//        MedicalStats medicalStats = character.getMedicalStats();
////        medicalStats.setHeartLifeSupport(BPM / 200);
//    }
//
//    @Override
//    public void render(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
//        long currentTime = System.currentTimeMillis();
//        float deltaTime = (currentTime - lastTime) / 1000f;
//        lastTime = currentTime;
//
//        currentAngle = (currentAngle + (rotationSpeed * deltaTime)) % 360;
//
//        int centerX = width / 2;
//        int centerY = height / 2;
//
//        drawIndicator(GuiGraphicsExtractor, centerX, centerY);
//
//        drawArc(GuiGraphicsExtractor, centerX, centerY, 0, 360, 2, calculateRedTint());
//        drawArc(GuiGraphicsExtractor, centerX, centerY, LEFT_SUCCESS_ZONE_START, SUCCESS_ZONE_SIZE, 3f,0xFF06D001);
//        drawArc(GuiGraphicsExtractor, centerX, centerY, LEFT_GREAT_ZONE_START, GREAT_ZONE_SIZE, 3f, 0xFF06F500);
//
//        drawArc(GuiGraphicsExtractor, centerX, centerY, RIGHT_SUCCESS_ZONE_START, SUCCESS_ZONE_SIZE, 3f, 0xFF06D001);
//        drawArc(GuiGraphicsExtractor, centerX, centerY, RIGHT_GREAT_ZONE_START, GREAT_ZONE_SIZE, 3f, 0xFF06F500);
//    }
//
//
//    private int calculateRedTint() {
//        float bpmDifference = Math.abs(BPM - 100);
//        float redTintFactor = Math.min(1.0f, bpmDifference / 100);
//
//        int alpha = (-1 >> 24) & 0xFF;
//        int originalRed = (-1 >> 16) & 0xFF;
//        int originalGreen = (-1 >> 8) & 0xFF;
//        int originalBlue = 0xFF;
//
//        int green = (int)(originalGreen * (1.0f - redTintFactor));
//        int blue = (int)(originalBlue * (1.0f - redTintFactor));
//
//        return (alpha << 24) | (originalRed << 16) | (green << 8) | blue;
//
//    }
//
//    private void drawIndicator(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int centerX, int centerY) {
////        float radians = (float) Math.toRadians(currentAngle);
////        double indicatorX = centerX + (CIRCLE_RADIUS * Math.cos(radians));
////        double indicatorY = centerY + (CIRCLE_RADIUS * Math.sin(radians));
////
////        Matrix3x2fStack poseStack = GuiGraphicsExtractor.pose();
////        poseStack.pushMatrix();
////
////        poseStack.translate(indicatorX, indicatorY, 0);
////        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(currentAngle));
////        poseStack.translate(-ICON_SIZE / 2.0f, -ICON_SIZE /2.0f, 0);
////
////        GuiGraphicsExtractor.blit(INDICATOR_TEXTURE,
////                0, 0,
////                0, 0,
////                ICON_SIZE, ICON_SIZE,
////                ICON_SIZE, ICON_SIZE);
////
////        poseStack.popPose();
//    }
//
//    private void drawArc(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int centerX, int centerY, float startAngle, float arcSize, float thickness, int color) {
////        PoseStack poseStack = GuiGraphicsExtractor.pose();
////        Matrix4f matrix = poseStack.last().pose();
////        thickness += thicknessIncrease;
////
////        RenderSystem.enableBlend();
////        RenderSystem.defaultBlendFunc();
////        RenderSystem.setShader(GameRenderer::getPositionColorShader);
////
////        float a = (color >> 24 & 0xFF) / 255.0F;
////        float r = (color >> 16 & 0xFF) / 255.0F;
////        float g = (color >> 8 & 0xFF) / 255.0F;
////        float b = (color & 0xFF) / 255.0F;
////
////        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
////        int segments = Math.max(1, (int)(arcSize * 360 / 360));
////
////        for (int i = 0; i <= segments; i++) {
////            float angle = (float) Math.toRadians(startAngle + (i * arcSize / segments));
////            float cos = (float) Math.cos(angle);
////            float sin = (float) Math.sin(angle);
////
////            float outerX = centerX + (CIRCLE_RADIUS + thickness/2) * cos;
////            float outerY = centerY + (CIRCLE_RADIUS + thickness/2) * sin;
////            bufferBuilder.addVertex(matrix, outerX, outerY, 0)
////                    .setColor(r, g, b, a);
////
////            float innerX = centerX + (CIRCLE_RADIUS - thickness/2) * cos;
////            float innerY = centerY + (CIRCLE_RADIUS - thickness/2) * sin;
////            bufferBuilder.addVertex(matrix, innerX, innerY, 0)
////                    .setColor(r, g, b, a);
////        }
////
////        BufferUploader.drawWithShader(Objects.requireNonNull(bufferBuilder.build()));
////        RenderSystem.disableBlend();
//    }
//
//
//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if (keyCode == GLFW.GLFW_KEY_SPACE) {
//            checkSkillCheck();
//            keyPressed = true;
//            return true;
//        }
//        return super.keyPressed(keyCode, scanCode, modifiers);
//    }
//
//    private void checkSkillCheck() {
//        float normalizedAngle = (currentAngle + 360) % 360;
//        boolean inSuccessZone = isAngleBetween(normalizedAngle, LEFT_SUCCESS_ZONE_START, LEFT_SUCCESS_ZONE_END);
//        if (!inSuccessZone) inSuccessZone = isAngleBetween(normalizedAngle, RIGHT_SUCCESS_ZONE_START, RIGHT_SUCCESS_ZONE_END);
//        boolean inGreatZone = isAngleBetween(normalizedAngle, LEFT_GREAT_ZONE_START, LEFT_GREAT_ZONE_END);
//        if (!inGreatZone) inGreatZone = isAngleBetween(normalizedAngle, RIGHT_GREAT_ZONE_START, RIGHT_GREAT_ZONE_END);
//
//        Player player = Minecraft.getInstance().player;
//        if (player == null) return;
//
//        if (inGreatZone) {
//            rotationSpeed = -rotationSpeed;
//            BPM = Math.min(BPM + BPM_INCREASE_GREAT, 100);
//            player.playSound(SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON);
//        } else if (inSuccessZone) {
//            rotationSpeed = -rotationSpeed;
//            BPM = Math.min(BPM + BPM_INCREASE_GOOD, 100);
//            player.playSound(SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON);
//        } else {
//            player.playSound(SoundEvents.ITEM_BREAK.value());
//        }
//    }
//
//    private boolean isAngleBetween(float angle, float start, float end) {
//        angle = (angle + 360) % 360;
//        start = (start + 360) % 360;
//        end = (end + 360) % 360;
//
//        if (start <= end) {
//            return angle >= start && angle <= end;
//        } else {
//            return angle >= start || angle <= end;
//        }
//    }
//
//    @Override
//    public boolean isPauseScreen() {
//        return true;
//    }
//}
