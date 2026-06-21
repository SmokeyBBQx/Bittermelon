package com.site21.bittermelon.common.content.blocks.stickynote.networking;

import com.site21.bittermelon.common.content.blocks.stickynote.StickyNoteBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.sounds.SoundSource.BLOCKS;

/**
 * <strong>C2S</strong> <br>
 * Replaces original text with updated text
 * @param pos Position of sticky note
 * @param message New text to replace original with
 * @param noteIndex Which note is being edited
 */
public record UpdateStickyNote(BlockPos pos, String message, int noteIndex) implements CustomPacketPayload {
    public static final Type<UpdateStickyNote> TYPE = new Type<>(Identifier.fromNamespaceAndPath("bittermelon", "update_sticky_note"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, UpdateStickyNote> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            UpdateStickyNote::pos,
            ByteBufCodecs.STRING_UTF8,
            UpdateStickyNote::message,
            ByteBufCodecs.INT,
            UpdateStickyNote::noteIndex,
            UpdateStickyNote::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();

        if (level.getBlockEntity(pos) instanceof StickyNoteBlockEntity stickyNote) {
            stickyNote.setNote(noteIndex, message);
            level.playSound(null, pos, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, BLOCKS, 1.0f, 1.0f);
        }
    }
}
