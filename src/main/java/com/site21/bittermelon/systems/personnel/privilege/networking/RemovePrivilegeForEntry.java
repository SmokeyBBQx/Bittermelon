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

public record RemovePrivilegeForEntry(int entryID, String privilege) implements CustomPacketPayload {
    public static final Type<RemovePrivilegeForEntry> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "remove_privilege_for_entry"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, RemovePrivilegeForEntry> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RemovePrivilegeForEntry::entryID,
            ByteBufCodecs.STRING_UTF8, RemovePrivilegeForEntry::privilege,
            RemovePrivilegeForEntry::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        registry.getEntry(entryID).removePrivilege(privilege);
        if (ctx.flow().isServerbound()) {
            registry.setDirty();
            PacketDistributor.sendToAllPlayers(new RemovePrivilegeForEntry(entryID, privilege));
        }
    }
}
