package com.site21.bittermelon.content.items.wires.wire.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.connection.Connection;
import com.site21.bittermelon.content.blocks.devices.connection.InputPort;
import com.site21.bittermelon.content.blocks.devices.connection.OutputPort;
import com.site21.bittermelon.content.blocks.devices.connection.WireConnection;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

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

        if (level.getBlockEntity(inputPos) instanceof IElectronic electronic) {
            inputPort = electronic.findInputPort(inputID);
        }

        if (level.getBlockEntity(outputPos) instanceof IElectronic electronic) {
            outputPort = electronic.findOutputPort(outputID);
        }

        if (inputPort == null || outputPort == null) return;

        WireConnection wireConnection = new WireConnection(inputPort, outputPort);
        if (level.getBlockEntity(inputPos) instanceof IElectronic electronic) {
            electronic.getConnections().add(wireConnection);
        }

        if (level.getBlockEntity(outputPos) instanceof IElectronic electronic) {
            electronic.getConnections().add(wireConnection);
        }
    }
}
