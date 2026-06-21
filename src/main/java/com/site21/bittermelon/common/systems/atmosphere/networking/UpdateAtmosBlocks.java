package com.site21.bittermelon.common.systems.atmosphere.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.atmosphere.AtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.data.AtmosLevelData;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UpdateAtmosBlocks(UUID uuid, boolean add, long blockPos) implements CustomPacketPayload {
    public static final Type<UpdateAtmosBlocks> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_atmos_blocks"));

    public static final StreamCodec<ByteBuf, UpdateAtmosBlocks> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            UpdateAtmosBlocks::uuid,
            ByteBufCodecs.BOOL,
            UpdateAtmosBlocks::add,
            ByteBufCodecs.VAR_LONG,
            UpdateAtmosBlocks::blockPos,
            UpdateAtmosBlocks::new
    );

    @Override
    public @NotNull Type<?> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
            AtmosLevelData data = AtmosLevelData.get(ctx.player().level());
            AtmosInstance instance = data.getAtmosInstance(uuid);
            if (instance != null) {
                if (add) {
                    instance.addBlock(blockPos, ctx.player().level());
                } else {
                    instance.removeBlock(blockPos, ctx.player().level());
                }
            }
    }
}