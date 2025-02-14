package com.site21.bittermelon.networking.server;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.blocks.blockentities.ContainmentPanelBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ContainmentNameUpdate(String name, BlockPos pos) implements CustomPacketPayload {
    public static final Type<ContainmentNameUpdate> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "containment_name_update"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ContainmentNameUpdate> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ContainmentNameUpdate::name,
            BlockPos.STREAM_CODEC,
            ContainmentNameUpdate::pos,
            ContainmentNameUpdate::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        if (level.getBlockEntity(pos()) instanceof ContainmentPanelBlockEntity blockEntity) {
            blockEntity.setName(name());
        }
    }
}
