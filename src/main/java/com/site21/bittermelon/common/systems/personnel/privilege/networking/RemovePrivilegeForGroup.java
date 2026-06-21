package com.site21.bittermelon.common.systems.personnel.privilege.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RemovePrivilegeForGroup(String group, String privilege) implements CustomPacketPayload {
    public static final Type<RemovePrivilegeForGroup> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "remove_privilege_for_group"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, RemovePrivilegeForGroup> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, RemovePrivilegeForGroup::group,
            ByteBufCodecs.STRING_UTF8, RemovePrivilegeForGroup::privilege,
            RemovePrivilegeForGroup::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        manager.getPrivilegeGroup(group).removePrivilege(privilege);
        if (ctx.flow().isServerbound()) {
            manager.setDirty();
            PacketDistributor.sendToAllPlayers(new RemovePrivilegeForGroup(group, privilege));
        }
    }
}
