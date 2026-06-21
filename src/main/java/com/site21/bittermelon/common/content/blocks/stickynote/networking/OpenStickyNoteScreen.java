package com.site21.bittermelon.common.content.blocks.stickynote.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.gui.ScreenSetter;
import com.site21.bittermelon.common.content.blocks.stickynote.StickyNoteBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * <strong>S2C</strong> <br>
 * Opens sticky note editing screen
 * @param pos Position of sticky note
 * @param noteIndex Which note is being edited
 */
public record OpenStickyNoteScreen(BlockPos pos, int noteIndex) implements CustomPacketPayload {
    public static final Type<OpenStickyNoteScreen> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_sticky_note_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenStickyNoteScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenStickyNoteScreen::pos,
            ByteBufCodecs.INT,
            OpenStickyNoteScreen::noteIndex,
            OpenStickyNoteScreen::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(pos) instanceof StickyNoteBlockEntity stickyNote) {
            ScreenSetter.displayStickyNoteScreen(stickyNote, noteIndex);
        }
    }
}
