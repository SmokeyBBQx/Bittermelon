package com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record PlaySlidingDoorStuckSound(BlockPos pos) implements CustomPacketPayload {
    public static final Type<PlaySlidingDoorStuckSound> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "play_sliding_door_stuck_sound"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, PlaySlidingDoorStuckSound> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            PlaySlidingDoorStuckSound::pos,
            PlaySlidingDoorStuckSound::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        level.playSound(null, pos, BitterSounds.LARGE_SLIDING_DOOR_STUCK.get(), SoundSource.BLOCKS, 0.4f, Mth.randomBetween(level.random, 0.9f, 1f));
    }
}
