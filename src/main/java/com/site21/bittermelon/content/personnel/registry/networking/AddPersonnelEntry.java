package com.site21.bittermelon.content.personnel.registry.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.content.personnel.registry.PersonnelRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AddPersonnelEntry(PersonnelEntry entry) implements CustomPacketPayload {
    public static final Type<AddPersonnelEntry> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "add_personnel_entry"));

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, AddPersonnelEntry> STREAM_CODEC = StreamCodec.composite(
            PersonnelEntry.STREAM_CODEC,
            AddPersonnelEntry::entry,
            AddPersonnelEntry::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.flow().isServerbound()) {
            PersonnelRegistry.get(ctx.player().level()).addEntry(entry);
        } else {
            PersonnelRegistry.get(ctx.player().level()).addEntryFromServer(entry);
        }
    }
}
