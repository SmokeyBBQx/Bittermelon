package com.site21.bittermelon.common.systems.personnel.privilege.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SetPrivilegeForGroup(String group, String privilege, boolean value) implements CustomPacketPayload {
    public static final Type<SetPrivilegeForGroup> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "set_privilege_for_group"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SetPrivilegeForGroup> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SetPrivilegeForGroup::group,
            ByteBufCodecs.STRING_UTF8, SetPrivilegeForGroup::privilege,
            ByteBufCodecs.BOOL, SetPrivilegeForGroup::value,
            SetPrivilegeForGroup::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        manager.getPrivilegeGroup(group).setPrivilege(privilege, value);
        if (ctx.flow().isServerbound()) {
            manager.setDirty();
            PacketDistributor.sendToAllPlayers(new SetPrivilegeForGroup(group, privilege, value));
        }
    }
}
