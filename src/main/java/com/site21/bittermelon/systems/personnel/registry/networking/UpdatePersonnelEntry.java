package com.site21.bittermelon.systems.personnel.registry.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.systems.personnel.registry.PersonnelRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdatePersonnelEntry(PersonnelEntry entry) implements CustomPacketPayload {
    public static final Type<UpdatePersonnelEntry> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_personnel_entry"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, UpdatePersonnelEntry> STREAM_CODEC = StreamCodec.composite(
            PersonnelEntry.STREAM_CODEC,
            UpdatePersonnelEntry::entry,
            UpdatePersonnelEntry::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        registry.getPersonnelEntries().put(entry.getId(), entry);
        if (ctx.flow().isServerbound()) {
            registry.setDirty();
        }
    }
}
