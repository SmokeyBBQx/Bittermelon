package com.site21.bittermelon.systems.personnel.privilege.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.personnel.registry.PersonnelRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SetPrivilegeForEntry(int entryID, String privilege, boolean value) implements CustomPacketPayload {
    public static final Type<SetPrivilegeForEntry> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "set_privilege_for_entry"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SetPrivilegeForEntry> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetPrivilegeForEntry::entryID,
            ByteBufCodecs.STRING_UTF8, SetPrivilegeForEntry::privilege,
            ByteBufCodecs.BOOL, SetPrivilegeForEntry::value,
            SetPrivilegeForEntry::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        registry.getEntry(entryID).setPrivilege(privilege, value);
        if (ctx.flow().isServerbound()) {
            registry.setDirty();
            PacketDistributor.sendToAllPlayers(new SetPrivilegeForEntry(entryID, privilege, value));
        }
    }
}
