package com.site21.bittermelon.content.items.wires.wire.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.systems.electronics.wiring.OutputPort;
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

public record SpliceOutputWire(BlockPos connectorPos, BlockPos targetPos, String connectorID, String targetID) implements CustomPacketPayload {
    public static final Type<SpliceOutputWire> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "splice_output_wire"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SpliceOutputWire> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SpliceOutputWire::connectorPos,
            BlockPos.STREAM_CODEC,
            SpliceOutputWire::targetPos,
            ByteBufCodecs.STRING_UTF8,
            SpliceOutputWire::connectorID,
            ByteBufCodecs.STRING_UTF8,
            SpliceOutputWire::targetID,
            SpliceOutputWire::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        OutputPort connectorPort = null;
        OutputPort targetPort = null;

        if (level.getBlockEntity(connectorPos) instanceof ElectronicDevice electronic) {
            connectorPort = electronic.findOutputPort(connectorID);
        }

        if (level.getBlockEntity(targetPos) instanceof ElectronicDevice electronic) {
            targetPort = electronic.findOutputPort(targetID);
        }

        if (connectorPort == null || targetPort == null) return;

        connectorPort.connectTo(targetPort, true);

        if (level.getBlockEntity(connectorPos) instanceof BlockEntity entity) {
            entity.setChanged();
        }
    }
}
