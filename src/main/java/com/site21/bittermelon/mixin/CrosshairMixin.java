package com.site21.bittermelon.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.stickynote.StickyNoteBlock;
import com.site21.bittermelon.common.content.blocks.stickynote.StickyNoteBlockEntity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.site21.bittermelon.init.neoforge.BitterBlockTags.INSPECTABLE;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.YELLOW_INSPECTION_POSTER;

@Mixin(Gui.class)
public class CrosshairMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void renderCustomCrosshair(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;

        if (minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) minecraft.hitResult;
            BlockPos pos = blockHitResult.getBlockPos();
            BlockState blockState = minecraft.level.getBlockState(pos);

            if (blockState.getBlock() instanceof StickyNoteBlock stickyNoteBlock
                    && minecraft.level.getBlockEntity(pos) instanceof StickyNoteBlockEntity stickyNote) {
                StickyNoteBlock.Position position = stickyNoteBlock.getPosition(blockState, blockHitResult.getLocation(), pos);
                String note = stickyNote.getNotes()[position.ordinal()];
                if (note == null) return;

                boolean hasNote = stickyNoteBlock.hasNoteAtPosition(position, blockState) && !note.isEmpty();

                if (hasNote) {
                    bittermelon$renderCustomCrosshairTexture(guiGraphics, minecraft);
                    ci.cancel();
                }
                return;
            }

            // TODO: Inspectable tag is not working, need to figure out why
            if (blockState.is(INSPECTABLE) || blockState.is(YELLOW_INSPECTION_POSTER)) {
                bittermelon$renderCustomCrosshairTexture(guiGraphics, minecraft);
                ci.cancel();
            }
        }
    }

    @Unique
    private void bittermelon$renderCustomCrosshairTexture(GuiGraphics guiGraphics, @NotNull Minecraft minecraft) {
        Options options = minecraft.options;

        if (!options.getCameraType().isFirstPerson() ||
                (Objects.requireNonNull(minecraft.gameMode).getPlayerMode() == GameType.SPECTATOR && !canRenderCrosshairForSpectator(minecraft.hitResult))) {
            return;
        }

        ResourceLocation customCrosshairTexture = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "icon/inspect_crosshair");
        int crosshairSize = 12;
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                customCrosshairTexture,
                (screenWidth - crosshairSize) / 2,
                (screenHeight - crosshairSize) / 2,
                crosshairSize,
                crosshairSize
        );
    }

    @Shadow
    private boolean canRenderCrosshairForSpectator(HitResult hitResult) {
        return false;
    }
}
