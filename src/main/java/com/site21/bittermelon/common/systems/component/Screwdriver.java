package com.site21.bittermelon.common.systems.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.electronics.PanelDevice;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.site21.bittermelon.init.neoforge.BitterSounds.*;

public record Screwdriver(int screwDuration, Holder<SoundEvent> screwSound) {
    public static final Codec<Screwdriver> CODEC;
    public static final Screwdriver DEFAULT = new Screwdriver(60, BitterSounds.SCREWDRIVER);

    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        if (level.isClientSide || player == null) return InteractionResult.PASS;

        if (level.getBlockEntity(clickedPos) instanceof PanelDevice) {
            player.startUsingItem(context.getHand());
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (level.isClientSide) return stack;

        if (entity instanceof Player player) {
            BlockPos targetPos = getTargetBlockPos(player);
            if (targetPos == null) return stack;

            if (level.getBlockEntity(targetPos) instanceof PanelDevice panelDevice) {
                panelDevice.togglePanel();

                playPanelSound(level, targetPos, panelDevice.isPanelOpen());
                player.displayClientMessage(Component.literal("You " + (panelDevice.isPanelOpen() ? "open" : "close") + " the panel.")
                                .withStyle(ChatFormatting.ITALIC)
                                .withStyle(ChatFormatting.GRAY),
                        true);
            }
        }

        return stack;
    }

    private @Nullable BlockPos getTargetBlockPos(@NotNull LivingEntity entity) {
        var hitResult = entity.pick(entity.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), 0.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) hitResult).getBlockPos();
        }
        return null;
    }

    private void playPanelSound(@NotNull Level level, BlockPos pos, boolean open) {
        level.playSound(null, pos, open ? SCREWDRIVER_OPEN.value() : SCREWDRIVER_CLOSE.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    public void playScrewSound(@NotNull Level level, @NotNull LivingEntity entity, int remainingUseDuration) {
        if (remainingUseDuration % 20 == 0) {
            level.playSound(null, entity.getOnPos(), screwSound().value(), SoundSource.AMBIENT, 0.3f, 1f);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(screwDuration, screwSound);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return obj instanceof Screwdriver(int duration, Holder<SoundEvent> sound) &&
                    this.screwDuration == duration &&
                    Objects.equals(this.screwSound, sound);
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("screwDuration").forGetter(Screwdriver::screwDuration),
                SoundEvent.CODEC.fieldOf("screwSound").forGetter(Screwdriver::screwSound)
        ).apply(instance, Screwdriver::new));
    }
}
