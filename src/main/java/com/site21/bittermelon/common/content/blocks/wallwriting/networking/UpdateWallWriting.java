package com.site21.bittermelon.common.content.blocks.wallwriting.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.world.level.block.Block.UPDATE_CLIENTS;

public record UpdateWallWriting(BlockPos pos, String[] text, boolean is_final) implements CustomPacketPayload {
    public static final Type<UpdateWallWriting> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_wall_writing"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, UpdateWallWriting> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            UpdateWallWriting::pos,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).map(
                    list -> list.toArray(new String[0]),
                    List::of
            ),
            UpdateWallWriting::text,
            ByteBufCodecs.BOOL, UpdateWallWriting::is_final,
            UpdateWallWriting::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(pos) instanceof WallWritingBlockEntity wallWriting) {
            wallWriting.updateText(text);
            wallWriting.getLevel().sendBlockUpdated(pos, wallWriting.getBlockState(), wallWriting.getBlockState(), UPDATE_CLIENTS);
            if (is_final) {
                boolean valid = false;
                for (Component component : wallWriting.getText().getMessages(false)) {
                    if (!component.toString().equals("empty") & !component.getString().isBlank()) {
                        valid = true;
                        break;
                    }



                }
                if (!valid) {
                    wallWriting.getLevel().setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_CLIENTS);
                } else {
                    wallWriting.getLevel().playSound(null, wallWriting.getBlockPos(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
    }
}
