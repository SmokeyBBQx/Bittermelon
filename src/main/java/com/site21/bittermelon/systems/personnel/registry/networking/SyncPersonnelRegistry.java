package com.site21.bittermelon.systems.personnel.registry.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.systems.personnel.registry.PersonnelRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record SyncPersonnelRegistry(Map<Integer, PersonnelEntry> personnelEntries) implements CustomPacketPayload {
    public static final Type<SyncPersonnelRegistry> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "sync_personnel_registry"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SyncPersonnelRegistry> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.INT,
                    PersonnelEntry.STREAM_CODEC),
            SyncPersonnelRegistry::personnelEntries,
            SyncPersonnelRegistry::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        registry.getPersonnelEntries().clear();
        registry.getPersonnelEntries().putAll(personnelEntries);
    }
}
