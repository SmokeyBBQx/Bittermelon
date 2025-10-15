package com.site21.bittermelon.common.systems.personnel.privilege.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RemovePrivilege(String privilege) implements CustomPacketPayload {
    public static final Type<RemovePrivilege> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "remove_privilege"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, RemovePrivilege> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            RemovePrivilege::privilege,
            RemovePrivilege::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        if (ctx.flow().isServerbound()) {
            manager.removePrivilege(privilege);
        } else {
            manager.getPrivileges().remove(privilege);
        }
    }
}
