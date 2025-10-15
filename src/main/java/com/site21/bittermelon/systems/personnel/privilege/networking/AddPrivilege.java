package com.site21.bittermelon.systems.personnel.privilege.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.personnel.privilege.PrivilegeManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AddPrivilege(String privilege) implements CustomPacketPayload {
    public static final Type<AddPrivilege> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "add_privilege"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, AddPrivilege> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            AddPrivilege::privilege,
            AddPrivilege::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        if (ctx.flow().isServerbound()) {
            manager.addPrivilege(privilege);
        } else {
            manager.addPrivilegeFromServer(privilege);
        }
    }
}
