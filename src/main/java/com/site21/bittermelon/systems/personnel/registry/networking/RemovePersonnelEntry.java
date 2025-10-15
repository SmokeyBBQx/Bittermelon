package com.site21.bittermelon.systems.personnel.registry.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.personnel.registry.PersonnelRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RemovePersonnelEntry(int entryID) implements CustomPacketPayload {
    public static final Type<RemovePersonnelEntry> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "remove_personnel_entry"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, RemovePersonnelEntry> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            RemovePersonnelEntry::entryID,
            RemovePersonnelEntry::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        if (ctx.flow().isServerbound()) {
            registry.removeEntry(entryID);
        } else {
            registry.getPersonnelEntries().remove(entryID);
        }
    }
}
