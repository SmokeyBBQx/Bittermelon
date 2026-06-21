package com.site21.bittermelon.common.systems.electronics.privilege.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeOwner;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RemovePrivilegeForBE(BlockPos pos, String privilege) implements CustomPacketPayload {
    public static final Type<RemovePrivilegeForBE> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "remove_privilege_for_be"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, RemovePrivilegeForBE> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            RemovePrivilegeForBE::pos,
            ByteBufCodecs.STRING_UTF8,
            RemovePrivilegeForBE::privilege,
            RemovePrivilegeForBE::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(pos) instanceof BlockEntity blockEntity) {
            if (blockEntity instanceof PrivilegeOwner privilegeOwner) {
                privilegeOwner.getPrivileges().remove(privilege);
                blockEntity.setChanged();
            }
        }
    }
}
