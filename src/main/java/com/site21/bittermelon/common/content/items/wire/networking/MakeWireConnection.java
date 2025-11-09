package com.site21.bittermelon.common.content.items.wire.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.OutputPort;
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

public record MakeWireConnection(BlockPos inputPos, BlockPos outputPos, String inputID, String outputID) implements CustomPacketPayload {
    public static final Type<MakeWireConnection> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "make_wire_connection"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, MakeWireConnection> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            MakeWireConnection::inputPos,
            BlockPos.STREAM_CODEC,
            MakeWireConnection::outputPos,
            ByteBufCodecs.STRING_UTF8,
            MakeWireConnection::inputID,
            ByteBufCodecs.STRING_UTF8,
            MakeWireConnection::outputID,
            MakeWireConnection::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        InputPort inputPort = null;
        OutputPort outputPort = null;

        if (level.getBlockEntity(inputPos) instanceof ElectronicDevice electronic) {
            inputPort = electronic.findInputPort(inputID);
        }

        if (level.getBlockEntity(outputPos) instanceof ElectronicDevice electronic) {
            outputPort = electronic.findOutputPort(outputID);
        }

        if (inputPort == null || outputPort == null) return;

        inputPort.connectTo(outputPort);
        outputPort.connectTo(inputPort);

        System.out.println("WireItem connection made. InputPort connected port: " + inputPort.getConnectedPort(level).id + " OutputPort connected port: " + outputPort.getConnectedPort(level).id);

        if (level.getBlockEntity(inputPos) instanceof BlockEntity entity) {
            entity.setChanged();
        }

        if (level.getBlockEntity(outputPos) instanceof BlockEntity entity) {
            entity.setChanged();
        }
    }
}
