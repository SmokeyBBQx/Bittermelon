//package com.site21.bittermelon.common.systems.medical.client.minigame;
//
//import com.site21.bittermelon.Bittermelon;
//import com.site21.bittermelon.common.systems.character.Character;
//import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
//import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
//import com.site21.bittermelon.init.neoforge.BitterSounds;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphicsExtractor;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//import org.jetbrains.annotations.NotNull;
//import org.lwjgl.glfw.GLFW;
//
//
//public class RetractMinigame extends MedicalMinigame {
//    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/retract_sprites.png");
//    private boolean retracting = false;
//    private int retractDown = height / 2 + 130;
//    private int retractUp = height / 2 + 130;
//    private boolean spaceWasPressed = false;
//
//    public RetractMinigame(ItemStack item, CompartmentInstance compartment, MedicalStats medicalStats, Character character) {
//        super(Component.literal("Retract"), item, compartment, medicalStats, character);
//    }
//
//    @Override
//    protected void init() {
//        COMPLETION_DELAY = 200;
//        SOUND_DELAY = 1690;
//    }
//
//    @Override
//    public void render(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTicks) {
//        super.render(GuiGraphicsExtractor, mouseX, mouseY, partialTicks);
//        int centerX = texW / 2 - 192;
//        int centerY = height / 2 - 40;
//        float scale = 4.0f;
//
//        GuiGraphicsExtractor.pose().pushMatrix();
//        GuiGraphicsExtractor.pose().scale(scale, scale);
//
//        if (!retracting) {
//            GuiGraphicsExtractor.blit(
//                    TEXTURE,
//                    (int) (centerX / scale),
//                    (int) (centerY / scale) - 11,
//                    0,
//                    0,
//                    95,
//                    20,
//                    192,
//                    40
//            );
//
//            GuiGraphicsExtractor.blit(
//                    TEXTURE,
//                    (int) (centerX / scale),
//                    (int) (centerY / scale) + 11,
//                    96,
//                    0,
//                    95,
//                    20,
//                    192,
//                    40
//            );
//        } else {
//            GuiGraphicsExtractor.blit(
//                    TEXTURE,
//                    (int) (centerX / scale),
//                    (int) (retractUp / scale) - 11,
//                    0,
//                    20,
//                    95,
//                    20,
//                    192,
//                    40
//            );
//
//            GuiGraphicsExtractor.blit(
//                    TEXTURE,
//                    (int) (centerX / scale),
//                    (int) (retractDown / scale) + 11,
//                    96,
//                    20,
//                    95,
//                    20,
//                    192,
//                    40
//            );
//        }
//
//        GuiGraphicsExtractor.pose().popMatrix();
//
//        if (retractUp < height / 6) {
//            isCompleting = true;
//        }
//
//        String text = "PRESS SPACE TO RETRACT";
//        int textWidth = Minecraft.getInstance().font.texW(text);
//        if (!retracting) {
//            GuiGraphicsExtractor.drawString(
//                    Minecraft.getInstance().font,
//                    Component.literal(text),
//                    texW / 2 - textWidth / 2,
//                    height / 2 + 30,
//                    0x808080
//            );
//        }
//    }
//
//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if (keyCode == GLFW.GLFW_KEY_SPACE) {
//            if (!isCompleting && !spaceWasPressed) {
//                retracting = true;
//                int retractIncrement = 4;
//                retractUp -= retractIncrement;
//                retractDown += retractIncrement;
//                makeSound(BitterSounds.RETRACT.value());
//                spaceWasPressed = true;
//                return true;
//            }
//        }
//        return super.keyPressed(keyCode, scanCode, modifiers);
//    }
//
//    @Override
//    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
//        if (keyCode == GLFW.GLFW_KEY_SPACE) {
//            if (!isCompleting) {
//                spaceWasPressed = false;
//                return true;
//            }
//        }
//        return super.keyPressed(keyCode, scanCode, modifiers);
//    }
//}
