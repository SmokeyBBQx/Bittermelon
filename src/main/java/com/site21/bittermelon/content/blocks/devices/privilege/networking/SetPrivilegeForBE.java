package com.site21.bittermelon.content.blocks.devices.privilege.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeOwner;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SetPrivilegeForBE(BlockPos pos, String privilege, boolean value) implements CustomPacketPayload {
    public static final Type<SetPrivilegeForBE> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "set_privilege_for_be"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SetPrivilegeForBE> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SetPrivilegeForBE::pos,
            ByteBufCodecs.STRING_UTF8,
            SetPrivilegeForBE::privilege,
            ByteBufCodecs.BOOL,
            SetPrivilegeForBE::value,
            SetPrivilegeForBE::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(pos) instanceof BlockEntity blockEntity) {
            if (blockEntity instanceof PrivilegeOwner privilegeOwner) {
                privilegeOwner.getPrivileges().put(privilege, value);
                blockEntity.setChanged();
            }
        }
    }
}
