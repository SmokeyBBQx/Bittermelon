package com.site21.bittermelon.common.content.items.wire.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SpliceInputWire(BlockPos connectorPos, BlockPos targetPos, String connectorID, String targetID) implements CustomPacketPayload {
    public static final Type<SpliceInputWire> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "splice_input_wire"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SpliceInputWire> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SpliceInputWire::connectorPos,
            BlockPos.STREAM_CODEC,
            SpliceInputWire::targetPos,
            ByteBufCodecs.STRING_UTF8,
            SpliceInputWire::connectorID,
            ByteBufCodecs.STRING_UTF8,
            SpliceInputWire::targetID,
            SpliceInputWire::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        InputPort connectorPort = null;
        InputPort targetPort = null;

        if (level.getBlockEntity(connectorPos) instanceof ElectronicDevice electronic) {
            connectorPort = electronic.findInputPort(connectorID);
        }

        if (level.getBlockEntity(targetPos) instanceof ElectronicDevice electronic) {
            targetPort = electronic.findInputPort(targetID);
        }

        if (connectorPort == null || targetPort == null) return;

        connectorPort.connectTo(targetPort, true);

        if (level.getBlockEntity(connectorPos) instanceof BlockEntity entity) {
            entity.setChanged();
        }
    }
}
